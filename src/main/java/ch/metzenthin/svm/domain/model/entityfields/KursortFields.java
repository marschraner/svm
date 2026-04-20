package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.Kursort;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record KursortFields(String bezeichnung, boolean selektierbar) {

  public static KursortFields of(Kursort entity) {
    if (entity == null) return null;

    return new KursortFields(entity.getBezeichnung(), entity.isSelektierbar());
  }

  public void mergeIntoEntity(Kursort entity) {
    if (entity == null) return;

    entity.setBezeichnung(bezeichnung());
    entity.setSelektierbar(selektierbar());
  }
}
