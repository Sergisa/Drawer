package sergisa.drawer;


import sergisa.drawer.geometry.PlacementAdapter;
import sergisa.drawer.settings.Settings;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
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
    Shape focusedObject;
    private int scale = 1;

    public Canvas() {
        setBackground(backgroundColor);
        setFocusable(true);
        DragListener dragListener = new DragListener();
        addMouseListener(dragListener);
        addMouseMotionListener(dragListener);
        addMouseWheelListener(dragListener);
        drawingObjects = new ArrayList<>();
        RoundRectangle2D.Double rectangle = new RoundRectangle2D.Double(50, 200, 50, 80, 9, 9);
        RoundRectangle2D.Double rectangle2 = new RoundRectangle2D.Double(200, 300, 50, 80, 9, 9);
        drawingObjects.add(rectangle);
        drawingObjects.add(rectangle2);

        updateOpts();
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

    private void updateOpts() {
        showCrosshair = settings.getBoolean("crosshair.show");
        showGrid = settings.getBoolean("grid.show");
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        updateOpts();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (showGrid) paintGrid(g);
        g2d.scale(scale, scale);
        for (Shape drawingObject : drawingObjects) {
            g.setColor(objectColor);
            g2d.fill(drawingObject);

            g.setColor(Colors.Red);
            g2d.draw(drawingObject);
        }
        if (showCrosshair && cross != null) cross.paint(g);
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

    public boolean isShowCrosshair() {
        return showCrosshair;
    }

    public void setShowCrosshair(boolean showCrosshair) {
        this.showCrosshair = showCrosshair;
        if (!showCrosshair) repaint();
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
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

    public class DragListener extends MouseInputAdapter {
        Point lastPressPoint;
        RectangularShape draggingElement;
        PlacementAdapter adapter;

        public DragListener() {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //nodeManager.setActiveElement(nodeManager.getNodeAtPosition(e.getPoint()));
            super.mouseClicked(e);
        }

        public void mousePressed(MouseEvent evt) {
            lastPressPoint = evt.getPoint();
            draggingElement = getNodeAtPosition(evt.getX(), evt.getY());
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        public void mouseDragged(MouseEvent e) {
            int dx = e.getX() - lastPressPoint.x;
            int dy = e.getY() - lastPressPoint.y;
            if (SwingUtilities.isRightMouseButton(e)) {
                System.out.print("MOVE");
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                if (draggingElement != null) {
                    shiftShape(draggingElement, dx, dy);
                    repaint();
                }
            }
            lastPressPoint = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int scr = e.getUnitsToScroll();
            scale += scr;
            repaint();
            super.mouseWheelMoved(e);
        }
    }
}
