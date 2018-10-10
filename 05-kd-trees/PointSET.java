import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> ts;
    private Draw draw;

    // construct an empty set of points
    public PointSET() {
        this.ts = new TreeSet<>();
        this.draw = new Draw();
    }

    public boolean isEmpty() {
        return ts.isEmpty();
    }

    // number of points in the set
    public int size() {
        return ts.size();
    }

    // number of points in the set
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        ts.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return ts.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : ts) {
            draw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        TreeSet<Point2D> range = new TreeSet<>();
        for (Point2D p : ts) {
            if (rect.contains(p))
                range.add(p);
        }
        return range;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D nearest = ts.first();
        double minDist = point.distanceTo(nearest);
        for (Point2D p : ts) {
            if (p.distanceTo(p) < minDist) {
                nearest = p;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        Point2D p1 = new Point2D(0.0, 0.0);
        Point2D p2 = new Point2D(0.1, 0.0);
        Point2D p3 = new Point2D(0.2, 0.0);
        Point2D p4 = new Point2D(0.3, 0.0);
        double x = 0, y = 0;
        for (float i = 0; i < 10; i++) {
            x = 0.0 + (i / 10);
            y = 0.0 + (i / 10);
            System.out.println(x);
            ps.insert(new Point2D(x, y));
        }
        ps.draw();
    }
}