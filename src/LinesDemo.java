/**
 * Created by tigr on 11/6/17.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        double distance;
        //List of all intersections between vector line and all the others.
        Set<Point.Double> intersections;
        double scaleFactor;

        void drawCircle(Graphics g, Point.Double p, int r) {
            g.drawOval((int)(p.x*scaleFactor-r),(int)(p.y*scaleFactor-r),(int)2*r,(int)2*r);
        }

        void fillCircle(Graphics g, Point.Double p, int r) {
            g.fillOval((int)(p.x*scaleFactor-r),(int)(p.y*scaleFactor-r),(int)2*r,(int)2*r);
        }

        Canvas (int width, int height) {
            super(width, height);
            lines = new ArrayList<>();
            intersections = new HashSet<>();
            segments = new ArrayList<>();

            scaleFactor = 40;

           // mousePos = new Point(0,0);

            vector = new Line(1,2,0);
            //lines.add(new Line(0, 0, 1, 1));
            //lines.add(new Line(0, 10, 1, 10));
            //lines.add(new Line(200, 0, 200, 1));
            //lines.add(new Line(0, 0, 1, 1));
            lines.add(vector);
            lines.add(new Line(0,1,0));
            lines.add(new Line(1,0,0));
            //lines.add(new Line(4,-2,-10));
            //lines.add(new Line(-1,2,-6));
            //lines.add(new Line(2,4,-8));
            //segments.add(new LineSegment(new Line(4,-2,-10),-50,-50,50,50));
            //segments.add(new LineSegment(new Line(-1,2,-6),-50,-50,50,50));
            //segments.add(new LineSegment(new Line(2,4,-8),-50,-50,50,50));

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent mouseEvent) {
                    if (focused == null)
                        return;

                    focused.move((mouseEvent.getX()-mousePos.x)/scaleFactor,(-mouseEvent.getY()+mousePos.y)/scaleFactor);
                    mousePos = mouseEvent.getPoint();

                    Point.Double intersection;
                    intersections.removeAll(intersections);
                    colinear = null;
                    List<Line> l = new ArrayList<>();
                    l.addAll(lines);
                    l.addAll(segments);
                    for (Line line : l) {
                        if (line == vector)
                            continue;
                        if ((intersection=line.getIntersection(focused)) != null)
                            intersections.add(new Point.Double((int)(intersection.x*100)/100.0, (int)(intersection.y*100)/100.0));
                        else if (vector.contains(line))
                            colinear = line;
                    }

                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    mousePos = mouseEvent.getPoint();

                    Point.Double p = new Point.Double();
                    p.x = mousePos.x- origin.x;
                    p.y = -mousePos.y+origin.y;
                    p.x /= scaleFactor;
                    p.y /=scaleFactor;
                    System.out.println(p);

                    distance = (FOCUS_DISTANCE + 1)/scaleFactor;
                    double new_dist;
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
                    System.out.println("Max dist: "+distance);
                    if ((new_dist=vector.distance(p)) < distance) {
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
            Point.Double start, end;
            start = new Point.Double();
            end = new Point.Double();
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
                    graphics.drawLine((int)(start.x*scaleFactor), (int)(start.y*scaleFactor), (int)(end.x*scaleFactor), (int)(end.y*scaleFactor));
                    graphics.setColor(Color.BLACK);

                } else if (line == colinear) {
                    graphics.setColor(Color.RED);
                    graphics.drawLine((int)(start.x*scaleFactor), (int)(start.y*scaleFactor), (int)(end.x*scaleFactor), (int)(end.y*scaleFactor));
                    graphics.setColor(Color.BLACK);
                } else
                    graphics.drawLine((int)(start.x*scaleFactor), (int)(start.y*scaleFactor), (int)(end.x*scaleFactor), (int)(end.y*scaleFactor));
            }

            for (LineSegment segment: segments) {
                graphics.drawLine((int)(segment.a.x*scaleFactor), (int)(segment.a.y*scaleFactor), (int)(segment.b.x*scaleFactor), (int)(segment.b.y*scaleFactor));
            }

            //Draw line intersections.
            graphics.setColor(Color.RED);
            for (Point.Double point : intersections)
                if (intersections != null) {
                    fillCircle(graphics, point, POINT_SIZE);

                    ((Graphics2D)graphics).scale(1,-1);
                    DecimalFormat df = new DecimalFormat("#0.##");
                    graphics.drawString("("+df.format(point.x)+" "+df.format(point.y)+")", (int)Math.round(point.x*scaleFactor+10), (int)Math.round(-point.y*scaleFactor));
                    ((Graphics2D)graphics).scale(1,-1);
                }
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
