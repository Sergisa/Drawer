package sergisa.drawer;


import sergisa.drawer.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel {
    public boolean showCrosshair = false;
    public boolean showGrid = false;
    Crosshair cross;
    Color gridColor = Colors.pink;
    Color crosshairColor = Colors.Blue;
    Color outlineColor = Colors.DarkBlue;
    Color backgroundColor = Colors.Cream;
    Color objectColor = Colors.Blue;
    Settings settings = Settings.getInstance();
    List<RectangularShape> drawingObjects;
    Rectangle selectionRectanglePhantom;
    Shape focusedObject;
    private double scale = 1;
    private Point2D.Double coordinateShift;
    UnShiftScaleAdapter shiftScaleAdapter;

    public Canvas() {
        setBackground(backgroundColor);
        setFocusable(true);
        shiftScaleAdapter = new UnShiftScaleAdapter(1, 0, 0);
        new MyCanvasMouseAdapter(this, shiftScaleAdapter);
        coordinateShift = new Point2D.Double(0, 0);
        drawingObjects = new ArrayList<>();
        RoundRectangle2D.Double rectangle = new RoundRectangle2D.Double(50, 200, 50, 80, 9, 9);
        RoundRectangle2D.Double rectangle2 = new RoundRectangle2D.Double(200, 300, 50, 80, 9, 9);
        drawingObjects.add(rectangle);
        drawingObjects.add(rectangle2);

        updateSettingsParameters();
        setLayout(null);
        if (settings.getBoolean("cursor.show")) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else {
            setCursor(getToolkit().createCustomCursor(
                    new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                    new Point(),
                    null
            ));
        }
    }

    private void updateSettingsParameters() {
        showCrosshair = settings.getBoolean("crosshair.show");
        showGrid = settings.getBoolean("grid.show");
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        updateSettingsParameters();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        AffineTransform oldTransform = g2d.getTransform();
        if (showGrid) paintGrid(g);
        g.translate((int) coordinateShift.x, (int) coordinateShift.y);
        g2d.scale(scale, scale);

        for (Shape drawingObject : drawingObjects) {
            Shape oldClip = g2d.getClip();
            Rectangle clippingRect = drawingObject.getBounds();
            clippingRect.grow(5, 5);
            g2d.clipRect(clippingRect.x, clippingRect.y, clippingRect.width, clippingRect.height);

            g.setColor(objectColor);
            g2d.fill(drawingObject);
            g.setColor(Colors.Red);
            g2d.draw(drawingObject);

            g2d.setClip(oldClip);
        }
        g2d.setTransform(oldTransform);
        if (selectionRectanglePhantom != null) {
            paintSelection(g2d);
        }
        if (showCrosshair && cross != null) cross.paint(g);
    }

    private void paintSelection(Graphics2D g2d) {
        BasicStroke SELECTION_RECTANGLE_STROKE = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);

        Color SELECTION_RECTANGLE_LINE_COLOR = Colors.Blue;
        Color SELECTION_RECTANGLE_BACKGROUND = new Color(53, 53, 53, 15);
        g2d.setColor(SELECTION_RECTANGLE_BACKGROUND);
        g2d.fillRect(selectionRectanglePhantom.x, selectionRectanglePhantom.y, selectionRectanglePhantom.width, selectionRectanglePhantom.height);
        g2d.setColor(SELECTION_RECTANGLE_LINE_COLOR);
        g2d.setStroke(SELECTION_RECTANGLE_STROKE);
        g2d.draw(selectionRectanglePhantom);
    }

    public void paintGrid(Graphics g) {
        int step = settings.getInt("grid.step");
        g.setColor(gridColor);
        ((Graphics2D) g).setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{15f, 5f}, 5f));
        for (int i = 0; i < getWidth(); i += step) {
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += step) {
            g.drawLine(0, i, getWidth(), i);
        }
        ((Graphics2D) g).setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, null, 5f));
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent me) {
        if (showCrosshair) {
            if (cross == null) {
                cross = new Crosshair(me.getX(), me.getY());
            } else {
                cross.setXY(me.getX(), me.getY());
            }
            setShowCrosshair(true);
            repaint();
        }
        super.processMouseMotionEvent(me);
    }

    @Override
    synchronized protected void processMouseEvent(MouseEvent evt) {
        if (evt.getID() == MouseEvent.MOUSE_ENTERED) {
            this.grabFocus();
        }
        super.processMouseEvent(evt);
    }

    public void setShowCrosshair(boolean showCrosshair) {
        this.showCrosshair = showCrosshair;
        if (!showCrosshair) repaint();
    }

    public RectangularShape getNodeAtPosition(int x, int y) {
        for (RectangularShape drawingObject : drawingObjects) {
            if (drawingObject.getBounds().contains(x, y)) {
                return drawingObject;
            }
        }
        return null;
    }

    private void shiftShape(RectangularShape shape, int dx, int dy) {
        Rectangle oldBounds = shape.getBounds();
        oldBounds.translate(dx, dy);
        shape.setFrame(oldBounds);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        shiftScaleAdapter.setScale(scale);
    }

    public void setCoordinateShift(int x, int y) {
        coordinateShift.setLocation(x, y);
        shiftScaleAdapter.setShiftX(x);
        shiftScaleAdapter.setShiftY(y);
    }

    public Point2D.Double getCoordinateShift() {
        return coordinateShift;
    }


    public Rectangle getSelectionRectanglePhantom() {
        return selectionRectanglePhantom;
    }

    public void resetCoordinates() {
        setCoordinateShift(0, 0);
        repaint();
    }

    public void resetScale() {
        setScale(1);
        repaint();
    }

    public UnShiftScaleAdapter getShiftScaleAdapter() {
        return shiftScaleAdapter;
    }

    private class MyCanvasMouseAdapter extends CanvasMouseAdapter {

        public MyCanvasMouseAdapter(Canvas mainCanvas, UnShiftScaleAdapter adapter) {
            super(mainCanvas, adapter);
        }

        public void onNodeDragging(MouseEvent event, int dx, int dy) {
            //зацепляется только после смещения координат
            shiftShape(hittedElement, dx, dy);
            lastPressPoint = event.getPoint();
            repaint();
        }

        public void onStartedSelection(MouseEvent event) {
            selectionRectanglePhantom = new Rectangle(event.getPoint());
        }

        public void onExtendedSelection(MouseEvent event) {
            int x0, y0, x1, y1;
            x0 = Math.min(lastPressPoint.x, event.getPoint().x);
            y0 = Math.min(lastPressPoint.y, event.getPoint().y);
            x1 = Math.max(lastPressPoint.x, event.getPoint().x);
            y1 = Math.max(lastPressPoint.y, event.getPoint().y);
            selectionRectanglePhantom.setBounds(x0, y0, x1 - x0, y1 - y0);
            repaint();
        }

        public void onEndSelection(Rectangle selectionRectangle) {
            System.out.println("Selection Ended");
            selectionRectanglePhantom = null;
            repaint();
        }

        @Override
        public void onPaneScrolling(int dx, int dy) {
            setCoordinateShift(
                    (int) coordinateShift.x + dx,
                    (int) coordinateShift.y + dy
            );
            repaint();
        }

        @Override
        public void onZoomingIn(MouseEvent event) {
            setScale((mainCanvas.getScale() / scaleFactor));
            setCoordinateShift(
                    (int) (coordinateShift.x / scaleFactor),
                    (int) (coordinateShift.y / scaleFactor)
            );
            repaint();
        }

        @Override
        public void onZoomingOut(MouseEvent event) {
            setScale((mainCanvas.getScale() * scaleFactor));
            setCoordinateShift(
                    (int) (coordinateShift.x * scaleFactor),
                    (int) (coordinateShift.y * scaleFactor)
            );
            repaint();
        }
    }
}
