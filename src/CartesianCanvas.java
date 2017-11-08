import javax.swing.*;
import java.awt.*;

/**
 * Created by tigr on 11/7/17.
 */
public abstract class CartesianCanvas extends JPanel {
    Dimension size;
    Point origin;

    CartesianCanvas (Dimension size) {
        this.size = (Dimension)size.clone();
        setPreferredSize(size);
        origin = new Point(size.width/2,size.height/2);
    }

    CartesianCanvas (Dimension size, Point origin) {
        this(size);
        this.origin.x = origin.x;
        this.origin.y = origin.y;
    }

    CartesianCanvas (int width, int height) {
        this(new Dimension(width, height));
    }

    CartesianCanvas (int width, int height, int originX, int originY) {
        this(new Dimension(width, height));
        origin.x = originX;
        origin.y = originY;
    }

    @Override
    protected void paintComponent (Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D)graphics;
        g.translate(origin.x, origin.y);
        g.scale(1,-1);

        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawLine(0, origin.y, 0, -origin.y);
        graphics.drawLine(-origin.x, 0, origin.x, 0);
    }
}
