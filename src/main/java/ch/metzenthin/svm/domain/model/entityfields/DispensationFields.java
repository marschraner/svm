package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record DispensationFields(
    String dispensationsbeginn,
    String dispensationsende,
    String voraussichtlicheDauer,
    String grund) {

  public static DispensationFields of(Dispensation entity) {
    if (entity == null) return null;

    return new DispensationFields(
        CalendarConverter.toString(entity.getDispensationsbeginn()),
        CalendarConverter.toString(entity.getDispensationsende()),
        entity.getVoraussichtlicheDauer(),
        entity.getGrund());
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedDispensationFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Calendar> dispensationsbeginn =
        CalendarConverter.convertToCalendar("dispensationsbeginn", dispensationsbeginn());
    if (!dispensationsbeginn.isValid()) conversionErrors.add(dispensationsbeginn);
    ConversionResult<Calendar> dispensationsende =
        CalendarConverter.convertToCalendar("dispensationsende", dispensationsende());
    if (!dispensationsende.isValid()) conversionErrors.add(dispensationsende);

    ConvertedDispensationFields convertedDispensationFields =
        new ConvertedDispensationFields(
            dispensationsbeginn.convertedValue(),
            dispensationsende.convertedValue(),
            voraussichtlicheDauer(),
            grund());

    return new ConvertedFieldsAndConversionResults<>(convertedDispensationFields, conversionErrors);
  }
}
