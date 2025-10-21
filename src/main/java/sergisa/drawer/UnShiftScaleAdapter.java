package sergisa.drawer;

import java.awt.*;

public class UnShiftScaleAdapter {
    double scale;
    double shiftY;
    double shiftX;

    public UnShiftScaleAdapter(double scale, double shiftY, double shiftX) {
        this.scale = scale;
        this.shiftY = shiftY;
        this.shiftX = shiftX;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setShiftY(double shiftY) {
        this.shiftY = shiftY;
    }

    public void setShiftX(double shiftX) {
        this.shiftX = shiftX;
    }

    public Point adaptPoint(Point point) {
        return new Point(
                (int) ((point.getX() - shiftX) / scale),
                (int) ((point.getY() - shiftY) / scale)
        );
    }
}
