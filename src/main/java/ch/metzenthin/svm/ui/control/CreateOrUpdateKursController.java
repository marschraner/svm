package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKursModel;
import ch.metzenthin.svm.domain.model.entityfields.KursFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateKursView;
import java.awt.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKursController
    extends AbstractSubmitDialogController<CreateOrUpdateKursView> {

  private static final String FEHLER = "Fehler";

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateOrUpdateKursController.class);

  private final CreateOrUpdateKursModel model;

  public CreateOrUpdateKursController(
      CreateOrUpdateKursModel createOrUpdateKursModel, String title) {
    super(createView(title));
    model = createOrUpdateKursModel;
    configComboBoxKurstyp();
    configTxtAltersbereich();
    configTxtStufe();
    configComboBoxWochentag();
    configTxtZeitBeginn();
    configTxtZeitEnde();
    configComboBoxKursort();
    configComboBoxLehrkraft1();
    configComboBoxLehrkraft2();
    configTxtBemerkungen();
    // Keine Konfiguration für Field Bemerkungen
    initialiseViewFields();
  }

  private static CreateOrUpdateKursView createView(String title) {
    return new CreateOrUpdateKursView(title);
  }

  private void configComboBoxKurstyp() {
    view.setComboBoxKurstypValues(model.getSelectableKurstypen());
    // ComboBox auf leeren Wert setzen
    view.setComboBoxKurstypSelectedItem(null);
    view.addComboBoxKurstypActionListener(e -> onKurstypSelected());
  }

  private void onKurstypSelected() {
    LOGGER.trace(
        "CreateOrUpdateSemesterController Event Kurstyp selected={}",
        view.getComboBoxKurstypSelectedItem());
    ValidationResult validationResult =
        model.validateKurstyp(view.getComboBoxKurstypSelectedItem());
    if (!validationResult.isValid()) {
      view.showErrorMessageDialog(validationResult.errorMessage(), FEHLER);
    }
  }

  private void configTxtAltersbereich() {
    view.addTxtAltersbereichActionListener(e -> onAltersbereichEvent());
    view.addTxtAltersbereichFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onAltersbereichEvent();
          }
        });
  }

  private void onAltersbereichEvent() {
    LOGGER.trace("CreateOrUpdateKursController Event Altersbereich");
    formatAndValidateString(
        view.getTxtAltersbereichText(),
        model::validateAltersbereich,
        view::setTxtAltersbereichText,
        view::setErrorLabelAltersbereichVisible,
        view::setErrorLabelAltersbereichInvisible);
  }

  private void configTxtStufe() {
    view.addTxtStufeActionListener(e -> onStufeEvent());
    view.addTxtStufeFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onStufeEvent();
          }
        });
  }

  private void onStufeEvent() {
    LOGGER.trace("CreateOrUpdateKursController Event Stufe");
    formatAndValidateString(
        view.getTxtStufeText(),
        model::validateStufe,
        view::setTxtStufeText,
        view::setErrorLabelStufeVisible,
        view::setErrorLabelStufeInvisible);
  }

  private void configComboBoxWochentag() {
    view.setComboBoxWochentagValues(Wochentag.getAllowedWochentageForKurs());
    // ComboBox auf leeren Wert setzen
    view.setComboBoxWochentagSelectedItem(null);
    view.addComboBoxWochentagActionListener(e -> onWochentagSelected());
  }

  private void onWochentagSelected() {
    LOGGER.trace(
        "CreateOrUpdateSemesterController Event Wochentag selected={}",
        view.getComboBoxWochentagSelectedItem());
    ValidationResult validationResult =
        model.validateWochentag(view.getComboBoxWochentagSelectedItem());
    if (!validationResult.isValid()) {
      view.showErrorMessageDialog(validationResult.errorMessage(), FEHLER);
    }
  }

  private void configTxtZeitBeginn() {
    view.addTxtZeitBeginnActionListener(e -> onZeitBeginnEvent());
    view.addTxtZeitBeginnFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onZeitBeginnEvent();
          }
        });
  }

  private void onZeitBeginnEvent() {
    LOGGER.trace("CreateOrUpdateKursController Event ZeitBeginn");
    formatConvertAndValidateTime(
        view.getTxtZeitBeginnText(),
        model::validateZeitBeginn,
        view::setTxtZeitBeginnText,
        view::setErrorLabelZeitBeginnVisible,
        view::setErrorLabelZeitBeginnInvisible);
  }

  private void configTxtZeitEnde() {
    view.addTxtZeitEndeActionListener(e -> onZeitEndeEvent());
    view.addTxtZeitEndeFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onZeitEndeEvent();
          }
        });
  }

  private void onZeitEndeEvent() {
    LOGGER.trace("CreateOrUpdateKursController Event ZeitEnde");
    formatConvertAndValidateTime(
        view.getTxtZeitEndeText(),
        model::validateZeitEnde,
        view::setTxtZeitEndeText,
        view::setErrorLabelZeitEndeVisible,
        view::setErrorLabelZeitEndeInvisible);
  }

  private void configComboBoxKursort() {
    view.setComboBoxKursortValues(model.getSelectableKursorte());
    // ComboBox auf leeren Wert setzen
    view.setComboBoxKursortSelectedItem(null);
    view.addComboBoxKursortActionListener(e -> onKursortSelected());
  }

  private void onKursortSelected() {
    LOGGER.trace(
        "CreateOrUpdateSemesterController Event Kursort selected={}",
        view.getComboBoxKursortSelectedItem());
    ValidationResult validationResult =
        model.validateKursort(view.getComboBoxKursortSelectedItem());
    if (!validationResult.isValid()) {
      view.showErrorMessageDialog(validationResult.errorMessage(), FEHLER);
    }
  }

  private void configComboBoxLehrkraft1() {
    view.setComboBoxLehrkraft1Values(model.getSelectableLehrkraefte1());
    // ComboBox auf leeren Wert setzen
    view.setComboBoxLehrkraft1SelectedItem(null);
    view.addComboBoxLehrkraft1ActionListener(e -> onLehrkraft1Selected());
  }

  private void onLehrkraft1Selected() {
    LOGGER.trace(
        "CreateOrUpdateSemesterController Event Lehrkraft1 selected={}",
        view.getComboBoxLehrkraft1SelectedItem());
    ValidationResult validationResult =
        model.validateLehrkraft1(view.getComboBoxLehrkraft1SelectedItem());
    if (!validationResult.isValid()) {
      view.showErrorMessageDialog(validationResult.errorMessage(), FEHLER);
    }
  }

  private void configComboBoxLehrkraft2() {
    view.setComboBoxLehrkraft2Values(model.getSelectableLehrkraefte2());
  }

  // Kein Event für Benutzereingaben für Field Lehrkraft2

  private void configTxtBemerkungen() {
    view.addTxtBemerkungenActionListener(e -> onBemerkungenEvent());
    view.addTxtBemerkungenFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBemerkungenEvent();
          }
        });
  }

  private void onBemerkungenEvent() {
    LOGGER.trace("CreateOrUpdateKursController Event Bemerkungen");
    formatAndValidateString(
        view.getTxtBemerkungenText(),
        model::validateBemerkungen,
        view::setTxtBemerkungenText,
        view::setErrorLabelBemerkungenVisible,
        view::setErrorLabelBemerkungenInvisible);
  }

  private void initialiseViewFields() {
    if (!model.isNeu()) {
      KursFields kursFields = model.getKursFields();
      view.setComboBoxKurstypSelectedItem(model.getKurstyp());
      view.setTxtAltersbereichText(kursFields.altersbereich());
      view.setTxtStufeText(kursFields.stufe());
      view.setComboBoxWochentagSelectedItem(kursFields.wochentag());
      view.setTxtZeitBeginnText(kursFields.zeitBeginn());
      view.setTxtZeitEndeText(kursFields.zeitEnde());
      view.setComboBoxKursortSelectedItem(model.getKursort());
      view.setComboBoxLehrkraft1SelectedItem(model.getLehrkraft1());
      view.setComboBoxLehrkraft2SelectedItem(model.getLehrkraft2());
      view.setTxtBemerkungenText(kursFields.bemerkungen());
    }
  }

  @Override
  protected ValidationResultsAndSubmitResult submit() {
    KursFields kursFields =
        new KursFields(
            view.getTxtAltersbereichText(),
            view.getTxtStufeText(),
            view.getComboBoxWochentagSelectedItem(),
            view.getTxtZeitBeginnText(),
            view.getTxtZeitEndeText(),
            view.getTxtBemerkungenText());

    return model.speichern(
        kursFields,
        view.getComboBoxKurstypSelectedItem(),
        view.getComboBoxKursortSelectedItem(),
        view.getComboBoxLehrkraft1SelectedItem(),
        view.getComboBoxLehrkraft2SelectedItem());
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    switch (field) {
      case KURSTYP ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelKurstypVisible);
      case ALTERSBEREICH ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelAltersbereichVisible);
      case STUFE ->
          setErrorLabelVisibleIfRequired(validationResult, field, view::setErrorLabelStufeVisible);
      case WOCHENTAG ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelWochentagVisible);
      case ZEIT_BEGINN ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelZeitBeginnVisible);
      case ZEIT_ENDE ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelZeitEndeVisible);
      case KURSORT ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelKursortVisible);
      case LEHRKRAFT1 ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelLehrkraft1Visible);
      case LEHRKRAFT2 ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelLehrkraft2Visible);
      case BEMERKUNGEN ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelBemerkungenVisible);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  public void setAllErrorLabelsInvisible() {
    view.setErrorLabelKurstypInvisible();
    view.setErrorLabelAltersbereichInvisible();
    view.setErrorLabelStufeInvisible();
    view.setErrorLabelWochentagInvisible();
    view.setErrorLabelZeitBeginnInvisible();
    view.setErrorLabelZeitEndeInvisible();
    view.setErrorLabelKursortInvisible();
    view.setErrorLabelLehrkraft1Invisible();
    view.setErrorLabelLehrkraft2Invisible();
    // Keine Validierung für Field Bemerkungen
  }
}
