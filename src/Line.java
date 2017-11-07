import java.awt.Point;

/**
 * Created by tigr on 11/6/17.
 */
public class Line {
    double A, B, C;
    //Point start, end;

    Line (Point start, Point end) {
     /*   this.start = start;
        this.end = end;
        */
        A = end.y - start.y;
        B = start.x - end.x;
        C = start.y*end.x - end.y*start.x;
    }

    Line (double A, double B, double C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    /*Line (int startX, int startY, int endX, int endY) {
        start.x = startX;
        start.y = startY;
        end.x = endX;
        end.y = endY;
    }*/

    boolean contains (Point p) {
        return (A*p.x + B*p.y + C) == 0;
    }

    Point getIntersection (Line l) {
        Point res = new Point();

        res.y = (int)((l.A*C - A*l.C)/(A*l.B - l.A*B));
        res.x = (int)((-B*res.y - C)/A);

        return res;
    }

    void move (int deltaX, int deltaY) {
        C -= (deltaX*A + deltaY*B);
    }

    void move (Point delta) {
       move(delta.x, delta.y);
    }

    int getX (int y) {
        return (int)((-C-B*y)/A);
    }

    int getY (int x) {
        return (int)((-C-A*x)/B);
    }

    double distance (Point p) {
        return Math.abs(A*p.x+B*p.y+C)/Math.sqrt(Math.pow(A,2)+Math.pow(B,2));
    }

    boolean isHorisontal () {
        return (A == 0);
    }

    boolean isVertical () {
        return (B == 0);
    }
}
