package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.conversion.BigDecimalConverter;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConvertedValueAndConversionResult;
import ch.metzenthin.svm.domain.model.conversion.IntegerConverter;
import ch.metzenthin.svm.domain.model.conversion.TimeConverter;
import ch.metzenthin.svm.domain.model.formatting.FormattingUtils;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.ui.view.AbstractView;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author Martin Schraner
 */
public abstract class AbstractSubmitController<T extends AbstractView<?>> {

  T view;

  protected AbstractSubmitController(T view) {
    this.view = view;
  }

  protected void setErrorLabelsVisible(List<ValidationResult> validationResults) {
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

  protected void showErrorMessageDialog(List<ValidationResult> validationResults) {
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
    setAndValidateFormattedStringValue(
        validateFieldFunction,
        setFieldConsumer,
        setErrorLabelVisibleConsumer,
        setErrorLabelInvisibleRunnable,
        formattedFieldValue);
  }

  private static void setAndValidateFormattedStringValue(
      Function<String, ValidationResult> validateFieldFunction,
      Consumer<String> setFieldConsumer,
      Consumer<String> setErrorLabelVisibleConsumer,
      Runnable setErrorLabelInvisibleRunnable,
      String formattedFieldValue) {
    setFieldConsumer.accept(formattedFieldValue);
    ValidationResult validationResult = validateFieldFunction.apply(formattedFieldValue);
    if (validationResult.isValid()) {
      setErrorLabelInvisibleRunnable.run();
    } else {
      setErrorLabelVisibleConsumer.accept(validationResult.errorMessage());
    }
  }

  protected static void formatAndValidateString(
      String fieldValue,
      UnaryOperator<String> formatFunction,
      Function<String, ValidationResult> validateFieldFunction,
      Consumer<String> setFieldConsumer,
      Consumer<String> setErrorLabelVisibleConsumer,
      Runnable setErrorLabelInvisibleRunnable) {
    String formattedFieldValue = formatFunction.apply(FormattingUtils.formatString(fieldValue));
    setAndValidateFormattedStringValue(
        validateFieldFunction,
        setFieldConsumer,
        setErrorLabelVisibleConsumer,
        setErrorLabelInvisibleRunnable,
        formattedFieldValue);
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
}
