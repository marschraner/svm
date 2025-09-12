package ch.metzenthin.svm.ui.componentmodel;

import static ch.metzenthin.svm.common.utils.Converter.asString;

import java.awt.*;
import java.util.Calendar;
import javax.swing.*;

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
