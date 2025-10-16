package sergisa.drawer.geometry;

public class AnchorPlacementAdapter extends PlacementAdapter {
    PlacementAnchor placementAnchor;

    public AnchorPlacementAdapter(PlacementAnchor placementAnchor, GraphicalNode currentPanel) {
        super(0, 0, currentPanel);
        switch (placementAnchor) {
            case LEFT_CENTER -> dy = -currentPanel.getHeight() / 2;
            case LEFT_BOTTOM -> dy = -currentPanel.getHeight();
            case CENTER_TOP -> dx = -currentPanel.getWidth() / 2;
            case CENTER_CENTER -> {
                dy = -currentPanel.getHeight() / 2;
                dx = -currentPanel.getWidth() / 2;
            }
            case CENTER_BOTTOM -> {
                dx = -currentPanel.getWidth() / 2;
                dy = -currentPanel.getHeight();
            }
            case RIGHT_TOP -> dx = -currentPanel.getWidth();
            case RIGHT_CENTER -> {
                dy = -currentPanel.getHeight() / 2;
                dx = -currentPanel.getWidth();
            }
            case RIGHT_BOTTOM -> {
                dx = -currentPanel.getWidth();
                dy = -currentPanel.getHeight();
            }
            case LEFT_TOP -> {
                dx = 0;
                dy = 0;
            }
        }
        this.placementAnchor = placementAnchor;
    }
}
