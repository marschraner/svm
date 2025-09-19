package ch.metzenthin.svm.ui.componentmodel;

import static ch.metzenthin.svm.common.utils.Converter.asString;

import java.util.Calendar;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Hans Stamm
 */
public class CalendarTableCellRenderer extends DefaultTableCellRenderer {

  public CalendarTableCellRenderer() {
    super();
    setHorizontalAlignment(SwingConstants.LEFT);
  }

  @Override
  public void setValue(Object value) {
    setText(asString((Calendar) value));
  }
}
