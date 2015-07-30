package ch.metzenthin.svm.ui.componentmodel;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Hans Stamm
 */
public class CalendarTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public void setValue(Object value) {
        setText(asString((Calendar) value));
    }

}
