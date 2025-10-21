package sergisa.drawer;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.RectangularShape;

public class CanvasMouseAdapter {
    Canvas mainCanvas;
    Point lastPressPoint;
    RectangularShape hittedElement;
    double scaleFactor = 1.1;
    UnShiftScaleAdapter adapter;


    public CanvasMouseAdapter(Canvas mainCanvas, UnShiftScaleAdapter adapter) {
        this.mainCanvas = mainCanvas;
        this.adapter = adapter;
        SimpleMouseAdapter mouseAdapter = new SimpleMouseAdapter();
        mainCanvas.addMouseListener(mouseAdapter);
        mainCanvas.addMouseMotionListener(mouseAdapter);
        mainCanvas.addMouseWheelListener(mouseAdapter);
    }


    public void onStartedNodeDrag(MouseEvent event) {
    }

    public void onNodeDragging(MouseEvent event) {
    }

    public void onFinishedNodeDragging(MouseEvent event) {
    }

    public void onStartedSelection(MouseEvent event) {
    }

    public void onExtendedSelection(MouseEvent event) {
    }

    public void onEndSelection(Rectangle selectionRectangle) {
    }

    public void onStartedPaneScroll() {
    }

    public void onPaneScrolling(int dx, int dy) {
    }

    public void onZoomingIn(MouseEvent event) {
    }

    public void onZoomingOut(MouseEvent event) {
    }

    private class SimpleMouseAdapter extends MouseInputAdapter {
        public void mousePressed(MouseEvent evt) {
            lastPressPoint = evt.getPoint();
            hittedElement = mainCanvas.getNodeAtPosition(evt.getX(), evt.getY());
            if (SwingUtilities.isLeftMouseButton(evt)) {
                if (hittedElement != null) {
                    onStartedNodeDrag(evt);
                } else {
                    onStartedSelection(evt);
                }
            } else if (SwingUtilities.isRightMouseButton(evt)) {
                onStartedPaneScroll();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
        }

        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (hittedElement != null) {
                    onNodeDragging(e);
                } else if (mainCanvas.getSelectionRectanglePhantom() != null) {
                    onExtendedSelection(e);
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                onPaneScrolling(e.getPoint().x - lastPressPoint.x, e.getPoint().y - lastPressPoint.y);
                lastPressPoint = e.getPoint();
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (hittedElement != null) {
                onFinishedNodeDragging(e);
            } else if (mainCanvas.getSelectionRectanglePhantom() != null) {
                onEndSelection(mainCanvas.getSelectionRectanglePhantom());
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            mainCanvas.getCoordinateShift().x -= e.getX();
            mainCanvas.getCoordinateShift().y -= e.getY();
            if (e.getWheelRotation() == 1) {
                onZoomingIn(e);
            } else {
                onZoomingOut(e);
            }
            mainCanvas.getCoordinateShift().x += e.getX();
            mainCanvas.getCoordinateShift().y += e.getY();
        }
    }
}
