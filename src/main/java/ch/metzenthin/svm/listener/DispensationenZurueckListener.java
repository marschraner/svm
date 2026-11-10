package ch.metzenthin.svm.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public interface DispensationenZurueckListener extends ActionListener {

  default void actionPerformed(ActionEvent e) {
    onDispensationenZurueck();
  }

  void onDispensationenZurueck();
}
