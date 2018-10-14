import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> ts;

    // construct an empty set of points
    public PointSET() {
        this.ts = new TreeSet<>();
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
        Draw draw = new Draw();
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
        double dist, minDist = point.distanceSquaredTo(nearest);
        for (Point2D p : ts) {
            dist = p.distanceSquaredTo(point);
            if (dist < minDist) {
                minDist = dist;
                nearest = p;
            }
        }
        return nearest;
    }
}