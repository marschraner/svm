package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.domain.model.conversion.BigDecimalConverter;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.conversion.IntegerConverter;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record LektionsgebuehrenFields(
    String lektionslaenge,
    String betrag1Kind,
    String betrag2Kinder,
    String betrag3Kinder,
    String betrag4Kinder,
    String betrag5Kinder,
    String betrag6Kinder) {

  public static LektionsgebuehrenFields of(Lektionsgebuehren entity) {
    if (entity == null) return null;

    return new LektionsgebuehrenFields(
        IntegerConverter.toString(entity.getLektionslaenge()),
        BigDecimalConverter.toString(entity.getBetrag1Kind()),
        BigDecimalConverter.toString(entity.getBetrag2Kinder()),
        BigDecimalConverter.toString(entity.getBetrag3Kinder()),
        BigDecimalConverter.toString(entity.getBetrag4Kinder()),
        BigDecimalConverter.toString(entity.getBetrag5Kinder()),
        BigDecimalConverter.toString(entity.getBetrag6Kinder()));
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedLektionsgebuehrenFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Integer> lektionslaenge =
        IntegerConverter.convertToInt("lektionslaenge", lektionslaenge());
    if (!lektionslaenge.isValid()) conversionErrors.add(lektionslaenge);
    ConversionResult<BigDecimal> betrag1Kind =
        BigDecimalConverter.convertToBigDecimal("betrag1Kind", betrag1Kind());
    if (!betrag1Kind.isValid()) conversionErrors.add(betrag1Kind);
    ConversionResult<BigDecimal> betrag2Kinder =
        BigDecimalConverter.convertToBigDecimal("betrag2Kinder", betrag2Kinder());
    if (!betrag2Kinder.isValid()) conversionErrors.add(betrag2Kinder);
    ConversionResult<BigDecimal> betrag3Kinder =
        BigDecimalConverter.convertToBigDecimal("betrag3Kinder", betrag3Kinder());
    if (!betrag3Kinder.isValid()) conversionErrors.add(betrag3Kinder);
    ConversionResult<BigDecimal> betrag4Kinder =
        BigDecimalConverter.convertToBigDecimal("betrag4Kinder", betrag4Kinder());
    if (!betrag4Kinder.isValid()) conversionErrors.add(betrag4Kinder);
    ConversionResult<BigDecimal> betrag5Kinder =
        BigDecimalConverter.convertToBigDecimal("betrag5Kinder", betrag5Kinder());
    if (!betrag5Kinder.isValid()) conversionErrors.add(betrag5Kinder);
    ConversionResult<BigDecimal> betrag6Kinder =
        BigDecimalConverter.convertToBigDecimal("betrag6Kinder", betrag6Kinder());
    if (!betrag6Kinder.isValid()) conversionErrors.add(betrag6Kinder);

    ConvertedLektionsgebuehrenFields convertedLektionsgebuehrenFields =
        new ConvertedLektionsgebuehrenFields(
            lektionslaenge.convertedValue(),
            betrag1Kind.convertedValue(),
            betrag2Kinder.convertedValue(),
            betrag3Kinder.convertedValue(),
            betrag4Kinder.convertedValue(),
            betrag5Kinder.convertedValue(),
            betrag6Kinder.convertedValue());

    return new ConvertedFieldsAndConversionResults<>(
        convertedLektionsgebuehrenFields, conversionErrors);
  }
}
