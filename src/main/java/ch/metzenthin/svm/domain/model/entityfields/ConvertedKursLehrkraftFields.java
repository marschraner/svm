package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.KursLehrkraft;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedKursLehrkraftFields(int lehrkraefteOrder) {

  public static ConvertedKursLehrkraftFields of(KursLehrkraft entity) {
    if (entity == null) return null;

    return new ConvertedKursLehrkraftFields(entity.getLehrkraefteOrder());
  }

  public void mergeIntoEntity(KursLehrkraft entity) {
    if (entity == null) return;

    entity.setLehrkraefteOrder(lehrkraefteOrder());
  }
}
