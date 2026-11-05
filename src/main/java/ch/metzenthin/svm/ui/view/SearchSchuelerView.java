package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.common.datatypes.Gruppe;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel.AnmeldestatusSelected;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel.DispensationSelected;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel.GeschlechtSelected;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel.RolleSelected;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.components.RadioButtonComponent;
import ch.metzenthin.svm.ui.components.RadioButtonGroupComponent;
import ch.metzenthin.svm.ui.components.SearchSchuelerPanel;
import ch.metzenthin.svm.ui.components.TextAreaWithErrorLabelComponent;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

/**
 * @author Hans Stamm
 */
public class SearchSchuelerView extends AbstractSubmitPanelView<SearchSchuelerPanel> {

  private final TextFieldWithErrorLabelComponent nachnameWithErrorLabel;
  private final TextFieldWithErrorLabelComponent vornameWithErrorLabel;
  private final TextFieldWithErrorLabelComponent strasseHausnummerWithErrorLabel;
  private final TextFieldWithErrorLabelComponent plzWithErrorLabel;
  private final TextFieldWithErrorLabelComponent ortWithErrorLabel;
  private final TextFieldWithErrorLabelComponent festnetzWithErrorLabel;
  private final TextFieldWithErrorLabelComponent natelWithErrorLabel;
  private final TextFieldWithErrorLabelComponent emailWithErrorLabel;
  private final TextFieldWithErrorLabelComponent geburtsdatumSuchperiodeWithErrorLabel;
  private final RadioButtonGroupComponent geschlechtRadioButtonGroup;
  private final RadioButtonGroupComponent rolleRadioButtonGroup;
  private final RadioButtonGroupComponent anmeldestatusRadioButtonGroup;
  private final RadioButtonGroupComponent dispensationRadioButtonGroup;
  private final TextFieldWithErrorLabelComponent stichtagWithErrorLabel;
  private final JComboBox<SchuelerCode> schuelerCodeComboBox;
  private final JSpinner semesterKursSpinner;
  private final JComboBox<Wochentag> wochentagComboBox;
  private final TextFieldWithErrorLabelComponent zeitBeginnWithErrorLabel;
  private final JComboBox<Mitarbeiter> lehrkraftComboBox;
  private final JCheckBox kursFuerSucheBeruecksichtigenCheckBox;
  private final JSpinner maerchenSpinner;
  private final JComboBox<Gruppe> gruppeComboBox;
  private final TextAreaWithErrorLabelComponent rollenWithErrorLabel;
  private final JComboBox<ElternmithilfeCode> elternmithilfeCodeComboBox;
  private final TextFieldWithErrorLabelComponent kuchenVorstellungWithErrorLabel;
  private final TextFieldWithErrorLabelComponent zusatzattributMaerchenWithErrorLabel;
  private final JCheckBox maerchenFuerSucheBeruecksichtigenCheckBox;

