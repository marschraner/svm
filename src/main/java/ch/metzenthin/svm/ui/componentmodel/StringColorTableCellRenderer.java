package ch.metzenthin.svm.ui.componentmodel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Martin Schraner
 */
public class StringColorTableCellRenderer extends ColorTableCellRenderer {

    public StringColorTableCellRenderer(Color schriftfarbe) {
        super(schriftfarbe);
        setHorizontalAlignment(SwingConstants.LEFT);
    }

}
