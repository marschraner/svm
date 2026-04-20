package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import java.math.BigDecimal;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedLektionsgebuehrenFields(
    int lektionslaenge,
    BigDecimal betrag1Kind,
    BigDecimal betrag2Kinder,
    BigDecimal betrag3Kinder,
    BigDecimal betrag4Kinder,
    BigDecimal betrag5Kinder,
    BigDecimal betrag6Kinder) {

  public static ConvertedLektionsgebuehrenFields of(Lektionsgebuehren entity) {
    if (entity == null) return null;

    return new ConvertedLektionsgebuehrenFields(
        entity.getLektionslaenge(),
        entity.getBetrag1Kind(),
        entity.getBetrag2Kinder(),
        entity.getBetrag3Kinder(),
        entity.getBetrag4Kinder(),
        entity.getBetrag5Kinder(),
        entity.getBetrag6Kinder());
  }

  public void mergeIntoEntity(Lektionsgebuehren entity) {
    if (entity == null) return;

    entity.setLektionslaenge(lektionslaenge());
    entity.setBetrag1Kind(betrag1Kind());
    entity.setBetrag2Kinder(betrag2Kinder());
    entity.setBetrag3Kinder(betrag3Kinder());
    entity.setBetrag4Kinder(betrag4Kinder());
    entity.setBetrag5Kinder(betrag5Kinder());
    entity.setBetrag6Kinder(betrag6Kinder());
  }
}
