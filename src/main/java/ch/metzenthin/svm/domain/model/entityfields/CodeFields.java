package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.Code;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record CodeFields(String kuerzel, String beschreibung, boolean selektierbar) {

  public static CodeFields of(Code entity) {
    if (entity == null) return null;

    return new CodeFields(entity.getKuerzel(), entity.getBeschreibung(), entity.isSelektierbar());
  }

  public void mergeIntoEntity(Code entity) {
    if (entity == null) return;

    entity.setKuerzel(kuerzel());
    entity.setBeschreibung(beschreibung());
    entity.setSelektierbar(selektierbar());
  }
}
