package ch.metzenthin.svm.ui.componentmodel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Martin Schraner
 */

// Source: http://esus.com/creating-a-jtable-with-a-different-background-color-per-column/

public class ColorTableCellRenderer extends DefaultTableCellRenderer {

    private final Color schriftfarbe;

    public ColorTableCellRenderer(Color schriftfarbe) {
        super();
        this.schriftfarbe = schriftfarbe;
    }

    @Override
    public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component cell = super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
        cell.setForeground(schriftfarbe);
        return cell;
    }
}
