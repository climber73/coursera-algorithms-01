//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FastCollinearPoints {
    private Point[] points;
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            if (p == null) throw new IllegalArgumentException("one of the points is null");
            for (int j = i + 1; j < points.length; j++) {
                Point r = points[j];
                if (p.compareTo(r) == 0) throw new IllegalArgumentException("repetitions" + p.toString());
            }
        }
        this.points = points.clone();
    }

    public int numberOfSegments() {
        return segments().length;
    }

    public LineSegment[] segments() {
        if (segments != null) return segments;

        LineSegmentPool lineSegmentPool = new LineSegmentPool();
        for (int i = 0; i < points.length; i++) {
            // get array of slopes:
            double[] slopes = new double[points.length];
            for (int j = 0; j < points.length; j++) {
                slopes[j] = points[i].slopeTo(points[j]);
            }
            // get segments from array of slopes:
            SegmentPool pool = new SegmentPool(points.length, i);
            for (int j = 0; j < points.length; j++) {
                pool.addForSlope(slopes[j], j);
            }

            for (Segment s : pool) {
                lineSegmentPool.add(s.lineSegment());
            }
        }
        segments = lineSegmentPool.asArray();
        return segments;
    }

//    private SegmentPool test(int n) {
//        return new SegmentPool(n, 0);
//    }

    private class SegmentPool implements Iterable<Segment> {
        int lastSegmentIndex;
        Segment[] segments;
        int n;
        int base;

        SegmentPool(int n, int base) {
            lastSegmentIndex = -1;
            segments = new Segment[n];
            this.n = n;
            this.base = base;
        }

        void addForSlope(double slope, int index) {
            boolean added = false;
            for (int i = 0; i <= lastSegmentIndex; i++) {
                if (segments[i].slope == slope) {
                    segments[i].add(index);
                    added = true;
                }
            }

            if (added) return;

            Segment segment = new Segment(base, slope);
            segment.add(index);
            if (lastSegmentIndex < 0) {
                segments[0] = segment;
                lastSegmentIndex = 0;
            } else {
                lastSegmentIndex++;
                segments[lastSegmentIndex] = segment;
            }
        }

        void print() {
            for (int i = 0; i <= lastSegmentIndex; i++) {
                if (segments[i].size() >= 3) segments[i].print();
            }
            System.out.println("=======");
        }

        public Iterator<Segment> iterator() {
            return new MyIterator();
        }

        private class MyIterator implements Iterator<Segment> {
            int index = 0;

            @Override
            public boolean hasNext() {
                while (index <= lastSegmentIndex) {
                    if (segments[index].size() >= 4) break;
                    index++;
                }
                return index <= lastSegmentIndex;
            }

            @Override
            public Segment next() {
                if (!hasNext()) throw new NoSuchElementException();
                Segment next = segments[index];
                index++;
                return next;
            }
        }
    }

    private class Segment {
        Item top;
        Item bottom;
        double slope;
        int size;

        Segment(int index, double slope) {
            Item item = new Item();
            item.index = index;
            item.next = null;
            top = item;
            bottom = item;
            this.slope = slope;
            this.size = 1;
        }

        LineSegment lineSegment() {
            int i;
            int index = 0;
            Point cur;
            if (size < 4) {
                throw new IllegalArgumentException("bool shit");
            } else {
                Point[] p = new Point[size];
                p[0] = points[top.index];
                Item item = top.next;
                while (item != null) {
                    i = index;
                    cur = points[item.index];
                    while (i >= 0 && p[i].compareTo(cur) > 0) {
                        p[i + 1] = p[i];
                        i--;
                    }
                    p[i + 1] = cur;
                    index++;
                    item = item.next;
                }
                return new LineSegment(p[0], p[size - 1]);
            }
        }

        void add(int index) {
            Item item = new Item();
            item.index = index;
            item.next = null;
            if (top == bottom) {
                top.next = item;
                bottom = item;
            } else {
                bottom.next = item;
                bottom = item;
            }
            size++;
        }

        int size() {
            return size;
        }

        void print() {
            System.out.print("slope=" + slope + "; ");
            Item i = top;
            while (i != null) {
                System.out.print(i.index + " ");
                i = i.next;
            }
            System.out.println();
        }
    }

    private class LineSegmentPool {
        LSItem top;
        LSItem bottom;
        int size = 0;

        LineSegmentPool() {
            top = null;
            bottom = null;
        }

        void add(LineSegment ls) {
            // check hash
            int hash = getHash(ls.toString());
            LSItem lsItem = top;
            while (lsItem != null) {
                if (lsItem.hash == hash) return;
                lsItem = lsItem.next;
            }


            LSItem item = new LSItem();
            item.ls = ls;
            item.hash = hash;
            item.next = null;
            if (top == null) {
                top = item;
                bottom = item;
            } else if (top == bottom) {
                top.next = item;
                bottom = item;
            } else {
                bottom.next = item;
                bottom = item;
            }
            size++;
        }

        LineSegment[] asArray() {
            LineSegment[] ls = new LineSegment[size];
            int i = 0;
            LSItem lsItem = top;
            while (lsItem != null) {
                ls[i++] = lsItem.ls;
                lsItem = lsItem.next;
            }
            return ls;
        }

        void print() {
            System.out.println("-----line segment:----");
            LSItem i = top;
            while (i != null) {
                System.out.println(i.ls);
                i = i.next;
            }
            System.out.println("--------------------");
        }
    }

    private class Item {
        int index;
        Item next;
    }

    private class LSItem {
        LineSegment ls;
        int hash;
        LSItem next;
    }

    private static int getHash(String s) {
        int hash = 7;
        for (int i = 0; i < s.length(); i++) {
            hash = hash * 31 + s.charAt(i);
        }
        return hash;
    }

    public static void main(String[] args) {
//        FastCollinearPoints outerObject = new FastCollinearPoints(new Point[0]);
//        Segment b = outerObject.new Segment(1, 0);
//        b.add(2);
//        b.add(3);
//        b.add(4);
//        b.print();
//        System.out.println("size: " + b.size());

//        FastCollinearPoints outerObject = new FastCollinearPoints(new Point[0]);
//        SegmentPool pool = outerObject.test(10);
//        pool.addForSlope(45, 1);
//        pool.addForSlope(5, 2);
//        pool.addForSlope(45, 3);
//        pool.addForSlope(5, 4);
//        pool.addForSlope(5, 5);
//        pool.addForSlope(45, 6);
//        pool.print();
//
//        Point[] points = new Point[12];
//        points[0] = new Point(0, 0);
//        points[1] = new Point(2, 2);
//        points[2] = new Point(5, 5);
//        points[3] = new Point(3, 1);
//        points[4] = new Point(6, 2);
//        points[5] = new Point(9, 3);
//        points[6] = new Point(7, -1);
//        points[7] = new Point(4, 8);
//        points[8] = new Point(0, -2);
//        points[9] = new Point(3, -1);
//        points[10] = new Point(6, 0);
//        points[11] = new Point(9, 1);
//
//        FastCollinearPoints f = new FastCollinearPoints(points);
//        f.segments();
//        String fileName = "collinear/equidistant.txt";
//        int n = 0;
//        try {
//            File file = new File(fileName);
//            FileReader fileReader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line;
//            if ((line = bufferedReader.readLine()) != null) {
//                n = Integer.parseInt(line);
//            }
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Point[] points = new Point[n];
//        try {
//            File file = new File(fileName);
//            FileReader fileReader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line;
//            String[] split;
//            int i = 0;
//            while ((line = bufferedReader.readLine()) != null) {
//                split = line.split("\\s+");
//                if (split.length > 1) {
//                    points[i++] = new Point(Integer.parseInt(split[0]),Integer.parseInt(split[1]));
//                }
//            }
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (Point p:points) {
//            System.out.println(p);
//        }
//        FastCollinearPoints f = new FastCollinearPoints(points);
//        LineSegment[] ls = f.segments();
//        for (LineSegment l : ls) {
//            System.out.println(l);
//        }
    }
}
