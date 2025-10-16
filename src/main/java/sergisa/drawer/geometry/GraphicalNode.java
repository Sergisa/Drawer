package sergisa.drawer.geometry;


import sergisa.drawer.Colors;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphicalNode {
    private int x;
    private int y;
    protected int width;
    protected int height;
    protected int arc;
    Color borderColor;
    Color textColor = Colors.DarkBlue;
    Color fillColor = Colors.Blue;
    List<GraphicalNode> childNodes;
    List<GraphicalNode> parentNodes;

    public GraphicalNode() {
    }

    public GraphicalNode(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        childNodes = new ArrayList<>();
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean contains(double x, double y) {
        return contains(new Point((int) x, (int) y));
    }

    public boolean contains(Point p) {
        double xmin = (getBounds().x);
        double ymin = (getBounds().y);
        double xmax = ((getBounds().x + getBounds().width)) - 1;
        double ymax = ((getBounds().y + getBounds().height)) - 1;
        return p.x >= xmin && p.x <= xmax && p.y >= ymin && p.y <= ymax;
    }

    public boolean intersects(GraphicalNode r) {
        return false;
    }

    public boolean contains(GraphicalNode r) {
        return false;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point getLocation() {
        return new Point(getX(), getY());
    }
}
