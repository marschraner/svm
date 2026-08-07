package ch.metzenthin.svm.ui.components;

import javax.swing.*;

/**
 * @author Hans Stamm
 */
public abstract class CreateOrUpdateBezeichnungAndSelektierbarDialog extends AbstractSubmitDialog {

  public abstract JTextField getTxtBezeichnung();

  public abstract JLabel getErrLblBezeichnung();

  public abstract JCheckBox getCheckBoxSelektierbar();
}
