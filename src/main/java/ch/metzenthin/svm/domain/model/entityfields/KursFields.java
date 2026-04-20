package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.conversion.TimeConverter;
import ch.metzenthin.svm.persistence.entities.Kurs;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record KursFields(
    String altersbereich,
    String stufe,
    Wochentag wochentag,
    String zeitBeginn,
    String zeitEnde,
    String bemerkungen) {

  public static KursFields of(Kurs entity) {
    if (entity == null) return null;

    return new KursFields(
        entity.getAltersbereich(),
        entity.getStufe(),
        entity.getWochentag(),
        TimeConverter.toString(entity.getZeitBeginn()),
        TimeConverter.toString(entity.getZeitEnde()),
        entity.getBemerkungen());
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedKursFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Time> zeitBeginn = TimeConverter.convertToTime("zeitBeginn", zeitBeginn());
    if (!zeitBeginn.isValid()) conversionErrors.add(zeitBeginn);
    ConversionResult<Time> zeitEnde = TimeConverter.convertToTime("zeitEnde", zeitEnde());
    if (!zeitEnde.isValid()) conversionErrors.add(zeitEnde);

    ConvertedKursFields convertedKursFields =
        new ConvertedKursFields(
            altersbereich(),
            stufe(),
            wochentag(),
            zeitBeginn.convertedValue(),
            zeitEnde.convertedValue(),
            bemerkungen());

    return new ConvertedFieldsAndConversionResults<>(convertedKursFields, conversionErrors);
  }
}
