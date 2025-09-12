package ch.metzenthin.svm.ui.componentmodel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Hans Stamm
 */
public class StringNumberTableCellRenderer extends DefaultTableCellRenderer {

  public StringNumberTableCellRenderer() {
    super();
    setHorizontalAlignment(SwingConstants.LEFT);
  }

  @Override
  public void setValue(Object value) {
    setText(value.toString());
  }
}
