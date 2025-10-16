package sergisa.drawer;

import java.awt.*;

public class Crosshair {
    int x;
    int y;
    int pad = 6;
    Color color = new Color(0x96ADD6);
    boolean visible = false;

    public Crosshair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void paint(Graphics g) {
        Rectangle contextRectangle = g.getClipBounds();
        g.setColor(color);
        g.drawLine(x, 0, x, y - pad);
        g.drawLine(x, y + pad, x, contextRectangle.height);

        g.drawLine(0, y, x - pad, y);
        g.drawLine(x + pad, y, contextRectangle.width, y);
    }


    public int getX() {
        return x;
    }

    public Crosshair setXY(int x, int y) {
        setX(x);
        setY(y);
        return this;
    }

    public Crosshair setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Crosshair setY(int y) {
        this.y = y;
        return this;
    }

    public int getPad() {
        return pad;
    }

    public Crosshair setPad(int pad) {
        this.pad = pad;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public Crosshair setColor(Color color) {
        this.color = color;
        return this;
    }

    public Crosshair setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}
