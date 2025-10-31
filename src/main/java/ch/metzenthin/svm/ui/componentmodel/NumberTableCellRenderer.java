package ch.metzenthin.svm.ui.componentmodel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Martin Schraner
 */
public class NumberTableCellRenderer extends DefaultTableCellRenderer {

  public NumberTableCellRenderer() {
    super();
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
