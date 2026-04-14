package ch.metzenthin.svm.ui.view;

import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateView {

  void addButtonSpeichernActionListener(ActionListener actionListener);

  void setButtonSpeichernFocusPainted(boolean focusPainted);

  void addButtonAbbrechenActionListener(ActionListener actionListener);

  void showDialog();

  void closeDialog();

  void configDialogClosing(ActionListener closeActionListener);

  void showErrorMessageDialog(String message, String title);
}
