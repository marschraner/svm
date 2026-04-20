package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record AnmeldungFields(String anmeldedatum, String abmeldedatum) {

  public static AnmeldungFields of(Anmeldung entity) {
    if (entity == null) return null;

    return new AnmeldungFields(
        CalendarConverter.toString(entity.getAnmeldedatum()),
        CalendarConverter.toString(entity.getAbmeldedatum()));
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedAnmeldungFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Calendar> anmeldedatum =
        CalendarConverter.convertToCalendar("anmeldedatum", anmeldedatum());
    if (!anmeldedatum.isValid()) conversionErrors.add(anmeldedatum);
    ConversionResult<Calendar> abmeldedatum =
        CalendarConverter.convertToCalendar("abmeldedatum", abmeldedatum());
    if (!abmeldedatum.isValid()) conversionErrors.add(abmeldedatum);

    ConvertedAnmeldungFields convertedAnmeldungFields =
        new ConvertedAnmeldungFields(anmeldedatum.convertedValue(), abmeldedatum.convertedValue());

    return new ConvertedFieldsAndConversionResults<>(convertedAnmeldungFields, conversionErrors);
  }
}
