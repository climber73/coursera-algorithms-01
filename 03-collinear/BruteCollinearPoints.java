public class BruteCollinearPoints {
    private Point[] points;
    private int index;
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
//        if (points.length < 4) throw new IllegalArgumentException("less than 4 points");
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            if (p == null) throw new IllegalArgumentException("one of the points is null");
            for (int j = i + 1; j < points.length; j++) {
                Point r = points[j];
                if (p.compareTo(r) == 0) throw new IllegalArgumentException("repetitions");
            }
        }
        this.points = points.clone();
    }

    // the number of line segments
    public int numberOfSegments() {
        segments();
        return index;
    }

    // the line segments
    public LineSegment[] segments() {
        if (this.segments != null) return this.segments;

        LineSegment[] segments = new LineSegment[points.length / 4 + 1];
        index = 0;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        Point p = points[i];
                        Point r = points[j];
                        Point q = points[k];
                        Point s = points[l];
                        if (p.slopeTo(r) == r.slopeTo(q) && r.slopeTo(q) == q.slopeTo(s)) {
                            segments[index] = segment(p, r, q, s);
                            index++;
                        }
                    }
                }
            }
        }
        LineSegment[] result = new LineSegment[index];
        for (int i = 0; i < index; i++) {
            result[i] = segments[i];
        }
        this.segments = result;
        return result;
    }

    private LineSegment segment(Point p, Point r, Point q, Point s) {
        return new LineSegment(uppermost(p, r, q, s), undermost(p, r, q, s));
    }

    private Point uppermost(Point p, Point r, Point q, Point s) {
        Point top = p;
        if (r.compareTo(top) > 0) top = r;
        if (q.compareTo(top) > 0) top = q;
        if (s.compareTo(top) > 0) top = s;
        return top;
    }
    private Point undermost(Point p, Point r, Point q, Point s) {
        Point bottom = p;
        if (r.compareTo(bottom) < 0) bottom = r;
        if (q.compareTo(bottom) < 0) bottom = q;
        if (s.compareTo(bottom) < 0) bottom = s;
        return bottom;
    }
}
