package ch.metzenthin.svm.ui.componentmodel;

import java.awt.*;

/**
 * @author Martin Schraner
 */
public class StringTableCellRenderer extends ColorTableCellRenderer {

    public StringTableCellRenderer() {
        this(Color.BLACK);
    }

    public StringTableCellRenderer(Color schriftfarbe) {
        super(schriftfarbe);
    }

}
