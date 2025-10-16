package sergisa.drawer.geometry;

import java.awt.*;

public class PlacementAdapter extends GraphicalNode {
    int dx;
    int dy;
    GraphicalNode currentPanel;

    public PlacementAdapter(int dx, int dy, GraphicalNode currentPanel) {
        this.dx = dx;
        this.dy = dy;
        this.currentPanel = currentPanel;
    }

    public PlacementAdapter(GraphicalNode currentPanel) {
        this(0, 0, currentPanel);
    }

    public void setLocation(Point p) {
        currentPanel.setLocation(p.x - dx, p.y - dy);
    }

    @Override
    public void setX(int x) {
        currentPanel.setX(x - dx);
    }

    @Override
    public void setY(int y) {
        currentPanel.setY(y - dy);
    }

    @Override
    public int getX() {
        return super.getX() + dx;
    }

    @Override
    public int getY() {
        return super.getY() + dy;
    }

    @Override
    public Point getLocation() {
        Point originalLocation = currentPanel.getLocation();
        originalLocation.x += dx;
        originalLocation.y += dy;
        return originalLocation;
    }
}
