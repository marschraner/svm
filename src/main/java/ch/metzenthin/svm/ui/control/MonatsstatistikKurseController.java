package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.domain.model.MonatsstatistikKurseModel.MONAT_JAHR_DATE_FORMAT_STRING;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.MonatsstatistikKurse;
import ch.metzenthin.svm.domain.model.MonatsstatistikKurseModel;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndServiceReturnValue;
import ch.metzenthin.svm.ui.view.MonatsstatistikKurseView;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikKurseController
    extends AbstractSubmitController<MonatsstatistikKurseView> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(MonatsstatistikKurseController.class);

  private final MonatsstatistikKurseModel model;

  public MonatsstatistikKurseController(
      MonatsstatistikKurseModel monatsstatistikKurseModel, ActionListener closeListener) {
    super(createView(closeListener));
    this.model = monatsstatistikKurseModel;
    configTxtMonatJahr();
    configBtnSuchen();
    configBtnAbbrechen();
    initialiseViewFields();
  }

  private static MonatsstatistikKurseView createView(ActionListener closeListener) {
    return new MonatsstatistikKurseView(closeListener);
  }

  private void configTxtMonatJahr() {
    view.addTxtMonatJahrActionListener(e -> onMonatJahrEvent());
    view.addTxtMonatJahrFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onMonatJahrEvent();
          }
        });
  }

  private void onMonatJahrEvent() {
    LOGGER.trace("MonatsstatistikKurseController Event MonatJahr");
    formatConvertAndValidateDate(
        view.getTxtMonatJahrText(),
        MONAT_JAHR_DATE_FORMAT_STRING,
        model::validateMonatJahr,
        view::setTxtMonatJahrText,
        view::setErrorLabelMonatJahrVisible,
        view::setErrorLabelMonatJahrInvisible);
  }

  protected void configBtnSuchen() {
    view.addButtonSubmitActionListener(e -> onSuchen());
  }

  private void onSuchen() {
    setAllErrorLabelsInvisible();
    ValidationResultsAndServiceReturnValue<MonatsstatistikKurse>
        validationResultsAndServiceReturnValue =
            model.calculateMonatsstatistik(view.getTxtMonatJahrText());
    if (!validationResultsAndServiceReturnValue.isValidationSuccessful()) {
      setErrorLabelsVisible(validationResultsAndServiceReturnValue.validationResults());
      showErrorMessageDialog(validationResultsAndServiceReturnValue.validationResults());
    } else {
      showMonatsstatistik(validationResultsAndServiceReturnValue.serviceReturnValue());
    }
  }

  private void showMonatsstatistik(MonatsstatistikKurse monatsstatistikKurse) {
    String statistik =
        "Anmeldungen: "
            + monatsstatistikKurse.numberOfAnmeldungen()
            + "\nAbmeldungen: "
            + monatsstatistikKurse.numberOfAbmeldungen()
            + "\nBesuchte Lektionen: "
            + monatsstatistikKurse.numberOfLektionen();
    view.showInfoMessageDialog(statistik, "Statistik per Ende Monat");
  }

  private void configBtnAbbrechen() {
    view.addButtonAbbrechenActionListener(e -> view.abbrechen());
  }

  private void initialiseViewFields() {
    Calendar monatJahrInit = new GregorianCalendar();
    monatJahrInit.add(Calendar.MONTH, -1);
    view.setTxtMonatJahrText(asString(monatJahrInit, MONAT_JAHR_DATE_FORMAT_STRING));
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    if (field == Field.MONAT_JAHR) {
      setErrorLabelVisibleIfRequired(validationResult, field, view::setErrorLabelMonatJahrVisible);
    } else {
      throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  protected void setAllErrorLabelsInvisible() {
    view.setErrorLabelMonatJahrInvisible();
  }

  public MonatsstatistikKurseView getView() {
    return view;
  }
}
