import edu.princeton.cs.algs4.Draw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private class Node {
        double key;
        Point2D point;
        Node left, right;

        Node(double key, Point2D point) {
            this.key = key;
            this.point = point;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    // construct an empty set of points
    public KdTree() {
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    // number of points in the set
    public int size() {
        return size(this.root);
    }

    private int size(Node root) {
        if (root == null)
            return 0;
        return size(root.left) + size(root.right) + 1;
    }

    // number of points in the set
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        this.root = put(root, p, true);
    }

    private Node put(Node root, Point2D point, boolean useX) {
        double key;
        if (useX)
            key = point.x();
        else
            key = point.y();

        if (root == null)
            return new Node(key, point);

        int cmp = Double.compare(key, root.key);
        if (cmp < 0)
            root.left = put(root.left, point, !useX);
        else if (cmp > 0)
            root.right = put(root.right, point, !useX);
        else
            root.point = point;
        return root;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        Node current = root;
        boolean useX = true;
        int cmp;
        while (current != null)
        {
            if (useX)
                cmp = Double.compare(p.x(), current.point.x());
            else
                cmp = Double.compare(p.y(), current.point.y());

            useX = !useX;

            if (cmp < 0)
                current = current.left;
            else if (cmp > 0)
                current = current.right;
            else
                return p.compareTo(current.point) == 0;
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        Draw draw = new Draw();
        draw.setPenColor(Draw.GRAY);
        for (double i = 0.1; i < 1.0; i += 0.1) {
            draw.line(0, i, 1, i);
            draw.line(i, 0, i, 1);
        }
        Node current = root;
        draw(current, true, 0, 1, draw);
    }

    private void draw(Node cur, boolean useX, double leftLimit, double rightLimit, Draw draw) {
        if (cur == null) return;
        if (useX) {
            draw.setPenColor(Draw.RED);
            draw.line(cur.point.x(), leftLimit, cur.point.x(), rightLimit);
        }
        else {
            draw.setPenColor(Draw.BLUE);
            draw.line(leftLimit, cur.point.y(), rightLimit, cur.point.y());
        }
        draw.setPenColor(Draw.BLACK);
        draw.filledCircle(cur.point.x(), cur.point.y(), 0.003);

        draw(cur.left, !useX, 0, cur.key, draw);
        draw(cur.right, !useX, cur.key, 1, draw);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> res = new ArrayList<>(); //todo: queue
        findPoints(root, new RectHV(0, 0, 1, 1), rect, true, res);
        return res;
    }

    private void findPoints(Node root, RectHV nodeRect, RectHV rect, boolean useX, List<Point2D> res) {
        if (root == null)
            return;

        if (!rect.intersects(nodeRect))
            return;

        Point2D point = root.point;
        if (rect.contains(point))
            res.add(point);

        RectHV leftRect, rightRect;
        if (useX) {
            leftRect = new RectHV(0, 0, point.x(), 1);
            rightRect = new RectHV(point.x(), 0, 1, 1);
        } else {
            leftRect = new RectHV(0, 0, 1, point.y());
            rightRect = new RectHV(0, point.y(), 1, 1);
        }
        findPoints(root.left, leftRect, rect, !useX, res);
        findPoints(root.right, rightRect, rect, !useX, res);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        findNearest(root, new RectHV(0, 0, 1, 1), p, true);
        return nearest;
    }

    private double minDist = Double.MAX_VALUE;
    private Point2D nearest = null;

    private Point2D findNearest(Node root, RectHV rect, Point2D destination, boolean useX) {
        if (root == null) return null;

        if (rect.distanceSquaredTo(destination) > minDist)
            return nearest;

        Point2D point = root.point;
        double dist = point.distanceSquaredTo(destination);
        if (dist < minDist) {
            minDist = dist;
            nearest = point;
        }
//        System.out.println(point + " " + dist + "\t\tmin=" + nearest);

        RectHV leftRect, rightRect;
        if (useX) {
            leftRect = new RectHV(0, 0, point.x(), 1);
            rightRect = new RectHV(point.x(), 0, 1, 1);
        } else {
            leftRect = new RectHV(0, 0, 1, point.y());
            rightRect = new RectHV(0, point.y(), 1, 1);
        }

        double keyCoord;
        if (useX)
            keyCoord = destination.x();
        else
            keyCoord = destination.y();

        if (root.key > keyCoord) {
            findNearest(root.left, leftRect, destination, !useX);
            findNearest(root.right, rightRect, destination, !useX);
        } else {
            findNearest(root.right, rightRect, destination, !useX);
            findNearest(root.left, leftRect, destination, !useX);
        }

        return nearest;
    }
}
