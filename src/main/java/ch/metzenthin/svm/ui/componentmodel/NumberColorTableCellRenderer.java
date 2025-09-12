package ch.metzenthin.svm.ui.componentmodel;

import java.awt.*;
import javax.swing.*;

/**
 * @author Martin Schraner
 */
public class NumberColorTableCellRenderer extends ColorTableCellRenderer {

  public NumberColorTableCellRenderer(Color schriftfarbe) {
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
