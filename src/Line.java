import java.awt.Point;

/**
 * Created by tigr on 11/6/17.
 */
public class Line {
    double A, B, C;
    //Point start, end;

    Line (double startX, double startY, double endX, double endY) {
     /*   this.start = start;
        this.end = end;
        */
        A = endY - startY;
        B = startX - endX;
        C = startY*endX - endY*startX;
    }

    Line (double A, double B, double C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    Line (Line l) {
        A = l.A;
        B = l.B;
        C = l.C;
    }

    /*Line (int startX, int startY, int endX, int endY) {
        start.x = startX;
        start.y = startY;
        end.x = endX;
        end.y = endY;
    }*/

    boolean contains (Point.Double p) {
        return (A*p.x + B*p.y + C) == 0;
    }
    boolean contains (Line l) {
         //return isParallel(l) && (l.C==C);
        boolean aux = isColinear(l) && (Math.abs(l.C/C - l.A/A) < 1.e-6);
        return aux;
    }

    Point.Double getIntersection (Line l) {
        Point.Double res = new Point.Double();

        if (isColinear(l))
            return null;
        else if (l.isHorisontal())
            return new Point.Double(getX(-l.C/l.B),-l.C/l.B);
        else if (isHorisontal())
            return new Point.Double(l.getX(-C/B),-C/B);
        else if (l.isVertical())
            return new Point.Double(-l.C/l.A,getY(-l.C/l.A));
        else if (isVertical())
            return new Point.Double(-C/A,l.getY(C/A));

        res.y = ((l.A*C - A*l.C)/(A*l.B - l.A*B));
        res.x = ((-B*res.y - C)/A);

        return res;
    }

    void move (double deltaX, double deltaY) {
        C -= (deltaX*A + deltaY*B);
    }

    void move (Point delta) {
       move(delta.x, delta.y);
    }

    double getX (double y) {
        return ((-C-B*y)/A);
    }

    double getY (double x) {
        return ((-C-A*x)/B);
    }

    double distance (Point.Double p) {
        return Math.abs(A*p.x+B*p.y+C)/Math.sqrt(Math.pow(A,2)+Math.pow(B,2));
    }

    boolean isHorisontal () {
        return (A == 0);
    }

    boolean isVertical () {
        return (B == 0);
    }

    boolean isColinear (Line l) {
        return (l.isHorisontal()&&isHorisontal()) || (l.isVertical()&&isVertical()) || (l.A/A == l.B/B);
    }

    boolean isParallel (Line l) {
        return (l.A==A) && (l.B==B);
    }
}
