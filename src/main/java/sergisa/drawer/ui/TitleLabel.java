package sergisa.drawer.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public final class TitleLabel extends JLabel {
    private boolean myStrikeout = true;
    protected static final Color ourColor2 = Gray._128;

    public TitleLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
    }

    public void setStrikeout(boolean strikeout) {
        myStrikeout = strikeout;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(ourColor2);
        int startX = 0;
        Rectangle2D r = g.getFontMetrics().getStringBounds(getText(), g);
        if (!getText().isEmpty()) startX += (int) r.getWidth();
        int width = Math.max(getSize().width, getPreferredSize().width);
        Icon icon = getIcon();
        if (icon != null) {
            startX += icon.getIconWidth();
            startX += getIconTextGap();
        }
        g.drawLine(startX + 5, getSize().height / 2, width, getSize().height / 2);
    }
}