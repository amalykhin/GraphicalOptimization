import java.awt.*;

/**
 * Created by tigr on 11/9/17.
 */
public class LineSegment extends Line {
    //Ends of the segment.
    Point.Double a, b;

    LineSegment (double A, double B, double C, Point.Double a, Point.Double b) {
        super(A, B, C);

        this.a = a;
        this.b = b;
    }

    LineSegment (double A, double B, double C, double x1, double y1, double x2, double y2) {
        this(A, B, C, new Point.Double(x1, y1), new Point.Double(x2, y2));
    }

    LineSegment (Line l, double x1, double y1, double x2, double y2) {
        this(l.A, l.B, l.C, x1, y1, x2, y2);
    }

    LineSegment (Line l, Point.Double a, Point.Double b) {
        this(l.A, l.B, l.C, a, b);
    }

    Point.Double getIntersection (LineSegment seg) {
        Point.Double inter = super.getIntersection(seg);

        if (contains(inter) && seg.contains(inter))
            return inter;

        return null;
    }

    @Override
    Point.Double getIntersection (Line line) {
        Point.Double inter = super.getIntersection(line);

        if (contains(inter))
            return inter;

        return null;
    }

    @Override
    boolean contains (Point.Double p) {
        if (!super.contains(p))
            return false;
        if ((a.x <= p.x) && (p.x <= b.x))
            if ((a.y <= p.y) && (p.y <= b.y) || (b.y <= p.y) && (p.y <= a.y))
                return true;
        if ((b.x <= p.x) && (p.x <= a.x))
        if ((a.y <= p.y) && (p.y <= b.y) || (b.y <= p.y) && (p.y <= a.y))
            return true;

        return false;
    }

}
