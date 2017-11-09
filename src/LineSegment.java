import java.awt.*;

/**
 * Created by tigr on 11/9/17.
 */
public class LineSegment extends Line {
    //Ends of the segment.
    Point a, b;

    LineSegment (double A, double B, double C, Point a, Point b) {
        super(A, B, C);

        this.a = a;
        this.b = b;
    }

    LineSegment (double A, double B, double C, int x1, int y1, int x2, int y2) {
        this(A, B, C, new Point(x1, y1), new Point(x2, y2));
    }

    LineSegment (Line l, int x1, int y1, int x2, int y2) {
        this(l.A, l.B, l.C, x1, y1, x2, y2);
    }

    LineSegment (Line l, Point a, Point b) {
        this(l.A, l.B, l.C, a, b);
    }

    Point getIntersection (LineSegment seg) {
        Point inter = super.getIntersection(seg);

        if (contains(inter) && seg.contains(inter))
            return inter;

        return null;
    }

    @Override
    Point getIntersection (Line line) {
        Point inter = super.getIntersection(line);

        if (contains(inter))
            return inter;

        return null;
    }

    @Override
    boolean contains (Point p) {
        if (!super.contains(p))
            return false;
        if ((a.x < p.x) && (p.x < b.x))
            if ((a.y < p.y) && (p.y < b.y) || (b.y < p.y) && (p.y < a.y))
                return true;
        if ((b.x < p.x) && (p.x < a.x))
            if ((a.y < p.y) && (p.y < b.y) || (b.y < p.y) && (p.y < a.y))
                return true;

        return false;
    }

}
