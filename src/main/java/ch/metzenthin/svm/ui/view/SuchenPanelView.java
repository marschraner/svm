package ch.metzenthin.svm.ui.view;

import java.awt.event.ActionListener;
import javax.swing.JComponent;

/**
 * @author Martin Schraner
 */
public interface SuchenPanelView {

  JComponent getRootComponent();

  void addButtonSubmitActionListener(ActionListener actionListener);

  void addButtonAbbrechenActionListener(ActionListener actionListener);

  void showErrorMessageDialog(String message, String title);

  void abbrechen();

  void setWaitCursorAllComponents();

  void resetCursorAllComponents();
}
