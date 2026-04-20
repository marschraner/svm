package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.domain.model.conversion.BooleanConverter;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record AngehoerigerFields(
    String wuenschtEmails,
    Anrede anrede,
    String vorname,
    String nachname,
    String geburtsdatum,
    String festnetz,
    String natel,
    String email) {

  public static AngehoerigerFields of(Angehoeriger entity) {
    if (entity == null) return null;

    return new AngehoerigerFields(
        BooleanConverter.toString(entity.getWuenschtEmails()),
        entity.getAnrede(),
        entity.getVorname(),
        entity.getNachname(),
        CalendarConverter.toString(entity.getGeburtsdatum()),
        entity.getFestnetz(),
        entity.getNatel(),
        entity.getEmail());
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedAngehoerigerFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Boolean> wuenschtEmails =
        BooleanConverter.convertToBoolean("wuenschtEmails", wuenschtEmails());
    if (!wuenschtEmails.isValid()) conversionErrors.add(wuenschtEmails);
    ConversionResult<Calendar> geburtsdatum =
        CalendarConverter.convertToCalendar("geburtsdatum", geburtsdatum());
    if (!geburtsdatum.isValid()) conversionErrors.add(geburtsdatum);

    ConvertedAngehoerigerFields convertedAngehoerigerFields =
        new ConvertedAngehoerigerFields(
            wuenschtEmails.convertedValue(),
            anrede(),
            vorname(),
            nachname(),
            geburtsdatum.convertedValue(),
            festnetz(),
            natel(),
            email());

    return new ConvertedFieldsAndConversionResults<>(convertedAngehoerigerFields, conversionErrors);
  }
}
