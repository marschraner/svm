package ch.metzenthin.svm.ui.view;

import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public interface DialogView {

  void showDialog();

  void closeDialog();

  void configDialogClosing(ActionListener closeActionListener);
}
