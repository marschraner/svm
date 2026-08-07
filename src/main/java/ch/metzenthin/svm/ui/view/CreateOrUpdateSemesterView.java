package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.ui.components.CreateOrUpdateSemesterDialog;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateSemesterView
    extends AbstractSubmitDialogView<CreateOrUpdateSemesterDialog> {

  private final JSpinner spinnerSchuljahre;
  private final JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung;
  private final TextFieldWithErrorLabelComponent semesterbeginnWithErrorLabel;
  private final TextFieldWithErrorLabelComponent semesterendeWithErrorLabel;
  private final TextFieldWithErrorLabelComponent ferienbeginn1WithErrorLabel;
  private final TextFieldWithErrorLabelComponent ferienende1WithErrorLabel;
  private final TextFieldWithErrorLabelComponent ferienbeginn2WithErrorLabel;
  private final TextFieldWithErrorLabelComponent ferienende2WithErrorLabel;

  public CreateOrUpdateSemesterView(String title) {
    super(new CreateOrUpdateSemesterDialog(title));
    this.spinnerSchuljahre = dialog.getSpinnerSchuljahre();
    this.comboBoxSemesterbezeichnung = dialog.getComboBoxSemesterbezeichnung();
    this.semesterbeginnWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtSemesterbeginn(), dialog.getErrLblSemesterbeginn());
    this.semesterendeWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtSemesterende(), dialog.getErrLblSemesterende());
    this.ferienbeginn1WithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtFerienbeginn1(), dialog.getErrLblFerienbeginn1());
    this.ferienende1WithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtFerienende1(), dialog.getErrLblFerienende1());
    this.ferienbeginn2WithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtFerienbeginn2(), dialog.getErrLblFerienbeginn2());
    this.ferienende2WithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtFerienende2(), dialog.getErrLblFerienende2());
  }

  // Schuljahre
  public void addSpinnerSchuljahreChangeListener(ChangeListener changeListenerDelegate) {
    spinnerSchuljahre.addChangeListener(changeListenerDelegate);
  }

  public String getSpinnerSchuljahreValue() {
    return (spinnerSchuljahre.getValue() == null) ? "" : spinnerSchuljahre.getValue().toString();
  }

  public void setSpinnerSchuljahreValue(Object value) {
    spinnerSchuljahre.setValue(value);
  }

  // Semesterbezeichnung
  public void addComboBoxSemesterbezeichnungActionListener(ActionListener actionListener) {
    comboBoxSemesterbezeichnung.addActionListener(actionListener);
  }

  public void setComboBoxSemesterbezeichnungValues(Semesterbezeichnung[] semesterbezeichnungen) {
    comboBoxSemesterbezeichnung.setModel(new DefaultComboBoxModel<>(semesterbezeichnungen));
  }

  public Semesterbezeichnung getComboBoxSemesterbezeichnungSelectedItem() {
    return (Semesterbezeichnung) comboBoxSemesterbezeichnung.getSelectedItem();
  }

  public void setComboBoxSemesterbezeichnungSelectedItem(Semesterbezeichnung semesterbezeichnung) {
    comboBoxSemesterbezeichnung.setSelectedItem(semesterbezeichnung);
  }

  // Semesterbeginn
  public void setErrorLabelSemesterbeginnVisible(String errorMessage) {
    semesterbeginnWithErrorLabel.setErrorLabelVisible(true);
    semesterbeginnWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelSemesterbeginnInvisible() {
    semesterbeginnWithErrorLabel.setErrorLabelVisible(false);
    semesterbeginnWithErrorLabel.setToolTipText(null);
  }

  public void addTxtSemesterbeginnActionListener(ActionListener actionListener) {
    semesterbeginnWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtSemesterbeginnFocusListener(FocusListener focusListener) {
    semesterbeginnWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtSemesterbeginnText() {
    return semesterbeginnWithErrorLabel.getText();
  }

  public void setTxtSemesterbeginnText(String text) {
    semesterbeginnWithErrorLabel.setText(text);
  }

  // Semesterende
  public void setErrorLabelSemesterendeVisible(String errorMessage) {
    semesterendeWithErrorLabel.setErrorLabelVisible(true);
    semesterendeWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelSemesterendeInvisible() {
    semesterendeWithErrorLabel.setErrorLabelVisible(false);
    semesterendeWithErrorLabel.setToolTipText(null);
  }

  public void addTxtSemesterendeActionListener(ActionListener actionListener) {
    semesterendeWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtSemesterendeFocusListener(FocusListener focusListener) {
    semesterendeWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtSemesterendeText() {
    return semesterendeWithErrorLabel.getText();
  }

  public void setTxtSemesterendeText(String text) {
    semesterendeWithErrorLabel.setText(text);
  }

  // Ferienbeginn1
  public void setErrorLabelFerienbeginn1Visible(String errorMessage) {
    ferienbeginn1WithErrorLabel.setErrorLabelVisible(true);
    ferienbeginn1WithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelFerienbeginn1Invisible() {
    ferienbeginn1WithErrorLabel.setErrorLabelVisible(false);
    ferienbeginn1WithErrorLabel.setToolTipText(null);
  }

  public void addTxtFerienbeginn1ActionListener(ActionListener actionListener) {
    ferienbeginn1WithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtFerienbeginn1FocusListener(FocusListener focusListener) {
    ferienbeginn1WithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtFerienbeginn1Text() {
    return ferienbeginn1WithErrorLabel.getText();
  }

  public void setTxtFerienbeginn1Text(String text) {
    ferienbeginn1WithErrorLabel.setText(text);
  }

  // Ferienende1
  public void setErrorLabelFerienende1Visible(String errorMessage) {
    ferienende1WithErrorLabel.setErrorLabelVisible(true);
    ferienende1WithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelFerienende1Invisible() {
    ferienende1WithErrorLabel.setErrorLabelVisible(false);
    ferienende1WithErrorLabel.setToolTipText(null);
  }

  public void addTxtFerienende1ActionListener(ActionListener actionListener) {
    ferienende1WithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtFerienende1FocusListener(FocusListener focusListener) {
    ferienende1WithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtFerienende1Text() {
    return ferienende1WithErrorLabel.getText();
  }

  public void setTxtFerienende1Text(String text) {
    ferienende1WithErrorLabel.setText(text);
  }

  // Ferienbeginn2
  public void setErrorLabelFerienbeginn2Visible(String errorMessage) {
    ferienbeginn2WithErrorLabel.setErrorLabelVisible(true);
    ferienbeginn2WithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelFerienbeginn2Invisible() {
    ferienbeginn2WithErrorLabel.setErrorLabelVisible(false);
    ferienbeginn2WithErrorLabel.setToolTipText(null);
  }

  public void addTxtFerienbeginn2ActionListener(ActionListener actionListener) {
    ferienbeginn2WithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtFerienbeginn2FocusListener(FocusListener focusListener) {
    ferienbeginn2WithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtFerienbeginn2Text() {
    return ferienbeginn2WithErrorLabel.getText();
  }

  public void setTxtFerienbeginn2Text(String text) {
    ferienbeginn2WithErrorLabel.setText(text);
  }

  // Ferienende2
  public void setErrorLabelFerienende2Visible(String errorMessage) {
    ferienende2WithErrorLabel.setErrorLabelVisible(true);
    ferienende2WithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelFerienende2Invisible() {
    ferienende2WithErrorLabel.setErrorLabelVisible(false);
    ferienende2WithErrorLabel.setToolTipText(null);
  }

  public void addTxtFerienende2ActionListener(ActionListener actionListener) {
    ferienende2WithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtFerienende2FocusListener(FocusListener focusListener) {
    ferienende2WithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtFerienende2Text() {
    return ferienende2WithErrorLabel.getText();
  }

  public void setTxtFerienende2Text(String text) {
    ferienende2WithErrorLabel.setText(text);
  }
}
