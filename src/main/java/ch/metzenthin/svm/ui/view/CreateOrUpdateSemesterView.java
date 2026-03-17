package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.ui.components.CreateOrUpdateSemesterDialog;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.function.Predicate;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateSemesterView
    extends SpeichernAbbrechenDialogView<CreateOrUpdateSemesterDialog> {

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

  public void addSpinnerSchuljahreChangeListener(
      ChangeListener changeListenerDelegate, Predicate<ChangeEvent> isInInitialisationMode) {
    spinnerSchuljahre.addChangeListener(
        e -> {
          if (!isInInitialisationMode.test(e)) {
            changeListenerDelegate.stateChanged(e);
          }
        });
  }

  public String getSpinnerSchuljahreValue() {
    return (spinnerSchuljahre.getValue() == null) ? "" : spinnerSchuljahre.getValue().toString();
  }

  public void setSpinnerSchuljahreValue(Object value) {
    spinnerSchuljahre.setValue(value);
  }

  public void setSpinnerSchuljahreToolTipText(String text) {
    spinnerSchuljahre.setToolTipText(text);
  }

  // Semesterbezeichnung

  public void addComboBoxSemesterbezeichnungActionListener(
      ActionListener actionListenerDelegate, Predicate<ActionEvent> isInInitialisationMode) {
    comboBoxSemesterbezeichnung.addActionListener(
        e -> {
          if (!isInInitialisationMode.test(e)) {
            actionListenerDelegate.actionPerformed(e);
          }
        });
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

  public void setComboBoxSemesterbezeichnungToolTipText(String text) {
    comboBoxSemesterbezeichnung.setToolTipText(text);
  }

  // Semesterbeginn

  public void setErrLblSemesterbeginnVisible(String errorMessage) {
    semesterbeginnWithErrorLabel.setErrorLabelVisible(true);
    semesterbeginnWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrLblSemesterbeginnInvisible() {
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

  public void setTxtSemesterbeginnToolTipText(String text) {
    semesterbeginnWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtSemesterbeginnEnabled() {
    return semesterbeginnWithErrorLabel.isEnabled();
  }

  // Semesterende

  public void setErrLblSemesterendeVisible(String errorMessage) {
    semesterendeWithErrorLabel.setErrorLabelVisible(true);
    semesterendeWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrLblSemesterendeInvisible() {
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

  public void setTxtSemesterendeToolTipText(String text) {
    semesterendeWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtSemesterendeEnabled() {
    return semesterendeWithErrorLabel.isEnabled();
  }

  // Ferienbeginn1

  public void setErrLblFerienbeginn1Visible(String errorMessage) {
    ferienbeginn1WithErrorLabel.setErrorLabelVisible(true);
    ferienbeginn1WithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrLblFerienbeginn1Invisible() {
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

  public void setTxtFerienbeginn1ToolTipText(String text) {
    ferienbeginn1WithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtFerienbeginn1Enabled() {
    return ferienbeginn1WithErrorLabel.isEnabled();
  }

  // Ferienende1

  public void setErrLblFerienende1Visible(String errorMessage) {
    ferienende1WithErrorLabel.setErrorLabelVisible(true);
    ferienende1WithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrLblFerienende1Invisible() {
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

  public void setTxtFerienende1ToolTipText(String text) {
    ferienende1WithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtFerienende1Enabled() {
    return ferienende1WithErrorLabel.isEnabled();
  }

  // Ferienbeginn2

  public void setErrLblFerienbeginn2Visible(String errorMessage) {
    ferienbeginn2WithErrorLabel.setErrorLabelVisible(true);
    ferienbeginn2WithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrLblFerienbeginn2Invisible() {
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

  public void setTxtFerienbeginn2ToolTipText(String text) {
    ferienbeginn2WithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtFerienbeginn2Enabled() {
    return ferienbeginn2WithErrorLabel.isEnabled();
  }

  // Ferienende2

  public void setErrLblFerienende2Visible(String errorMessage) {
    ferienende2WithErrorLabel.setErrorLabelVisible(true);
    ferienende2WithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrLblFerienende2Invisible() {
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

  public void setTxtFerienende2ToolTipText(String text) {
    ferienende2WithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtFerienende2Enabled() {
    return ferienende2WithErrorLabel.isEnabled();
  }
}
