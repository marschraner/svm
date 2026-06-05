package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.conversion.IntegerConverter;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import java.util.ArrayList;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record MaerchenFields(String schuljahr, String bezeichnung, String anzahlVorstellungen) {

  public static MaerchenFields of(Maerchen entity) {
    if (entity == null) return null;

    return new MaerchenFields(
        entity.getSchuljahr(),
        entity.getBezeichnung(),
        IntegerConverter.toString(entity.getAnzahlVorstellungen()));
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedMaerchenFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Integer> anzahlVorstellungen =
        IntegerConverter.convertToInt("anzahlVorstellungen", anzahlVorstellungen());
    if (!anzahlVorstellungen.isValid()) conversionErrors.add(anzahlVorstellungen);

    ConvertedMaerchenFields convertedMaerchenFields =
        new ConvertedMaerchenFields(
            schuljahr(), bezeichnung(), anzahlVorstellungen.convertedValue());

    return new ConvertedFieldsAndConversionResults<>(convertedMaerchenFields, conversionErrors);
  }
}