  public SearchSchuelerView(ActionListener closeListener) {
    super(new SearchSchuelerPanel(), closeListener);
    this.nachnameWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtNachname(), panel.getErrLblNachname());
    this.vornameWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtVorname(), panel.getErrLblVorname());
    this.strasseHausnummerWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            panel.getTxtStrasseHausnummer(), panel.getErrLblStrasseHausnummer());
    this.plzWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtOrt(), panel.getErrLblOrt());
    this.ortWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtPlz(), panel.getErrLblPlz());
    this.festnetzWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtFestnetz(), panel.getErrLblFestnetz());
    this.natelWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtNatel(), panel.getErrLblNatel());
    this.emailWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtEmail(), panel.getErrLblEmail());
    this.geburtsdatumSuchperiodeWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            panel.getTxtGeburtsdatumSuchperiode(), panel.getErrLblGeburtsdatumSuchperiode());
    this.geschlechtRadioButtonGroup =
        new RadioButtonGroupComponent(
            new RadioButtonComponent(
                panel.getRadioBtnWeiblich(), GeschlechtSelected.WEIBLICH.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnMaennlich(), GeschlechtSelected.MAENNLICH.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnGeschlechtAlle(), GeschlechtSelected.ALLE.toString()));
    this.rolleRadioButtonGroup =
        new RadioButtonGroupComponent(
            new RadioButtonComponent(
                panel.getRadioBtnSchueler(), RolleSelected.SCHUELER.toString()),
            new RadioButtonComponent(panel.getRadioBtnEltern(), RolleSelected.ELTERN.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnRechnungsempfaenger(),
                RolleSelected.RECHNUNGSEMPFAENGER.toString()),
            new RadioButtonComponent(panel.getRadioBtnRolleAlle(), RolleSelected.ALLE.toString()));
    this.anmeldestatusRadioButtonGroup =
        new RadioButtonGroupComponent(
            new RadioButtonComponent(
                panel.getRadioBtnAngemeldet(), AnmeldestatusSelected.ANGEMELDET.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnAbgemeldet(), AnmeldestatusSelected.ABGEMELDET.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnAnmeldestatusAlle(), AnmeldestatusSelected.ALLE.toString()));
    this.dispensationRadioButtonGroup =
        new RadioButtonGroupComponent(
            new RadioButtonComponent(
                panel.getRadioBtnDispensiert(), DispensationSelected.DISPENSIERT.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnNichtDispensiert(),
                DispensationSelected.NICHT_DISPENSIERT.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnDispensationAlle(), DispensationSelected.ALLE.toString()));
    this.stichtagWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtStichtag(), panel.getErrLblStichtag());
    this.schuelerCodeComboBox = panel.getComboBoxSchuelerCode();
    this.semesterKursSpinner = panel.getSpinnerSemesterKurs();
    this.wochentagComboBox = panel.getComboBoxWochentag();
    this.zeitBeginnWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtZeitBeginn(), panel.getErrLblZeitBeginn());
    this.lehrkraftComboBox = panel.getComboBoxLehrkraft();
    this.kursFuerSucheBeruecksichtigenCheckBox = panel.getCheckBoxKursFuerSucheBeruecksichtigen();
    this.maerchenSpinner = panel.getSpinnerMaerchen();
    this.gruppeComboBox = panel.getComboBoxGruppe();
    this.rollenWithErrorLabel =
        new TextAreaWithErrorLabelComponent(panel.getTxtAreaRollen(), panel.getErrLblRollen());
    this.elternmithilfeCodeComboBox = panel.getComboBoxElternmithilfeCode();
    this.kuchenVorstellungWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            panel.getTxtKuchenVorstellung(), panel.getErrLblKuchenVorstellung());
    this.zusatzattributMaerchenWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            panel.getTxtZusatzattributMaerchen(), panel.getErrLblZusatzattributMaerchen());
    this.maerchenFuerSucheBeruecksichtigenCheckBox =
        panel.getCheckBoxMaerchenFuerSucheBeruecksichtigen();
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

  // Strasse/Hausnummer
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

  // PLZ
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

  // E-Mail
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

  // Geburtsdatum Suchperiode
  public void setErrorLabelGeburtsdatumSuchperiodeVisible(String errorMessage) {
    geburtsdatumSuchperiodeWithErrorLabel.setErrorLabelVisible(true);
    geburtsdatumSuchperiodeWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelGeburtsdatumSuchperiodeInvisible() {
    geburtsdatumSuchperiodeWithErrorLabel.setErrorLabelVisible(false);
    geburtsdatumSuchperiodeWithErrorLabel.setToolTipText(null);
  }

  public void addTxtGeburtsdatumSuchperiodeActionListener(ActionListener actionListener) {
    geburtsdatumSuchperiodeWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtGeburtsdatumSuchperiodeFocusListener(FocusListener focusListener) {
    geburtsdatumSuchperiodeWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtGeburtsdatumSuchperiodeText() {
    return geburtsdatumSuchperiodeWithErrorLabel.getText();
  }

  public void setTxtGeburtsdatumSuchperiodeText(String text) {
    geburtsdatumSuchperiodeWithErrorLabel.setText(text);
  }

  // Geschlecht
  public void setRadioButtonGroupGeschlechtSelected(GeschlechtSelected geschlechtSelected) {
    geschlechtRadioButtonGroup.setSelected(geschlechtSelected.toString());
  }

  public GeschlechtSelected getRadioButtonGroupGeschlechtSelected() {
    String selected = geschlechtRadioButtonGroup.getSelected();
    return GeschlechtSelected.valueOf(selected);
  }

  // Rolle
  public void setRadioButtonGroupRolleSelected(RolleSelected rolleSelected) {
    rolleRadioButtonGroup.setSelected(rolleSelected.toString());
  }

  public RolleSelected getRadioButtonGroupRolleSelected() {
    String selected = rolleRadioButtonGroup.getSelected();
    return RolleSelected.valueOf(selected);
  }

  // Anmeldestatus
  public void setRadioButtonGroupAnmeldestatusSelected(
      AnmeldestatusSelected anmeldestatusSelected) {
    anmeldestatusRadioButtonGroup.setSelected(anmeldestatusSelected.toString());
  }

  public AnmeldestatusSelected getRadioButtonGroupAnmeldestatusSelected() {
    String selected = anmeldestatusRadioButtonGroup.getSelected();
    return AnmeldestatusSelected.valueOf(selected);
  }

  // Dispensation
  public void setRadioButtonGroupDispensationSelected(DispensationSelected dispensationSelected) {
    dispensationRadioButtonGroup.setSelected(dispensationSelected.toString());
  }

  public DispensationSelected getRadioButtonGroupDispensationSelected() {
    String selected = dispensationRadioButtonGroup.getSelected();
    return DispensationSelected.valueOf(selected);
  }

  // Stichtag
  public void setErrorLabelStichtagVisible(String errorMessage) {
    stichtagWithErrorLabel.setErrorLabelVisible(true);
    stichtagWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelStichtagInvisible() {
    stichtagWithErrorLabel.setErrorLabelVisible(false);
    stichtagWithErrorLabel.setToolTipText(null);
  }

  public void addTxtStichtagActionListener(ActionListener actionListener) {
    stichtagWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtStichtagFocusListener(FocusListener focusListener) {
    stichtagWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtStichtagText() {
    return stichtagWithErrorLabel.getText();
  }

  public void setTxtStichtagText(String text) {
    stichtagWithErrorLabel.setText(text);
  }

  // Schüler-Code
  public void setComboBoxSchuelerCodeValues(SchuelerCode[] values) {
    schuelerCodeComboBox.setModel(new DefaultComboBoxModel<>(values));
  }

  public SchuelerCode getComboBoxSchuelerCodeSelectedItem() {
    return (SchuelerCode) schuelerCodeComboBox.getSelectedItem();
  }

  public void setComboBoxSchuelerCodeSelectedItem(SchuelerCode schuelerCode) {
    schuelerCodeComboBox.setSelectedItem(schuelerCode);
  }

  // Semester/Kurs
  public void setSemesterKursSpinnerModelValues(List<Semester> semesterKursList) {
    SpinnerModel spinnerModelSemesterKursList = new SpinnerListModel(semesterKursList);
    semesterKursSpinner.setModel(spinnerModelSemesterKursList);
    if (semesterKursList.isEmpty()) {
      semesterKursSpinner.setEnabled(false);
    }
  }

  public Semester getSemesterKursSpinnerValue() {
    return (Semester) semesterKursSpinner.getValue();
  }

  public void setSemesterKursSpinnerValue(Object value) {
    semesterKursSpinner.setValue(value);
  }

  // Wochentag
  public void setComboBoxWochentagValues(Wochentag[] values) {
    wochentagComboBox.setModel(new DefaultComboBoxModel<>(values));
  }

  public Wochentag getComboBoxWochentagSelectedItem() {
    return (Wochentag) wochentagComboBox.getSelectedItem();
  }

  public void setComboBoxWochentagSelectedItem(Wochentag wochentag) {
    wochentagComboBox.setSelectedItem(wochentag);
  }

  // Zeit-Beginn
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

  // Lehrkraft
  public void setComboBoxLehrkraftValues(Mitarbeiter[] values) {
    lehrkraftComboBox.setModel(new DefaultComboBoxModel<>(values));
  }

  public Mitarbeiter getComboBoxLehrkraftSelectedItem() {
    return (Mitarbeiter) lehrkraftComboBox.getSelectedItem();
  }

  public void setComboBoxLehrkraftSelectedItem(Mitarbeiter lehrkraft) {
    lehrkraftComboBox.setSelectedItem(lehrkraft);
  }

  // Kurs für Suche berücksichtigen
  public boolean isCheckBoxKursFuerSucheBeruecksichtigenSelected() {
    return kursFuerSucheBeruecksichtigenCheckBox.isSelected();
  }

  public void setCheckBoxKursFuerSucheBeruecksichtigenSelected(boolean selected) {
    kursFuerSucheBeruecksichtigenCheckBox.setSelected(selected);
  }

  // Märchen
  public void setMaerchenSpinnerModelValues(List<Maerchen> maerchenList) {
    SpinnerModel spinnerModelMaerchenList = new SpinnerListModel(maerchenList);
    maerchenSpinner.setModel(spinnerModelMaerchenList);
    if (maerchenList.isEmpty()) {
      maerchenSpinner.setEnabled(false);
    }
  }

  public Maerchen getMaerchenSpinnerValue() {
    return (Maerchen) maerchenSpinner.getValue();
  }

  public void setMaerchenSpinnerValue(Object value) {
    maerchenSpinner.setValue(value);
  }

  // Gruppe
  public void setComboBoxGruppeValues(Gruppe[] values) {
    gruppeComboBox.setModel(new DefaultComboBoxModel<>(values));
  }

  public Gruppe getComboBoxGruppeSelectedItem() {
    return (Gruppe) gruppeComboBox.getSelectedItem();
  }

  public void setComboBoxGruppeSelectedItem(Gruppe gruppe) {
    gruppeComboBox.setSelectedItem(gruppe);
  }

  // Rollen
  public void setErrorLabelRollenVisible(String errorMessage) {
    rollenWithErrorLabel.setErrorLabelVisible(true);
    rollenWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelRollenInvisible() {
    rollenWithErrorLabel.setErrorLabelVisible(false);
    rollenWithErrorLabel.setToolTipText(null);
  }

  public void addTxtRollenFocusListener(FocusListener focusListener) {
    rollenWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtRollenText() {
    return rollenWithErrorLabel.getText();
  }

  public void setTxtRollenText(String text) {
    rollenWithErrorLabel.setText(text);
  }

  // Elternmithilfe-Code
  public void setComboBoxElternmithilfeCodeValues(ElternmithilfeCode[] values) {
    elternmithilfeCodeComboBox.setModel(new DefaultComboBoxModel<>(values));
  }

  public ElternmithilfeCode getComboBoxElternmithilfeCodeSelectedItem() {
    return (ElternmithilfeCode) elternmithilfeCodeComboBox.getSelectedItem();
  }

  public void setComboBoxElternmithilfeCodeSelectedItem(ElternmithilfeCode elternmithilfeCode) {
    elternmithilfeCodeComboBox.setSelectedItem(elternmithilfeCode);
  }

  // Kuchen Vorstellung
  public void setErrorLabelKuchenVorstellungVisible(String errorMessage) {
    kuchenVorstellungWithErrorLabel.setErrorLabelVisible(true);
    kuchenVorstellungWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelKuchenVorstellungInvisible() {
    kuchenVorstellungWithErrorLabel.setErrorLabelVisible(false);
    kuchenVorstellungWithErrorLabel.setToolTipText(null);
  }

  public void addTxtKuchenVorstellungActionListener(ActionListener actionListener) {
    kuchenVorstellungWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtKuchenVorstellungFocusListener(FocusListener focusListener) {
    kuchenVorstellungWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtKuchenVorstellungText() {
    return kuchenVorstellungWithErrorLabel.getText();
  }

  public void setTxtKuchenVorstellungText(String text) {
    kuchenVorstellungWithErrorLabel.setText(text);
  }

  // Zusatzattribut Märchen
  public void setErrorLabelZusatzattributMaerchenVisible(String errorMessage) {
    zusatzattributMaerchenWithErrorLabel.setErrorLabelVisible(true);
    zusatzattributMaerchenWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelZusatzattributMaerchenInvisible() {
    zusatzattributMaerchenWithErrorLabel.setErrorLabelVisible(false);
    zusatzattributMaerchenWithErrorLabel.setToolTipText(null);
  }

  public void addTxtZusatzattributMaerchenActionListener(ActionListener actionListener) {
    zusatzattributMaerchenWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtZusatzattributMaerchenFocusListener(FocusListener focusListener) {
    zusatzattributMaerchenWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtZusatzattributMaerchenText() {
    return zusatzattributMaerchenWithErrorLabel.getText();
  }

  public void setTxtZusatzattributMaerchenText(String text) {
    zusatzattributMaerchenWithErrorLabel.setText(text);
  }

  // Märchen für Suche berücksichtigen
  public boolean isCheckBoxMaerchenFuerSucheBeruecksichtigenSelected() {
    return maerchenFuerSucheBeruecksichtigenCheckBox.isSelected();
  }

  public void setCheckBoxMaerchenFuerSucheBeruecksichtigenSelected(boolean selected) {
    maerchenFuerSucheBeruecksichtigenCheckBox.setSelected(selected);
  }
}
