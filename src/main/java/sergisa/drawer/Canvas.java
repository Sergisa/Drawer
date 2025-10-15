package sergisa.drawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;

public class Canvas extends JComponent {
    int x;
    int y;
    int pad = 9;
    Vector<Shape> drawableShapes = new Vector<>();
    Shape activeElement = null;
    public boolean showCrosshair = false;
    public boolean showGrid = false;

    public Canvas() {
        setFocusable(true);
        CanvasDragListener dragListener = new CanvasDragListener();
        addMouseListener(dragListener);
        addMouseMotionListener(dragListener);
        drawableShapes.add(new RoundRectangle2D.Double(100, 100, 30, 20, 8, 8));
        drawableShapes.add(new RoundRectangle2D.Double(300, 300, 30, 30, 8, 8));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (showGrid) paintGrid(g);
        for (Shape s : drawableShapes) {
            g.setColor(new Color(0xF6FF00));
            ((Graphics2D) g).fill(s);
        }
        if (activeElement != null) {
            g.setColor(new Color(0x00A137));
            Rectangle outerRect = getActiveElementOuterRectangle(activeElement.getBounds(), 4);
            ((Graphics2D) g).draw(outerRect);
        }
        if (showCrosshair) paintCrossHair(g);
    }

    public void paintGrid(Graphics g) {
        int step = 20;
        g.setColor(new Color(0x2AD5A5FF, true));
        ((Graphics2D) g).setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{15f, 5f}, 5f));
        for (int i = 0; i < getWidth(); i += step) {
            g.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += step) {
            g.drawLine(0, i, getWidth(), i);
        }
        ((Graphics2D) g).setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, null, 5f));
    }

    public void paintCrossHair(Graphics g) {
        if (showCrosshair) {
            g.setColor(new Color(0x12FF00));
            g.drawLine(x, 0, x, y - pad);
            g.drawLine(x, y + pad, x, getHeight());

            g.drawLine(0, y, x - pad, y);
            g.drawLine(x + pad, y, getWidth(), y);
        }
    }

    private Rectangle getActiveElementOuterRectangle(Rectangle activeElementBounds, int pad) {
        return new Rectangle(
                activeElementBounds.x - pad,
                activeElementBounds.y - pad,
                activeElementBounds.width + 2 * pad,
                activeElementBounds.height + 2 * pad
        );
    }

    private void replaceShape(Shape shape, int x, int y) {
        RectangularShape currentshape = (RectangularShape) shape;
        Rectangle oldBounds = currentshape.getBounds();
        currentshape.setFrame(
                x,
                y,
                oldBounds.getWidth(),
                oldBounds.getHeight()
        );
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        int moveStep = 20;
        if ((e.getID() == KeyEvent.KEY_PRESSED) && (e.getModifiersEx() == 0)) {
            if (activeElement != null) {
                Rectangle oldBounds = activeElement.getBounds();
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    replaceShape(activeElement, oldBounds.x + moveStep, oldBounds.y);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    replaceShape(activeElement, oldBounds.x - moveStep, oldBounds.y);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    replaceShape(activeElement, oldBounds.x, oldBounds.y - moveStep);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    replaceShape(activeElement, oldBounds.x, oldBounds.y + moveStep);
                }
            }
            repaint();
        }
        super.processKeyEvent(e);
    }

    public Shape getShape(int mouseX, int mouseY) {
        int elementsCount = (drawableShapes == null) ? -1 : drawableShapes.size();
        for (int i = elementsCount - 1; i >= 0; i--) {
            Shape s = drawableShapes.elementAt(i);
            double xmin = (s.getBounds().x);
            double ymin = (s.getBounds().y);
            double xmax = ((s.getBounds().x + s.getBounds().width)) - 1;
            double ymax = ((s.getBounds().y + s.getBounds().height)) - 1;
            if (mouseX >= xmin && mouseX <= xmax && mouseY >= ymin && mouseY <= ymax)
                return s;
        }
        return null;
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
        if (showGrid) repaint();
    }

    public class CanvasDragListener extends MouseAdapter {
        Point oldPoint;

        @Override
        public void mousePressed(MouseEvent e) {
            oldPoint = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            double dx = e.getX() - oldPoint.x;
            double dy = e.getY() - oldPoint.y;
            ((RectangularShape) activeElement).setFrame(
                    activeElement.getBounds().x + dx,
                    activeElement.getBounds().y + dy,
                    activeElement.getBounds().width,
                    activeElement.getBounds().height
            );
            oldPoint = e.getPoint();
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }

        @Override
        public void mouseClicked(MouseEvent evt) {
            Shape currentShape = getShape(evt.getX(), evt.getY());
            if (currentShape != null) {
                activeElement = currentShape;
            }

            repaint();
        }
    }
}
