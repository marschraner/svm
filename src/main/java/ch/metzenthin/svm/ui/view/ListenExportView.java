package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.ui.components.ComboBoxWithErrorLabelComponent;
import ch.metzenthin.svm.ui.components.ListenExportDialog;
import ch.metzenthin.svm.ui.components.ListenExportFileChooser;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.io.File;

/**
 * @author Hans Stamm
 */
public class ListenExportView extends SpeichernAbbrechenDialogView<ListenExportDialog> {

  private final ComboBoxWithErrorLabelComponent<Listentyp> listentypComboBoxWithErrorLabel;
  private final TextFieldWithErrorLabelComponent titelWithErrorLabel;

  public ListenExportView(String title) {
    super(new ListenExportDialog(title));
    this.listentypComboBoxWithErrorLabel =
        new ComboBoxWithErrorLabelComponent<>(
            dialog.getComboBoxListentyp(), dialog.getErrLblListentyp());
    this.titelWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtTitel(), dialog.getErrLblTitel());
  }

  // Listentyp
  public void setErrorLabelListentypVisible(String errorMessage) {
    listentypComboBoxWithErrorLabel.setErrorLabelVisible(true);
    listentypComboBoxWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelListentypInvisible() {
    listentypComboBoxWithErrorLabel.setErrorLabelVisible(false);
  }

  public void addComboBoxListentypActionListener(ActionListener actionListener) {
    listentypComboBoxWithErrorLabel.addActionListener(actionListener);
  }

  public void setComboBoxListentypValues(Listentyp[] listentypen) {
    listentypComboBoxWithErrorLabel.setValues(listentypen);
  }

  public Listentyp getComboBoxListentypSelectedItem() {
    return listentypComboBoxWithErrorLabel.getSelectedItem();
  }

  public void setComboBoxListentypSelectedItem(Listentyp listentyp) {
    listentypComboBoxWithErrorLabel.setSelectedItem(listentyp);
  }

  // Titel
  public void setTxtTitelEnabled() {
    titelWithErrorLabel.setEnabled(true);
  }

  public void setTxtTitelDisabled() {
    titelWithErrorLabel.setEnabled(false);
  }

  public void setErrorLabelTitelVisible(String errorMessage) {
    titelWithErrorLabel.setErrorLabelVisible(true);
    titelWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelTitelInvisible() {
    titelWithErrorLabel.setErrorLabelVisible(false);
    titelWithErrorLabel.setToolTipText(null);
  }

  public void addTxtTitelActionListener(ActionListener actionListener) {
    titelWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtTitelFocusListener(FocusListener focusListener) {
    titelWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtTitelText() {
    return titelWithErrorLabel.getText();
  }

  public void setTxtTitelText(String text) {
    titelWithErrorLabel.setText(text);
  }

  public File showFileChooserAndGetSelectedFile() {
    ListenExportFileChooser listenExportConfigDialog =
        new ListenExportFileChooser(getComboBoxListentypSelectedItem(), dialog);
    return listenExportConfigDialog.getSelectedFile();
  }
}
