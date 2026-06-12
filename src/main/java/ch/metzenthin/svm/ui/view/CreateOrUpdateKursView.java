package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.ui.components.CreateOrUpdateKursDialog;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateKursView extends SpeichernAbbrechenDialogView<CreateOrUpdateKursDialog> {

  private final JComboBox<Kurstyp> comboBoxKurstyp;
  private final TextFieldWithErrorLabelComponent altersbereichWithErrorLabel;
  private final TextFieldWithErrorLabelComponent stufeWithErrorLabel;
  private final JComboBox<Wochentag> comboBoxWochentag;
  private final TextFieldWithErrorLabelComponent zeitBeginnWithErrorLabel;
  private final TextFieldWithErrorLabelComponent zeitEndeWithErrorLabel;
  private final JComboBox<Kursort> comboBoxKursort;
  private final JComboBox<Mitarbeiter> comboBoxLehrkraft1;
  private final JComboBox<Mitarbeiter> comboBoxLehrkraft2;
  private final TextFieldWithErrorLabelComponent bemerkungenWithErrorLabel;

  public CreateOrUpdateKursView(String title) {
    super(new CreateOrUpdateKursDialog(title));
    this.comboBoxKurstyp = dialog.getComboBoxKurstyp();
    this.altersbereichWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtAltersbereich(), dialog.getErrLblAltersbereich());
    this.stufeWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtStufe(), dialog.getErrLblStufe());
    this.comboBoxWochentag = dialog.getComboBoxWochentag();
    this.zeitBeginnWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtZeitBeginn(), dialog.getErrLblZeitBeginn());
    this.zeitEndeWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtZeitEnde(), dialog.getErrLblZeitEnde());
    this.comboBoxKursort = dialog.getComboBoxKursort();
    this.comboBoxLehrkraft1 = dialog.getComboBoxLehrkraft1();
    this.comboBoxLehrkraft2 = dialog.getComboBoxLehrkraft2();
    this.bemerkungenWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBemerkungen(), dialog.getErrLblBemerkungen());
  }

  // Kurstyp
  public void addComboBoxKurstypActionListener(ActionListener actionListener) {
    comboBoxKurstyp.addActionListener(actionListener);
  }

  public void setComboBoxKurstypValues(Kurstyp[] kurstypen) {
    comboBoxKurstyp.setModel(new DefaultComboBoxModel<>(kurstypen));
  }

  public Kurstyp getComboBoxKurstypSelectedItem() {
    return (Kurstyp) comboBoxKurstyp.getSelectedItem();
  }

  public void setComboBoxKurstypSelectedItem(Kurstyp kurstyp) {
    comboBoxKurstyp.setSelectedItem(kurstyp);
  }

  // Altersbereich
  public void setErrorLabelAltersbereichVisible(String errorMessage) {
    altersbereichWithErrorLabel.setErrorLabelVisible(true);
    altersbereichWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelAltersbereichInvisible() {
    altersbereichWithErrorLabel.setErrorLabelVisible(false);
    altersbereichWithErrorLabel.setToolTipText(null);
  }

  public void addTxtAltersbereichActionListener(ActionListener actionListener) {
    altersbereichWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtAltersbereichFocusListener(FocusListener focusListener) {
    altersbereichWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtAltersbereichText() {
    return altersbereichWithErrorLabel.getText();
  }

  public void setTxtAltersbereichText(String text) {
    altersbereichWithErrorLabel.setText(text);
  }

  // Stufe
  public void setErrorLabelStufeVisible(String errorMessage) {
    stufeWithErrorLabel.setErrorLabelVisible(true);
    stufeWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelStufeInvisible() {
    stufeWithErrorLabel.setErrorLabelVisible(false);
    stufeWithErrorLabel.setToolTipText(null);
  }

  public void addTxtStufeActionListener(ActionListener actionListener) {
    stufeWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtStufeFocusListener(FocusListener focusListener) {
    stufeWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtStufeText() {
    return stufeWithErrorLabel.getText();
  }

  public void setTxtStufeText(String text) {
    stufeWithErrorLabel.setText(text);
  }

  // Wochentag
  public void addComboBoxWochentagActionListener(ActionListener actionListener) {
    comboBoxWochentag.addActionListener(actionListener);
  }

  public void setComboBoxWochentagValues(Wochentag[] wochentage) {
    comboBoxWochentag.setModel(new DefaultComboBoxModel<>(wochentage));
  }

  public Wochentag getComboBoxWochentagSelectedItem() {
    return (Wochentag) comboBoxWochentag.getSelectedItem();
  }

  public void setComboBoxWochentagSelectedItem(Wochentag wochentag) {
    comboBoxWochentag.setSelectedItem(wochentag);
  }

  // ZeitBeginn
  public void setErrorLabelZeitBeginnVisible(String errorMessage) {
    zeitBeginnWithErrorLabel.setErrorLabelVisible(true);
    zeitBeginnWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelZeitBeginnInvisible() {
    zeitBeginnWithErrorLabel.setErrorLabelVisible(false);
    zeitBeginnWithErrorLabel.setToolTipText(null);
  }

  public void addTxtZeitBeginnActionListener(ActionListener actionListener) {
    zeitBeginnWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtZeitBeginnFocusListener(FocusListener focusListener) {
    zeitBeginnWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtZeitBeginnText() {
    return zeitBeginnWithErrorLabel.getText();
  }

  public void setTxtZeitBeginnText(String text) {
    zeitBeginnWithErrorLabel.setText(text);
  }

  // ZeitEnde
  public void setErrorLabelZeitEndeVisible(String errorMessage) {
    zeitEndeWithErrorLabel.setErrorLabelVisible(true);
    zeitEndeWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelZeitEndeInvisible() {
    zeitEndeWithErrorLabel.setErrorLabelVisible(false);
    zeitEndeWithErrorLabel.setToolTipText(null);
  }

  public void addTxtZeitEndeActionListener(ActionListener actionListener) {
    zeitEndeWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtZeitEndeFocusListener(FocusListener focusListener) {
    zeitEndeWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtZeitEndeText() {
    return zeitEndeWithErrorLabel.getText();
  }

  public void setTxtZeitEndeText(String text) {
    zeitEndeWithErrorLabel.setText(text);
  }

  // Kursort
  public void addComboBoxKursortActionListener(ActionListener actionListener) {
    comboBoxKursort.addActionListener(actionListener);
  }

  public void setComboBoxKursortValues(Kursort[] kursorte) {
    comboBoxKursort.setModel(new DefaultComboBoxModel<>(kursorte));
  }

  public Kursort getComboBoxKursortSelectedItem() {
    return (Kursort) comboBoxKursort.getSelectedItem();
  }

  public void setComboBoxKursortSelectedItem(Kursort kursort) {
    comboBoxKursort.setSelectedItem(kursort);
  }

  // Lehrkraft1
  public void addComboBoxLehrkraft1ActionListener(ActionListener actionListener) {
    comboBoxLehrkraft1.addActionListener(actionListener);
  }

  public void setComboBoxLehrkraft1Values(Mitarbeiter[] lehrkraefte1) {
    comboBoxLehrkraft1.setModel(new DefaultComboBoxModel<>(lehrkraefte1));
  }

  public Mitarbeiter getComboBoxLehrkraft1SelectedItem() {
    return (Mitarbeiter) comboBoxLehrkraft1.getSelectedItem();
  }

  public void setComboBoxLehrkraft1SelectedItem(Mitarbeiter lehrkraft1) {
    comboBoxLehrkraft1.setSelectedItem(lehrkraft1);
  }

  // Lehrkraft2
  public void setComboBoxLehrkraft2Values(Mitarbeiter[] lehrkraefte2) {
    comboBoxLehrkraft2.setModel(new DefaultComboBoxModel<>(lehrkraefte2));
  }

  public Mitarbeiter getComboBoxLehrkraft2SelectedItem() {
    return (Mitarbeiter) comboBoxLehrkraft2.getSelectedItem();
  }

  public void setComboBoxLehrkraft2SelectedItem(Mitarbeiter lehrkraft2) {
    comboBoxLehrkraft2.setSelectedItem(lehrkraft2);
  }

  // Bemerkungen
  public String getTxtBemerkungenText() {
    return bemerkungenWithErrorLabel.getText();
  }

  public void setTxtBemerkungenText(String text) {
    bemerkungenWithErrorLabel.setText(text);
  }
}
