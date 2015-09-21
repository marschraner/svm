package ch.metzenthin.svm.ui.componentmodel;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Hans Stamm
 */
public class CalendarColorTableCellRenderer extends ColorTableCellRenderer {

    public CalendarColorTableCellRenderer(Color schriftfarbe) {
        super(schriftfarbe);
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    @Override
    public void setValue(Object value) {
        setText(asString((Calendar) value));
    }

}
