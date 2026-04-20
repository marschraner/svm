package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record KursanmeldungFields(String anmeldedatum, String abmeldedatum, String bemerkungen) {

  public static KursanmeldungFields of(Kursanmeldung entity) {
    if (entity == null) return null;

    return new KursanmeldungFields(
        CalendarConverter.toString(entity.getAnmeldedatum()),
        CalendarConverter.toString(entity.getAbmeldedatum()),
        entity.getBemerkungen());
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedKursanmeldungFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Calendar> anmeldedatum =
        CalendarConverter.convertToCalendar("anmeldedatum", anmeldedatum());
    if (!anmeldedatum.isValid()) conversionErrors.add(anmeldedatum);
    ConversionResult<Calendar> abmeldedatum =
        CalendarConverter.convertToCalendar("abmeldedatum", abmeldedatum());
    if (!abmeldedatum.isValid()) conversionErrors.add(abmeldedatum);

    ConvertedKursanmeldungFields convertedKursanmeldungFields =
        new ConvertedKursanmeldungFields(
            anmeldedatum.convertedValue(), abmeldedatum.convertedValue(), bemerkungen());

    return new ConvertedFieldsAndConversionResults<>(
        convertedKursanmeldungFields, conversionErrors);
  }
}
