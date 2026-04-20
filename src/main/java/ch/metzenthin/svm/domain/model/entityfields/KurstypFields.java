package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.Kurstyp;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record KurstypFields(String bezeichnung, boolean selektierbar) {

  public static KurstypFields of(Kurstyp entity) {
    if (entity == null) return null;

    return new KurstypFields(entity.getBezeichnung(), entity.isSelektierbar());
  }

  public void mergeIntoEntity(Kurstyp entity) {
    if (entity == null) return;

    entity.setBezeichnung(bezeichnung());
    entity.setSelektierbar(selektierbar());
  }
}
