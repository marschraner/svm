package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record SemesterFields(
    String schuljahr,
    Semesterbezeichnung semesterbezeichnung,
    String semesterbeginn,
    String semesterende,
    String ferienbeginn1,
    String ferienende1,
    String ferienbeginn2,
    String ferienende2) {

  public static SemesterFields of(Semester entity) {
    if (entity == null) return null;

    return new SemesterFields(
        entity.getSchuljahr(),
        entity.getSemesterbezeichnung(),
        CalendarConverter.toString(entity.getSemesterbeginn()),
        CalendarConverter.toString(entity.getSemesterende()),
        CalendarConverter.toString(entity.getFerienbeginn1()),
        CalendarConverter.toString(entity.getFerienende1()),
        CalendarConverter.toString(entity.getFerienbeginn2()),
        CalendarConverter.toString(entity.getFerienende2()));
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedSemesterFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Calendar> semesterbeginn =
        CalendarConverter.convertToCalendar("semesterbeginn", semesterbeginn());
    if (!semesterbeginn.isValid()) conversionErrors.add(semesterbeginn);
    ConversionResult<Calendar> semesterende =
        CalendarConverter.convertToCalendar("semesterende", semesterende());
    if (!semesterende.isValid()) conversionErrors.add(semesterende);
    ConversionResult<Calendar> ferienbeginn1 =
        CalendarConverter.convertToCalendar("ferienbeginn1", ferienbeginn1());
    if (!ferienbeginn1.isValid()) conversionErrors.add(ferienbeginn1);
    ConversionResult<Calendar> ferienende1 =
        CalendarConverter.convertToCalendar("ferienende1", ferienende1());
    if (!ferienende1.isValid()) conversionErrors.add(ferienende1);
    ConversionResult<Calendar> ferienbeginn2 =
        CalendarConverter.convertToCalendar("ferienbeginn2", ferienbeginn2());
    if (!ferienbeginn2.isValid()) conversionErrors.add(ferienbeginn2);
    ConversionResult<Calendar> ferienende2 =
        CalendarConverter.convertToCalendar("ferienende2", ferienende2());
    if (!ferienende2.isValid()) conversionErrors.add(ferienende2);

    ConvertedSemesterFields convertedSemesterFields =
        new ConvertedSemesterFields(
            schuljahr(),
            semesterbezeichnung(),
            semesterbeginn.convertedValue(),
            semesterende.convertedValue(),
            ferienbeginn1.convertedValue(),
            ferienende1.convertedValue(),
            ferienbeginn2.convertedValue(),
            ferienende2.convertedValue());

    return new ConvertedFieldsAndConversionResults<>(convertedSemesterFields, conversionErrors);
  }
}
