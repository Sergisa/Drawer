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
            lastPressPoint = adapter.adaptPoint(evt.getPoint());
            hittedElement = mainCanvas.getNodeAtPosition(
                    adapter.adaptPoint(evt.getPoint()).x,
                    adapter.adaptPoint(evt.getPoint()).y
            );
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

        public void mouseDragged(MouseEvent event) {
            if (SwingUtilities.isLeftMouseButton(event)) {
                if (hittedElement != null) {
                    onNodeDragging(event);
                } else if (mainCanvas.getSelectionRectanglePhantom() != null) {
                    onExtendedSelection(event);
                }
            } else if (SwingUtilities.isRightMouseButton(event)) {
                onPaneScrolling(
                        event.getPoint().x - lastPressPoint.x,
                        event.getPoint().y - lastPressPoint.y
                );
                lastPressPoint = event.getPoint();
            }

        }

        @Override
        public void mouseReleased(MouseEvent event) {
            if (hittedElement != null) {
                onFinishedNodeDragging(event);
            } else if (mainCanvas.getSelectionRectanglePhantom() != null) {
                onEndSelection(mainCanvas.getSelectionRectanglePhantom());
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent event) {
            mainCanvas.getCoordinateShift().x -= event.getX();
            mainCanvas.getCoordinateShift().y -= event.getY();
            if (event.getWheelRotation() == 1) {
                onZoomingIn(event);
            } else {
                onZoomingOut(event);
            }
            mainCanvas.getCoordinateShift().x += event.getX();
            mainCanvas.getCoordinateShift().y += event.getY();
        }
    }
}
