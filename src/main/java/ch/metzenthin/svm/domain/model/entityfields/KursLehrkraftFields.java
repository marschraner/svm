package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.conversion.IntegerConverter;
import ch.metzenthin.svm.persistence.entities.KursLehrkraft;
import java.util.ArrayList;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record KursLehrkraftFields(String lehrkraefteOrder) {

  public static KursLehrkraftFields of(KursLehrkraft entity) {
    if (entity == null) return null;

    return new KursLehrkraftFields(IntegerConverter.toString(entity.getLehrkraefteOrder()));
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedKursLehrkraftFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Integer> lehrkraefteOrder =
        IntegerConverter.convertToInt("lehrkraefteOrder", lehrkraefteOrder());
    if (!lehrkraefteOrder.isValid()) conversionErrors.add(lehrkraefteOrder);

    ConvertedKursLehrkraftFields convertedKursLehrkraftFields =
        new ConvertedKursLehrkraftFields(lehrkraefteOrder.convertedValue());

    return new ConvertedFieldsAndConversionResults<>(
        convertedKursLehrkraftFields, conversionErrors);
  }
}
