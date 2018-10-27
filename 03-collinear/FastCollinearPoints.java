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

    }
}
