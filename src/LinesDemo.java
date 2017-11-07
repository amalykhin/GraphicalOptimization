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

    class Canvas extends JPanel {
        Dimension dim;
        //Gonna do a list for all the lines except the vector line.
        Line vec,l2;
        Point mouse = null;
        Point p;
        int dist;
        Point origin;
        //Gonna do a list of intersection points.
        Point intersections;

        Canvas (int width, int height) {
            dim = new Dimension();
            dim.setSize(width, height);
            setPreferredSize(dim);

            p = new Point(0,0);
            vec = new Line(new Point(0, 0), new Point(0, 1));
            l2 = new Line(new Point(0, 0), new Point(1, 1));
            origin = new Point(dim.width/2,dim.height/2);

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent mouseEvent) {
                    p = mouseEvent.getPoint();
                    p.x -= origin.x;
                    p.y = -p.y+origin.y;
                    dist = (int)vec.distance(p);


                    repaint();

                    if (mouse == null)
                        return;
                    intersections = vec.getIntersection(l2);

                    vec.move(mouseEvent.getX()-mouse.x,-mouseEvent.getY()+mouse.y);
                    mouse = mouseEvent.getPoint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    mouse = mouseEvent.getPoint();

                    p = (Point)mouse.clone();
                    p.x -= origin.x;
                    p.y = -p.y+origin.y;
                    System.out.println(mouse);

                //    intersections = vec.getIntersection(l2);

                    dist = (int) vec.distance(p);
                    if (vec.distance(p) > 10) {
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
            Graphics2D g = (Graphics2D)graphics;
            g.translate(origin.x, origin.y);
            g.scale(1,-1);

            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, origin.y, 0, -origin.y);
            g.drawLine(-origin.x, 0, origin.x, 0);

            //Drawing an endless line.
            Point start, end;
            start = new Point();
            end = new Point();
            if (vec.isHorisontal() || vec.getX(origin.y) < -origin.x) {
                start.x = -origin.x;
                start.y = vec.getY(start.x);
            } else {
                //start.x = vec.getX(origin.y);
                start.y = origin.y;
                start.x = vec.getX(start.y);
            }
            if (vec.isHorisontal() || vec.getX(-origin.y) > origin.x) {
                end.x = origin.x;
                end.y = vec.getY(end.x);
            } else {
                end.y = -origin.y;
                end.x = vec.getX(end.y);
            }
            g.setColor(Color.BLACK);
            g.drawLine(start.x,start.y,end.x,end.y);

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
            g.drawLine(start.x,start.y,end.x,end.y);

            g.setColor(Color.RED);
            if (intersections != null)
                fillCircle(g, intersections, 3);
            //drawCircle(g, p, dist);
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
