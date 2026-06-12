package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.DialogClosingListener;
import ch.metzenthin.svm.domain.model.conversion.BigDecimalConverter;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConvertedValueAndConversionResult;
import ch.metzenthin.svm.domain.model.conversion.IntegerConverter;
import ch.metzenthin.svm.domain.model.conversion.TimeConverter;
import ch.metzenthin.svm.domain.model.formatting.FormattingUtils;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateView;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Martin Schraner
 */
public abstract class AbstractCreateOrUpdateController<T extends CreateOrUpdateView>
    implements DialogClosingListener {

  protected final T view;

  protected AbstractCreateOrUpdateController(T view) {
    this.view = view;
    view.configDialogClosing(this);
    configBtnSpeichern();
    configBtnAbbrechen();
  }

  protected void configBtnSpeichern() {
    view.addButtonSpeichernActionListener(e -> onSpeichern());
  }

  private void onSpeichern() {
    setAllErrorLabelsInvisible();
    ValidationResultsAndSaveResult validationResultsAndSaveResult = speichern();
    if (!validationResultsAndSaveResult.isValidationSuccessful()) {
      setErrorLabelsVisible(validationResultsAndSaveResult.validationResults());
      showErrorMessageDialog(validationResultsAndSaveResult.validationResults());
    } else if (!validationResultsAndSaveResult.isSaveSuccessful()) {
      view.showErrorMessageDialog(validationResultsAndSaveResult.getSaveErrorMessage(), "Fehler");
      if (validationResultsAndSaveResult.isDialogToBeClosedAfterSave()) {
        closeDialog();
      } else {
        view.setButtonSpeichernFocusPainted(false);
      }
    } else {
      if (validationResultsAndSaveResult.isDialogToBeClosedAfterSave()) {
        closeDialog();
      }
    }
  }

  protected abstract ValidationResultsAndSaveResult speichern();

  private void setErrorLabelsVisible(List<ValidationResult> validationResults) {
    for (ValidationResult validationResult : validationResults) {
      if (!validationResult.isValid() && validationResult.affectedFields() != null) {
        for (Field field : validationResult.affectedFields()) {
          setErrorLabelVisible(validationResult, field);
        }
      }
    }
  }

  protected abstract void setErrorLabelVisible(ValidationResult validationResult, Field field);

  protected abstract void setAllErrorLabelsInvisible();

  protected void setErrorLabelVisibleIfRequired(
      ValidationResult validationResult, Field field, Consumer<String> setErrorLabelVisible) {
    if (validationResult.affectedFields().contains(field)) {
      setErrorLabelVisible.accept(validationResult.errorMessage());
    }
  }

  private void showErrorMessageDialog(List<ValidationResult> validationResults) {
    for (ValidationResult validationResult : validationResults) {
      if (!validationResult.isValid()
          && (validationResult.affectedFields() == null
              || validationResult.affectedFields().isEmpty())) {
        view.showErrorMessageDialog(validationResult.errorMessage(), "Fehler");
      }
    }
  }

  protected static void formatAndValidateString(
      String fieldValue,
      Function<String, ValidationResult> validateFieldFunction,
      Consumer<String> setFieldConsumer,
      Consumer<String> setErrorLabelVisibleConsumer,
      Runnable setErrorLabelInvisibleRunnable) {
    String formattedFieldValue = FormattingUtils.formatString(fieldValue);
    setFieldConsumer.accept(formattedFieldValue);
    ValidationResult validationResult = validateFieldFunction.apply(formattedFieldValue);
    if (validationResult.isValid()) {
      setErrorLabelInvisibleRunnable.run();
    } else {
      setErrorLabelVisibleConsumer.accept(validationResult.errorMessage());
    }
  }

  protected static void formatConvertAndValidateDate(
      String fieldValue,
      Function<Calendar, ValidationResult> validateFieldFunction,
      Consumer<String> setFieldConsumer,
      Consumer<String> setErrorLabelVisibleConsumer,
      Runnable setErrorLabelInvisibleRunnable) {
    String formattedFieldValue = FormattingUtils.formatAsDate(fieldValue);
    setFieldConsumer.accept(formattedFieldValue);
    ConvertedValueAndConversionResult<Calendar> convertedFieldValueAndConversionResult =
        CalendarConverter.toCalendar(formattedFieldValue);
    validate(
        validateFieldFunction,
        setErrorLabelVisibleConsumer,
        setErrorLabelInvisibleRunnable,
        convertedFieldValueAndConversionResult);
  }

  private static <T> void validate(
      Function<T, ValidationResult> validateFieldFunction,
      Consumer<String> setErrorLabelVisibleConsumer,
      Runnable setErrorLabelInvisibleRunnable,
      ConvertedValueAndConversionResult<T> convertedFieldValueAndConversionResult) {
    if (!convertedFieldValueAndConversionResult.isValid()) {
      setErrorLabelVisibleConsumer.accept(convertedFieldValueAndConversionResult.errorMessage());
    } else {
      ValidationResult validationResult =
          validateFieldFunction.apply(convertedFieldValueAndConversionResult.convertedValue());
      if (validationResult.isValid()) {
        setErrorLabelInvisibleRunnable.run();
      } else {
        setErrorLabelVisibleConsumer.accept(validationResult.errorMessage());
      }
    }
  }

  protected static void formatConvertAndValidateTime(
      String fieldValue,
      Function<Time, ValidationResult> validateFieldFunction,
      Consumer<String> setFieldConsumer,
      Consumer<String> setErrorLabelVisibleConsumer,
      Runnable setErrorLabelInvisibleRunnable) {
    String formattedFieldValue = FormattingUtils.formatString(fieldValue);
    setFieldConsumer.accept(formattedFieldValue);
    ConvertedValueAndConversionResult<Time> convertedFieldValueAndConversionResult =
        TimeConverter.toTime(formattedFieldValue);
    validate(
        validateFieldFunction,
        setErrorLabelVisibleConsumer,
        setErrorLabelInvisibleRunnable,
        convertedFieldValueAndConversionResult);
  }

  protected static void formatConvertAndValidatePrice(
      String fieldValue,
      Function<BigDecimal, ValidationResult> validateFieldFunction,
      Consumer<String> setFieldConsumer,
      Consumer<String> setErrorLabelVisibleConsumer,
      Runnable setErrorLabelInvisibleRunnable) {
    String formattedFieldValue = FormattingUtils.formatAsPrice(fieldValue);
    setFieldConsumer.accept(formattedFieldValue);
    ConvertedValueAndConversionResult<BigDecimal> convertedFieldValueAndConversionResult =
        BigDecimalConverter.convertToBigDecimal(formattedFieldValue);
    validate(
        validateFieldFunction,
        setErrorLabelVisibleConsumer,
        setErrorLabelInvisibleRunnable,
        convertedFieldValueAndConversionResult);
  }

  protected static void formatConvertAndValidateInt(
      String fieldValue,
      Function<Integer, ValidationResult> validateFieldFunction,
      Consumer<String> setFieldConsumer,
      Consumer<String> setErrorLabelVisibleConsumer,
      Runnable setErrorLabelInvisibleRunnable) {
    String formattedFieldValue = FormattingUtils.formatString(fieldValue);
    setFieldConsumer.accept(formattedFieldValue);
    ConvertedValueAndConversionResult<Integer> convertedFieldValueAndConversionResult =
        IntegerConverter.convertToInt(formattedFieldValue);
    validate(
        validateFieldFunction,
        setErrorLabelVisibleConsumer,
        setErrorLabelInvisibleRunnable,
        convertedFieldValueAndConversionResult);
  }

  private void closeDialog() {
    view.closeDialog();
  }

  private void configBtnAbbrechen() {
    view.addButtonAbbrechenActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    closeDialog();
  }

  @Override
  public void onCloseDialog() {
    closeDialog();
  }

  protected void showDialog() {
    view.showDialog();
  }
}
