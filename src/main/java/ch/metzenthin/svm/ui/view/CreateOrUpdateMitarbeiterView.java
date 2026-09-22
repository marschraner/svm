package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.ui.components.ComboBoxWithErrorLabelComponent;
import ch.metzenthin.svm.ui.components.CreateOrUpdateMitarbeiterDialog;
import ch.metzenthin.svm.ui.components.TextAreaWithErrorLabelComponent;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateMitarbeiterView
    extends AbstractSubmitDialogView<CreateOrUpdateMitarbeiterDialog> {

  private final ComboBoxWithErrorLabelComponent<Anrede> anredeComboBoxWithErrorLabel;
  private final TextFieldWithErrorLabelComponent nachnameWithErrorLabel;
  private final TextFieldWithErrorLabelComponent vornameWithErrorLabel;
  private final TextFieldWithErrorLabelComponent strasseHausnummerWithErrorLabel;
  private final TextFieldWithErrorLabelComponent plzWithErrorLabel;
  private final TextFieldWithErrorLabelComponent ortWithErrorLabel;
  private final TextFieldWithErrorLabelComponent festnetzWithErrorLabel;
  private final TextFieldWithErrorLabelComponent natelWithErrorLabel;
  private final TextFieldWithErrorLabelComponent emailWithErrorLabel;
  private final TextFieldWithErrorLabelComponent geburtsdatumWithErrorLabel;
  private final TextFieldWithErrorLabelComponent ahvNummerWithErrorLabel;
  private final TextFieldWithErrorLabelComponent ibanNummerWithErrorLabel;
  private final TextAreaWithErrorLabelComponent vertretungsmoeglichkeitenWithErrorLabel;
  private final TextAreaWithErrorLabelComponent bemerkungenWithErrorLabel;
  private final JCheckBox lehrkraftCheckBox;
  private final JCheckBox aktivCheckBox;
  private final JLabel codesLabel;
  private final JButton buttonCodesBearbeiten;

  public CreateOrUpdateMitarbeiterView(String title) {
    super(new CreateOrUpdateMitarbeiterDialog(title));
    this.anredeComboBoxWithErrorLabel =
        new ComboBoxWithErrorLabelComponent<>(dialog.getComboBoxAnrede(), dialog.getErrLblAnrede());
    this.nachnameWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtNachname(), dialog.getErrLblNachname());
    this.vornameWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtVorname(), dialog.getErrLblVorname());
    this.strasseHausnummerWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtStrasseHausnummer(), dialog.getErrLblStrasseHausnummer());
    this.plzWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtPlz(), dialog.getErrLblPlz());
    this.ortWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtOrt(), dialog.getErrLblOrt());
    this.festnetzWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtFestnetz(), dialog.getErrLblFestnetz());
    this.natelWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtNatel(), dialog.getErrLblNatel());
    this.emailWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtEmail(), dialog.getErrLblEmail());
    this.geburtsdatumWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtGeburtsdatum(), dialog.getErrLblGeburtsdatum());
    this.ahvNummerWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtAhvNummer(), dialog.getErrLblAhvNummer());
    this.ibanNummerWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtIbanNummer(), dialog.getErrLblIbanNummer());
    this.vertretungsmoeglichkeitenWithErrorLabel =
        new TextAreaWithErrorLabelComponent(
            dialog.getTextAreaVertretungsmoeglichkeiten(),
            dialog.getErrLblVertretungsmoeglichkeiten());
    this.bemerkungenWithErrorLabel =
        new TextAreaWithErrorLabelComponent(
            dialog.getTextAreaBemerkungen(), dialog.getErrLblBemerkungen());
    this.lehrkraftCheckBox = dialog.getCheckBoxLehrkraft();
    this.aktivCheckBox = dialog.getCheckBoxAktiv();
    this.codesLabel = dialog.getLblCodes();
    this.buttonCodesBearbeiten = dialog.getBtnCodesBearbeiten();
  }

  // Anrede
  public void setErrorLabelAnredeVisible(String errorMessage) {
    anredeComboBoxWithErrorLabel.setErrorLabelVisible(true);
    anredeComboBoxWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelAnredeInvisible() {
    anredeComboBoxWithErrorLabel.setErrorLabelVisible(false);
  }

  public void addComboBoxAnredeActionListener(ActionListener actionListener) {
    anredeComboBoxWithErrorLabel.addActionListener(actionListener);
  }

  public void setComboBoxAnredeValues(Anrede[] anreden) {
    anredeComboBoxWithErrorLabel.setValues((anreden));
  }

  public Anrede getComboBoxAnredeSelectedItem() {
    return anredeComboBoxWithErrorLabel.getSelectedItem();
  }

  public void setComboBoxAnredeSelectedItem(Anrede anrede) {
    anredeComboBoxWithErrorLabel.setSelectedItem(anrede);
  }

  // Nachname
  public void setErrorLabelNachnameVisible(String errorMessage) {
    nachnameWithErrorLabel.setErrorLabelVisible(true);
    nachnameWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelNachnameInvisible() {
    nachnameWithErrorLabel.setErrorLabelVisible(false);
    nachnameWithErrorLabel.setToolTipText(null);
  }

  public void addTxtNachnameActionListener(ActionListener actionListener) {
    nachnameWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtNachnameFocusListener(FocusListener focusListener) {
    nachnameWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtNachnameText() {
    return nachnameWithErrorLabel.getText();
  }

  public void setTxtNachnameText(String text) {
    nachnameWithErrorLabel.setText(text);
  }

  // Vorname
  public void setErrorLabelVornameVisible(String errorMessage) {
    vornameWithErrorLabel.setErrorLabelVisible(true);
    vornameWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelVornameInvisible() {
    vornameWithErrorLabel.setErrorLabelVisible(false);
    vornameWithErrorLabel.setToolTipText(null);
  }

  public void addTxtVornameActionListener(ActionListener actionListener) {
    vornameWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtVornameFocusListener(FocusListener focusListener) {
    vornameWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtVornameText() {
    return vornameWithErrorLabel.getText();
  }

  public void setTxtVornameText(String text) {
    vornameWithErrorLabel.setText(text);
  }

  // Strasse Hausnummer
  public void setErrorLabelStrasseHausnummerVisible(String errorMessage) {
    strasseHausnummerWithErrorLabel.setErrorLabelVisible(true);
    strasseHausnummerWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelStrasseHausnummerInvisible() {
    strasseHausnummerWithErrorLabel.setErrorLabelVisible(false);
    strasseHausnummerWithErrorLabel.setToolTipText(null);
  }

  public void addTxtStrasseHausnummerActionListener(ActionListener actionListener) {
    strasseHausnummerWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtStrasseHausnummerFocusListener(FocusListener focusListener) {
    strasseHausnummerWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtStrasseHausnummerText() {
    return strasseHausnummerWithErrorLabel.getText();
  }

  public void setTxtStrasseHausnummerText(String text) {
    strasseHausnummerWithErrorLabel.setText(text);
  }

  // Plz
  public void setErrorLabelPlzVisible(String errorMessage) {
    plzWithErrorLabel.setErrorLabelVisible(true);
    plzWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelPlzInvisible() {
    plzWithErrorLabel.setErrorLabelVisible(false);
    plzWithErrorLabel.setToolTipText(null);
  }

  public void addTxtPlzActionListener(ActionListener actionListener) {
    plzWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtPlzFocusListener(FocusListener focusListener) {
    plzWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtPlzText() {
    return plzWithErrorLabel.getText();
  }

  public void setTxtPlzText(String text) {
    plzWithErrorLabel.setText(text);
  }

  // Ort
  public void setErrorLabelOrtVisible(String errorMessage) {
    ortWithErrorLabel.setErrorLabelVisible(true);
    ortWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelOrtInvisible() {
    ortWithErrorLabel.setErrorLabelVisible(false);
    ortWithErrorLabel.setToolTipText(null);
  }

  public void addTxtOrtActionListener(ActionListener actionListener) {
    ortWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtOrtFocusListener(FocusListener focusListener) {
    ortWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtOrtText() {
    return ortWithErrorLabel.getText();
  }

  public void setTxtOrtText(String text) {
    ortWithErrorLabel.setText(text);
  }

  // Festnetz
  public void setErrorLabelFestnetzVisible(String errorMessage) {
    festnetzWithErrorLabel.setErrorLabelVisible(true);
    festnetzWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelFestnetzInvisible() {
    festnetzWithErrorLabel.setErrorLabelVisible(false);
    festnetzWithErrorLabel.setToolTipText(null);
  }

  public void addTxtFestnetzActionListener(ActionListener actionListener) {
    festnetzWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtFestnetzFocusListener(FocusListener focusListener) {
    festnetzWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtFestnetzText() {
    return festnetzWithErrorLabel.getText();
  }

  public void setTxtFestnetzText(String text) {
    festnetzWithErrorLabel.setText(text);
  }

  // Natel
  public void setErrorLabelNatelVisible(String errorMessage) {
    natelWithErrorLabel.setErrorLabelVisible(true);
    natelWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelNatelInvisible() {
    natelWithErrorLabel.setErrorLabelVisible(false);
    natelWithErrorLabel.setToolTipText(null);
  }

  public void addTxtNatelActionListener(ActionListener actionListener) {
    natelWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtNatelFocusListener(FocusListener focusListener) {
    natelWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtNatelText() {
    return natelWithErrorLabel.getText();
  }

  public void setTxtNatelText(String text) {
    natelWithErrorLabel.setText(text);
  }

  // Email
  public void setErrorLabelEmailVisible(String errorMessage) {
    emailWithErrorLabel.setErrorLabelVisible(true);
    emailWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelEmailInvisible() {
    emailWithErrorLabel.setErrorLabelVisible(false);
    emailWithErrorLabel.setToolTipText(null);
  }

  public void addTxtEmailActionListener(ActionListener actionListener) {
    emailWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtEmailFocusListener(FocusListener focusListener) {
    emailWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtEmailText() {
    return emailWithErrorLabel.getText();
  }

  public void setTxtEmailText(String text) {
    emailWithErrorLabel.setText(text);
  }

  // Geburtsdatum
  public void setErrorLabelGeburtsdatumVisible(String errorMessage) {
    geburtsdatumWithErrorLabel.setErrorLabelVisible(true);
    geburtsdatumWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelGeburtsdatumInvisible() {
    geburtsdatumWithErrorLabel.setErrorLabelVisible(false);
    geburtsdatumWithErrorLabel.setToolTipText(null);
  }

  public void addTxtGeburtsdatumActionListener(ActionListener actionListener) {
    geburtsdatumWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtGeburtsdatumFocusListener(FocusListener focusListener) {
    geburtsdatumWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtGeburtsdatumText() {
    return geburtsdatumWithErrorLabel.getText();
  }

  public void setTxtGeburtsdatumText(String text) {
    geburtsdatumWithErrorLabel.setText(text);
  }

  // Ahv-Nummer
  public void setErrorLabelAhvNummerVisible(String errorMessage) {
    ahvNummerWithErrorLabel.setErrorLabelVisible(true);
    ahvNummerWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelAhvNummerInvisible() {
    ahvNummerWithErrorLabel.setErrorLabelVisible(false);
    ahvNummerWithErrorLabel.setToolTipText(null);
  }

  public void addTxtAhvNummerActionListener(ActionListener actionListener) {
    ahvNummerWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtAhvNummerFocusListener(FocusListener focusListener) {
    ahvNummerWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtAhvNummerText() {
    return ahvNummerWithErrorLabel.getText();
  }

  public void setTxtAhvNummerText(String text) {
    ahvNummerWithErrorLabel.setText(text);
  }

  // Iban-Nummer
  public void setErrorLabelIbanNummerVisible(String errorMessage) {
    ibanNummerWithErrorLabel.setErrorLabelVisible(true);
    ibanNummerWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelIbanNummerInvisible() {
    ibanNummerWithErrorLabel.setErrorLabelVisible(false);
    ibanNummerWithErrorLabel.setToolTipText(null);
  }

  public void addTxtIbanNummerActionListener(ActionListener actionListener) {
    ibanNummerWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtIbanNummerFocusListener(FocusListener focusListener) {
    ibanNummerWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtIbanNummerText() {
    return ibanNummerWithErrorLabel.getText();
  }

  public void setTxtIbanNummerText(String text) {
    ibanNummerWithErrorLabel.setText(text);
  }

  // Vertretungsmoeglichkeiten
  public void setErrorLabelVertretungsmoeglichkeitenVisible(String errorMessage) {
    vertretungsmoeglichkeitenWithErrorLabel.setErrorLabelVisible(true);
    vertretungsmoeglichkeitenWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelVertretungsmoeglichkeitenInvisible() {
    vertretungsmoeglichkeitenWithErrorLabel.setErrorLabelVisible(false);
    vertretungsmoeglichkeitenWithErrorLabel.setToolTipText(null);
  }

  public void addTxtVertretungsmoeglichkeitenFocusListener(FocusListener focusListener) {
    vertretungsmoeglichkeitenWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtVertretungsmoeglichkeitenText() {
    return vertretungsmoeglichkeitenWithErrorLabel.getText();
  }

  public void setTxtVertretungsmoeglichkeitenText(String text) {
    vertretungsmoeglichkeitenWithErrorLabel.setText(text);
  }

  // Bemerkungen
  public void setErrorLabelBemerkungenVisible(String errorMessage) {
    bemerkungenWithErrorLabel.setErrorLabelVisible(true);
    bemerkungenWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelBemerkungenInvisible() {
    bemerkungenWithErrorLabel.setErrorLabelVisible(false);
    bemerkungenWithErrorLabel.setToolTipText(null);
  }

  public void addTxtBemerkungenFocusListener(FocusListener focusListener) {
    bemerkungenWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBemerkungenText() {
    return bemerkungenWithErrorLabel.getText();
  }

  public void setTxtBemerkungenText(String text) {
    bemerkungenWithErrorLabel.setText(text);
  }

  // Lehrkraft
  public boolean isCheckBoxLehrkraftSelected() {
    return lehrkraftCheckBox.isSelected();
  }

  public void setCheckBoxLehrkraftSelected(boolean selected) {
    lehrkraftCheckBox.setSelected(selected);
  }

  // Aktiv
  public boolean isCheckBoxAktivSelected() {
    return aktivCheckBox.isSelected();
  }

  public void setCheckBoxAktivSelected(boolean selected) {
    aktivCheckBox.setSelected(selected);
  }

  // Codes-Label
  public void setLabelCodesText(String codesText) {
    codesLabel.setText(codesText);
  }

  // Bearbeiten-Button
  public void addButtonCodesBearbeitenActionListener(ActionListener actionListener) {
    buttonCodesBearbeiten.addActionListener(actionListener);
  }
}
