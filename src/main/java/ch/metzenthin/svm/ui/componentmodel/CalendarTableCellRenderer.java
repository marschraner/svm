package ch.metzenthin.svm.ui.componentmodel;

import java.awt.*;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Hans Stamm
 */
public class CalendarTableCellRenderer extends ColorTableCellRenderer {

    public CalendarTableCellRenderer() {
        this(Color.BLACK);
    }

    public CalendarTableCellRenderer(Color schriftfarbe) {
        super(schriftfarbe);
    }

    @Override
    public void setValue(Object value) {
        setText(asString((Calendar) value));
    }

}
