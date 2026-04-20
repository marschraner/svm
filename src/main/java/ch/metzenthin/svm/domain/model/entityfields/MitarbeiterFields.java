package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record MitarbeiterFields(
    String ahvNummer,
    String ibanNummer,
    boolean lehrkraft,
    String vertretungsmoeglichkeiten,
    String bemerkungen,
    boolean aktiv,
    Anrede anrede,
    String vorname,
    String nachname,
    String geburtsdatum,
    String festnetz,
    String natel,
    String email) {

  public static MitarbeiterFields of(Mitarbeiter entity) {
    if (entity == null) return null;

    return new MitarbeiterFields(
        entity.getAhvNummer(),
        entity.getIbanNummer(),
        entity.isLehrkraft(),
        entity.getVertretungsmoeglichkeiten(),
        entity.getBemerkungen(),
        entity.isAktiv(),
        entity.getAnrede(),
        entity.getVorname(),
        entity.getNachname(),
        CalendarConverter.toString(entity.getGeburtsdatum()),
        entity.getFestnetz(),
        entity.getNatel(),
        entity.getEmail());
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedMitarbeiterFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Calendar> geburtsdatum =
        CalendarConverter.convertToCalendar("geburtsdatum", geburtsdatum());
    if (!geburtsdatum.isValid()) conversionErrors.add(geburtsdatum);

    ConvertedMitarbeiterFields convertedMitarbeiterFields =
        new ConvertedMitarbeiterFields(
            ahvNummer(),
            ibanNummer(),
            lehrkraft(),
            vertretungsmoeglichkeiten(),
            bemerkungen(),
            aktiv(),
            anrede(),
            vorname(),
            nachname(),
            geburtsdatum.convertedValue(),
            festnetz(),
            natel(),
            email());

    return new ConvertedFieldsAndConversionResults<>(convertedMitarbeiterFields, conversionErrors);
  }
}
