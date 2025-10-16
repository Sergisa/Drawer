package sergisa.drawer.geometry;

public enum PlacementAnchor {
    LEFT_TOP(1, 1),
    CENTER_TOP(2, 1),
    RIGHT_TOP(3, 1),
    LEFT_CENTER(1, 2),
    CENTER_CENTER(2, 2),
    RIGHT_CENTER(3, 2),
    LEFT_BOTTOM(1, 3),
    CENTER_BOTTOM(2, 3),
    RIGHT_BOTTOM(3, 3);

    private final int i;
    private final int j;

    PlacementAnchor(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
