/**
 * Created by tigr on 11/6/17.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

public class LinesDemo extends JFrame {

    LinesDemo () {
        Canvas canvas = new Canvas(600, 600);
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
        final int POINT_SIZE = 3;
        final int FOCUS_DISTANCE = 5;

        final Line vector;
        List<Line> lines;
        List<LineSegment> segments;
        //Colinear lines.
        Line colinear;
        //Reference to the focused line. (It is supposed to be the vector.)
        Line focused;
        //Mouse position according to the origin in the top left corner.
        Point mousePos;
        //Distance between the focused line and the mouse cursor. (For debug purposes.)
        int distance;
        //List of all intersections between vector line and all the others.
        List<Point> intersections;

        Canvas (int width, int height) {
            super(width, height);
            lines = new ArrayList<>();
            intersections = new ArrayList<>();
            segments = new ArrayList<>();

           // mousePos = new Point(0,0);
            vector = new Line(0, 0, 0, 1);

            //lines.add(new Line(0, 0, 1, 1));
            //lines.add(new Line(0, 10, 1, 10));
            //lines.add(new Line(200, 0, 200, 1));
            //lines.add(new Line(0, 0, 1, 1));
            lines.add(vector);

            segments.add(new LineSegment(new Line(0,0,1,1),-50,-50,50,50));

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent mouseEvent) {
                    if (focused == null)
                        return;

                    Point intersection;
                    intersections.removeAll(intersections);
                    colinear = null;
                    List<Line> l = new ArrayList<>();
                    l.addAll(lines);
                    l.addAll(segments);
                    for (Line line : l) {
                        if (line == vector)
                            continue;
                        if ((intersection=line.getIntersection(vector)) != null)
                            intersections.add(intersection);
                        else if (vector.contains(line))
                            colinear = vector;
                    }

                    focused.move(mouseEvent.getX()-mousePos.x,-mouseEvent.getY()+mousePos.y);
                    mousePos = mouseEvent.getPoint();

                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    mousePos = mouseEvent.getPoint();

                    Point p = new Point();
                    p.x = mousePos.x - origin.x;
                    p.y = -mousePos.y+origin.y;
                    System.out.println(p);

                    distance = FOCUS_DISTANCE + 1;
                    int new_dist;
                    /*
                    for (Line line : lines) {
                        new_dist = (int)line.distance(p);

                        //Debug information.
                        System.out.println(line + ". Distance: " + new_dist);

                        if (distance > new_dist) {
                            distance = new_dist;
                            focused = line;
                        }
                    }
                    */
                    if ((new_dist=(int)vector.distance(p)) < distance) {
                        distance = new_dist;
                        focused = vector;
                    } else
                        focused = null;
                    System.out.println(vector + ". Distance: " + new_dist);

                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
            //        focused = null;
             //       repaint();
                }
            });

        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            //Antaliasing.
            //Graphics2D g2d = (Graphics2D)graphics;
            //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Drawing the endless lines.
            Point start, end;
            start = new Point();
            end = new Point();
            graphics.setColor(Color.BLACK);
            for (Line line : lines) {
                if (line.isHorisontal() || line.getX(origin.y) < -origin.x) {
                    start.x = -origin.x;
                    start.y = line.getY(start.x);
                } else {
                    start.y = origin.y;
                    start.x = line.getX(start.y);
                }
                if (line.isHorisontal() || line.getX(-origin.y) > origin.x) {
                    end.x = origin.x;
                    end.y = line.getY(end.x);
                } else {
                    end.y = -origin.y;
                    end.x = line.getX(end.y);
                }
                if (line == focused) {
                    graphics.setColor(Color.BLUE);
                    graphics.drawLine(start.x, start.y, end.x, end.y);
                    graphics.setColor(Color.BLACK);
                } else if (line == colinear) {
                    graphics.setColor(Color.RED);
                    graphics.drawLine(start.x, start.y, end.x, end.y);
                    graphics.setColor(Color.BLACK);
                } else
                    graphics.drawLine(start.x, start.y, end.x, end.y);
            }

            for (LineSegment segment: segments) {
                graphics.drawLine(segment.a.x, segment.a.y, segment.b.x, segment.b.y);
            }

            //Draw line intersections.
            graphics.setColor(Color.RED);
            for (Point point : intersections)
                if (intersections != null)
                    fillCircle(graphics, point, POINT_SIZE);
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
