package ch.metzenthin.svm.ui.view;

import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public interface SubmitDialogView extends DialogView {

  void addButtonSubmitActionListener(ActionListener actionListener);

  void setButtonSubmitFocusPainted(boolean focusPainted);

  void addButtonAbbrechenActionListener(ActionListener actionListener);

  void showErrorMessageDialog(String message, String title);
}
