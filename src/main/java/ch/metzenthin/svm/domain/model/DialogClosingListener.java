package ch.metzenthin.svm.domain.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public interface DialogClosingListener extends ActionListener {

  default void actionPerformed(ActionEvent e) {
    onCloseDialog();
  }

  void onCloseDialog();
}
