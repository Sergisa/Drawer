package sergisa.drawer.geometry;

public class ShiftedPlacementAdapter extends GraphicalNode {
    int dx, dy;
    GraphicalNode rect;

    public ShiftedPlacementAdapter(GraphicalNode node, int dx, int dy) {
        super(node.getX(), node.getY(), node.width, node.height);
        rect = node;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void setX(int x) {
        super.setX(x + dx);
    }

    @Override
    public void setY(int y) {
        super.setY(y + dy);
    }
}
