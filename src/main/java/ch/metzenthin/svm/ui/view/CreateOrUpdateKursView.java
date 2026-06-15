package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.ui.components.ComboBoxWithErrorLabelComponent;
import ch.metzenthin.svm.ui.components.CreateOrUpdateKursDialog;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateKursView extends SpeichernAbbrechenDialogView<CreateOrUpdateKursDialog> {

  private final ComboBoxWithErrorLabelComponent<Kurstyp> kurstypComboBoxWithErrorLabel;
  private final TextFieldWithErrorLabelComponent altersbereichWithErrorLabel;
  private final TextFieldWithErrorLabelComponent stufeWithErrorLabel;
  private final ComboBoxWithErrorLabelComponent<Wochentag> wochentagComboBoxWithErrorLabel;
  private final TextFieldWithErrorLabelComponent zeitBeginnWithErrorLabel;
  private final TextFieldWithErrorLabelComponent zeitEndeWithErrorLabel;
  private final ComboBoxWithErrorLabelComponent<Kursort> kursortComboBoxWithErrorLabel;
  private final ComboBoxWithErrorLabelComponent<Mitarbeiter> lehrkraft1ComboBoxWithErrorLabel;
  private final ComboBoxWithErrorLabelComponent<Mitarbeiter> lehrkraft2ComboBoxWithErrorLabel;
  private final TextFieldWithErrorLabelComponent bemerkungenWithErrorLabel;

  public CreateOrUpdateKursView(String title) {
    super(new CreateOrUpdateKursDialog(title));
    this.kurstypComboBoxWithErrorLabel =
        new ComboBoxWithErrorLabelComponent<>(
            dialog.getComboBoxKurstyp(), dialog.getErrLblKurstyp());
    this.altersbereichWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtAltersbereich(), dialog.getErrLblAltersbereich());
    this.stufeWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtStufe(), dialog.getErrLblStufe());
    this.wochentagComboBoxWithErrorLabel =
        new ComboBoxWithErrorLabelComponent<>(
            dialog.getComboBoxWochentag(), dialog.getErrLblWochentag());
    this.zeitBeginnWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtZeitBeginn(), dialog.getErrLblZeitBeginn());
    this.zeitEndeWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtZeitEnde(), dialog.getErrLblZeitEnde());
    this.kursortComboBoxWithErrorLabel =
        new ComboBoxWithErrorLabelComponent<>(
            dialog.getComboBoxKursort(), dialog.getErrLblKursort());
    this.lehrkraft1ComboBoxWithErrorLabel =
        new ComboBoxWithErrorLabelComponent<>(
            dialog.getComboBoxLehrkraft1(), dialog.getErrLblLehrkraft1());
    this.lehrkraft2ComboBoxWithErrorLabel =
        new ComboBoxWithErrorLabelComponent<>(
            dialog.getComboBoxLehrkraft2(), dialog.getErrLblLehrkraft2());
    this.bemerkungenWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBemerkungen(), dialog.getErrLblBemerkungen());
  }

  // Kurstyp
  public void setErrorLabelKurstypVisible(String errorMessage) {
    kurstypComboBoxWithErrorLabel.setErrorLabelVisible(true);
    kurstypComboBoxWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelKurstypInvisible() {
    kurstypComboBoxWithErrorLabel.setErrorLabelVisible(false);
  }

  public void addComboBoxKurstypActionListener(ActionListener actionListener) {
    kurstypComboBoxWithErrorLabel.addActionListener(actionListener);
  }

  public void setComboBoxKurstypValues(Kurstyp[] kurstypen) {
    kurstypComboBoxWithErrorLabel.setValues(kurstypen);
  }

  public Kurstyp getComboBoxKurstypSelectedItem() {
    return kurstypComboBoxWithErrorLabel.getSelectedItem();
  }

  public void setComboBoxKurstypSelectedItem(Kurstyp kurstyp) {
    kurstypComboBoxWithErrorLabel.setSelectedItem(kurstyp);
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
  public void setErrorLabelWochentagVisible(String errorMessage) {
    wochentagComboBoxWithErrorLabel.setErrorLabelVisible(true);
    wochentagComboBoxWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelWochentagInvisible() {
    wochentagComboBoxWithErrorLabel.setErrorLabelVisible(false);
  }

  public void addComboBoxWochentagActionListener(ActionListener actionListener) {
    wochentagComboBoxWithErrorLabel.addActionListener(actionListener);
  }

  public void setComboBoxWochentagValues(Wochentag[] wochentage) {
    wochentagComboBoxWithErrorLabel.setValues(wochentage);
  }

  public Wochentag getComboBoxWochentagSelectedItem() {
    return wochentagComboBoxWithErrorLabel.getSelectedItem();
  }

  public void setComboBoxWochentagSelectedItem(Wochentag wochentag) {
    wochentagComboBoxWithErrorLabel.setSelectedItem(wochentag);
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
  public void setErrorLabelKursortVisible(String errorMessage) {
    kursortComboBoxWithErrorLabel.setErrorLabelVisible(true);
    kursortComboBoxWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelKursortInvisible() {
    kursortComboBoxWithErrorLabel.setErrorLabelVisible(false);
  }

  public void addComboBoxKursortActionListener(ActionListener actionListener) {
    kursortComboBoxWithErrorLabel.addActionListener(actionListener);
  }

  public void setComboBoxKursortValues(Kursort[] kursorte) {
    kursortComboBoxWithErrorLabel.setValues(kursorte);
  }

  public Kursort getComboBoxKursortSelectedItem() {
    return kursortComboBoxWithErrorLabel.getSelectedItem();
  }

  public void setComboBoxKursortSelectedItem(Kursort kursort) {
    kursortComboBoxWithErrorLabel.setSelectedItem(kursort);
  }

  // Lehrkraft1
  public void setErrorLabelLehrkraft1Visible(String errorMessage) {
    lehrkraft1ComboBoxWithErrorLabel.setErrorLabelVisible(true);
    lehrkraft1ComboBoxWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelLehrkraft1Invisible() {
    lehrkraft1ComboBoxWithErrorLabel.setErrorLabelVisible(false);
  }

  public void addComboBoxLehrkraft1ActionListener(ActionListener actionListener) {
    lehrkraft1ComboBoxWithErrorLabel.addActionListener(actionListener);
  }

  public void setComboBoxLehrkraft1Values(Mitarbeiter[] lehrkraefte1) {
    lehrkraft1ComboBoxWithErrorLabel.setValues(lehrkraefte1);
  }

  public Mitarbeiter getComboBoxLehrkraft1SelectedItem() {
    return lehrkraft1ComboBoxWithErrorLabel.getSelectedItem();
  }

  public void setComboBoxLehrkraft1SelectedItem(Mitarbeiter lehrkraft1) {
    lehrkraft1ComboBoxWithErrorLabel.setSelectedItem(lehrkraft1);
  }

  // Lehrkraft2
  public void setErrorLabelLehrkraft2Visible(String errorMessage) {
    lehrkraft2ComboBoxWithErrorLabel.setErrorLabelVisible(true);
    lehrkraft2ComboBoxWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelLehrkraft2Invisible() {
    lehrkraft2ComboBoxWithErrorLabel.setErrorLabelVisible(false);
  }

  public void setComboBoxLehrkraft2Values(Mitarbeiter[] lehrkraefte2) {
    lehrkraft2ComboBoxWithErrorLabel.setValues(lehrkraefte2);
  }

  public Mitarbeiter getComboBoxLehrkraft2SelectedItem() {
    return lehrkraft2ComboBoxWithErrorLabel.getSelectedItem();
  }

  public void setComboBoxLehrkraft2SelectedItem(Mitarbeiter lehrkraft2) {
    lehrkraft2ComboBoxWithErrorLabel.setSelectedItem(lehrkraft2);
  }

  // Bemerkungen
  public String getTxtBemerkungenText() {
    return bemerkungenWithErrorLabel.getText();
  }

  public void setTxtBemerkungenText(String text) {
    bemerkungenWithErrorLabel.setText(text);
  }
}
