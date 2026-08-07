package ch.metzenthin.svm.ui.view;

import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public interface SubmitDialogView {

  void addButtonSubmitActionListener(ActionListener actionListener);

  void setButtonSubmitFocusPainted(boolean focusPainted);

  void addButtonAbbrechenActionListener(ActionListener actionListener);

  void showDialog();

  void closeDialog();

  void configDialogClosing(ActionListener closeActionListener);

  void showErrorMessageDialog(String message, String title);
}
