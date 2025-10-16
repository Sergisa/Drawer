package sergisa.drawer;


import sergisa.drawer.geometry.GraphicalNode;
import sergisa.drawer.geometry.PlacementAdapter;
import sergisa.drawer.settings.Settings;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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
    //ClassSettingPanel floatingPanel = new ClassSettingPanel();

    public Canvas() {
        setBackground(backgroundColor);
        setFocusable(true);
        DragListener drag = new DragListener();
        addMouseListener(drag);
        addMouseMotionListener(drag);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);

        /*NamedNode node1 = new NamedNode(100, 100, 30, 20);
        ClassNode node2 = new ClassNode(300, 300, 30, 30, 9);
        node2.setFillColor(Colors.Yellow);
        node1.addChildNode(node2.setNodeName("Parent"));
        nodeManager.addNode(node1);
        nodeManager.addNode(node2);
        nodeManager.addConnection(node1, node2, NodeManager.ConnectionType.AGGREGATION);*/

        updateOpts();
        setLayout(null);
        if (settings.getBoolean("cursor.show")) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else {
            setCursor(getToolkit().createCustomCursor(
                    new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                    new Point(),
                    null));

        }
    }

    private void updateOpts() {
        showCrosshair = settings.getBoolean("crosshair.show");
        showGrid = settings.getBoolean("grid.show");
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateOpts();
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (showGrid) paintGrid(g);

        if (showCrosshair && cross != null) (cross).paint(g);
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

    private Rectangle getActiveElementOuterRectangle(Rectangle activeElementBounds, int pad) {
        return new Rectangle(
                activeElementBounds.x - pad,
                activeElementBounds.y - pad,
                activeElementBounds.width + 2 * pad,
                activeElementBounds.height + 2 * pad
        );
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
        if (evt.getID() == MouseEvent.MOUSE_EXITED) {
            //this.transferFocus();
        } else {
            this.grabFocus();
        }
        if (evt.getID() == MouseEvent.MOUSE_CLICKED) {
            /*GraphicalNode currentShape = nodeManager.getNodeAtPosition(evt.getX(), evt.getY());
            if (currentShape != null) {
                if (nodeManager.getActiveElement() == currentShape) {
                    nodeManager.unsetActiveElement();
                } else {
                    nodeManager.setActiveElement(currentShape);
                    buildPanelForActiveElement(currentShape);
                }
                repaint();
            }*/
        }
        super.processMouseEvent(evt);
    }


    @Override
    protected void processKeyEvent(KeyEvent e) {
        int moveStep = 20;
        if ((e.getID() == KeyEvent.KEY_PRESSED) && (e.getModifiersEx() == 0)) {
            /*if (nodeManager.hasActiveElement()) {
                Rectangle oldBounds = nodeManager.getActiveElement().getBounds();
                int newX = oldBounds.x;
                int newY = oldBounds.y;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    newX += moveStep;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    newX -= moveStep;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    newY -= moveStep;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    newY += moveStep;
                }
                //nodeManager.getActiveElement().setX(newX);
                //nodeManager.getActiveElement().setY(newY);
                //replaceShape(nodeManager.getActiveElement(), newX, newY);
                if (showCrosshair && cross != null) {
                    cross.setXY(newX / 2, newY / 2);
                }
            }*/
            repaint();
        }
        super.processKeyEvent(e);
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

    public class DragListener extends MouseInputAdapter {
        Point firstPressPoint;
        GraphicalNode draggingElement;
        PlacementAdapter adapter;

        public DragListener() {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //nodeManager.setActiveElement(nodeManager.getNodeAtPosition(e.getPoint()));
            super.mouseClicked(e);
        }

        public void mousePressed(MouseEvent me) {
            firstPressPoint = me.getPoint();
            /*draggingElement = nodeManager.getNodeAtPosition(me.getX(), me.getY());
            if (draggingElement != null) {
                //nodeManager.setActiveElement(draggingElement);
                adapter = new PlacementAdapter(
                        me.getX() - draggingElement.getX(),
                        me.getY() - draggingElement.getY(),
                        draggingElement
                );
            }*/
        }

        public void mouseDragged(MouseEvent me) {
            if (me.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
                if (cross != null) cross.setXY(me.getX(), me.getY());
                if (draggingElement != null) {
                    adapter.setX(me.getX());
                    adapter.setY(me.getY());
                }
                if (cross != null || draggingElement != null) repaint();
            }
        }
        private void dragObject(GraphicalNode draggingNode){

        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if (draggingElement != null) {
                repaint();
            }
            super.mouseReleased(e);
        }
    }

}
