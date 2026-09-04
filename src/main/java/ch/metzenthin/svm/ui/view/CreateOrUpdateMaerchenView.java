package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.CreateOrUpdateMaerchenDialog;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateMaerchenView
    extends AbstractSubmitDialogView<CreateOrUpdateMaerchenDialog> {

  private final JSpinner schuljahrSpinner;
  private final TextFieldWithErrorLabelComponent bezeichnungWithErrorLabel;
  private final TextFieldWithErrorLabelComponent anzahlVorstellungenWithErrorLabel;

  public CreateOrUpdateMaerchenView(String title) {
    super(new CreateOrUpdateMaerchenDialog(title));
    this.schuljahrSpinner = dialog.getSchuljahrSpinner();
    this.bezeichnungWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBezeichnung(), dialog.getErrLblBezeichnung());
    this.anzahlVorstellungenWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtAnzahlVorstellungen(), dialog.getErrLblAnzahlVorstellungen());
  }

  // Schuljahre
  public void setSchuljahrSpinnerModelValues(String[] schuljahre) {
    SpinnerModel schuljahrSpinnerModel = new SpinnerListModel(schuljahre);
    schuljahrSpinner.setModel(schuljahrSpinnerModel);
  }

  public String getSchuljahrSpinnerValue() {
    return (schuljahrSpinner.getValue() == null) ? "" : schuljahrSpinner.getValue().toString();
  }

  public void setSchuljahrSpinnerValue(Object value) {
    schuljahrSpinner.setValue(value);
  }

  public void addSchuljahrSpinnerChangeListener(ChangeListener changeListenerDelegate) {
    schuljahrSpinner.addChangeListener(changeListenerDelegate);
  }

  // Bezeichnung
  public void setErrorLabelBezeichnungVisible(String errorMessage) {
    bezeichnungWithErrorLabel.setErrorLabelVisible(true);
    bezeichnungWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelBezeichnungInvisible() {
    bezeichnungWithErrorLabel.setErrorLabelVisible(false);
    bezeichnungWithErrorLabel.setToolTipText(null);
  }

  public void addTxtBezeichnungActionListener(ActionListener actionListener) {
    bezeichnungWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtBezeichnungFocusListener(FocusListener focusListener) {
    bezeichnungWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBezeichnungText() {
    return bezeichnungWithErrorLabel.getText();
  }

  public void setTxtBezeichnungText(String text) {
    bezeichnungWithErrorLabel.setText(text);
  }

  // Anzahl Vorstellungen
  public void setErrorLabelAnzahlVorstellungenVisible(String errorMessage) {
    anzahlVorstellungenWithErrorLabel.setErrorLabelVisible(true);
    anzahlVorstellungenWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelAnzahlVorstellungenInvisible() {
    anzahlVorstellungenWithErrorLabel.setErrorLabelVisible(false);
    anzahlVorstellungenWithErrorLabel.setToolTipText(null);
  }

  public void addTxtAnzahlVorstellungenActionListener(ActionListener actionListener) {
    anzahlVorstellungenWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtAnzahlVorstellungenFocusListener(FocusListener focusListener) {
    anzahlVorstellungenWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtAnzahlVorstellungenText() {
    return anzahlVorstellungenWithErrorLabel.getText();
  }

  public void setTxtAnzahlVorstellungenText(String text) {
    anzahlVorstellungenWithErrorLabel.setText(text);
  }
}
