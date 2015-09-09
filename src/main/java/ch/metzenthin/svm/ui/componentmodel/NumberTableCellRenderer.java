package ch.metzenthin.svm.ui.componentmodel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Martin Schraner
 */
public class NumberTableCellRenderer extends ColorTableCellRenderer {

    public NumberTableCellRenderer() {
        this(Color.BLACK);
    }

    public NumberTableCellRenderer(Color schriftfarbe) {
        super(schriftfarbe);
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            setText(value.toString());
        } else {
            setText(null);
        }
    }

}
