package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.Dispensation;
import java.util.Calendar;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedDispensationFields(
    Calendar dispensationsbeginn,
    Calendar dispensationsende,
    String voraussichtlicheDauer,
    String grund) {

  public static ConvertedDispensationFields of(Dispensation entity) {
    if (entity == null) return null;

    return new ConvertedDispensationFields(
        entity.getDispensationsbeginn(),
        entity.getDispensationsende(),
        entity.getVoraussichtlicheDauer(),
        entity.getGrund());
  }

  public void mergeIntoEntity(Dispensation entity) {
    if (entity == null) return;

    entity.setDispensationsbeginn(dispensationsbeginn());
    entity.setDispensationsende(dispensationsende());
    entity.setVoraussichtlicheDauer(voraussichtlicheDauer());
    entity.setGrund(grund());
  }
}
