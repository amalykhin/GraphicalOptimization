/**
 * Created by tigr on 11/6/17.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

public class LinesDemo extends JFrame {

    LinesDemo () {
        Canvas canvas = new Canvas(300, 300);
        canvas.setBackground(Color.WHITE);
        setContentPane(canvas);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Line Demo");
        setVisible(true);
    }

    void drawCircle(Graphics g, Point p, int r) {
        g.drawOval(p.x-r,p.y-r,2*r,2*r);
    }

    void fillCircle(Graphics g, Point p, int r) {
        g.fillOval(p.x-r,p.y-r,2*r,2*r);
    }

    class Canvas extends CartesianCanvas {
        Line vector;
        List<Line> lines;
        //Reference to the focused line. (It is supposed to be the vector.)
        Line focused;
        Point mousePos;
        //Distance between the focused line and the mouse cursor.
        int distance;
        //List of all intersections between vector line and all the others.
        List<Point> intersections;

        Canvas (int width, int height) {
            super(width, height);

            mousePos = new Point(0,0);
            vector = new Line(0, 0, 0, 1);
            //l2 = new Line(new Point(0, 0), new Point(1, 1));
            lines.add(new Line(0, 0, 1, 1));

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent mouseEvent) {
                    mousePos = mouseEvent.getPoint();
                    //Transforming the mouse cursor coordinates according to the current coordinate system.
                    mousePos.x -= origin.x;
                    mousePos.y = -mousePos.y+origin.y;

                    distance = (int)vector.distance(mousePos);


                    repaint();

                    if (focused == null)
                        return;
                    intersections = vector.getIntersection(l2);

                    vector.move(mouseEvent.getX()-mouse.x,-mouseEvent.getY()+mouse.y);
                    mouse = mouseEvent.getPoint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    mouse = mouseEvent.getPoint();

                    p = (Point)mouse.clone();
                    mousePos.x -= origin.x;
                    mousePos.y = -mousePos.y+origin.y;
                    System.out.println(mouse);

                //    intersections = vector.getIntersection(l2);

                    distance = (int) vector.distanceance(p);
                    if (vector.distanceance(p) > 10) {
                        mouse = null;
                    }

                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                    mouse = null;
                }
            });

        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            graphics.setColor(Color.LIGHT_GRAY);
            graphics.drawLine(0, origin.y, 0, -origin.y);
            graphics.drawLine(-origin.x, 0, origin.x, 0);

            //Drawing an endless line.
            Point start, end;
            start = new Point();
            end = new Point();
            if (vector.isHorisontal() || vector.getX(origin.y) < -origin.x) {
                start.x = -origin.x;
                start.y = vector.getY(start.x);
            } else {
                //start.x = vector.getX(origin.y);
                start.y = origin.y;
                start.x = vector.getX(start.y);
            }
            if (vector.isHorisontal() || vector.getX(-origin.y) > origin.x) {
                end.x = origin.x;
                end.y = vector.getY(end.x);
            } else {
                end.y = -origin.y;
                end.x = vector.getX(end.y);
            }
            graphics.setColor(Color.BLACK);
            graphics.drawLine(start.x,start.y,end.x,end.y);

            if (l2.isHorisontal() || l2.getX(origin.y) < -origin.x) {
                start.x = -origin.x;
                start.y = l2.getY(start.x);
            } else {
                start.y = origin.y;
                start.x = l2.getX(start.y);
            }
            if (l2.isHorisontal() || l2.getX(-origin.y) > origin.x) {
                end.x = origin.x;
                end.y = l2.getY(end.x);
            } else {
                end.y = -origin.y;
                end.x = l2.getX(end.y);
            }
            graphics.drawLine(start.x,start.y,end.x,end.y);

            graphics.setColor(Color.RED);
            if (intersections != null)
                fillCircle(g, intersections, 3);
            //drawCircle(g, p, distance);
        }

    }

    public static void main (String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                new LinesDemo();
            }
        });
    }
}
