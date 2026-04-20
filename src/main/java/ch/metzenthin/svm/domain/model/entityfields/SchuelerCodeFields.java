package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record SchuelerCodeFields(String kuerzel, String beschreibung, boolean selektierbar) {

  public static SchuelerCodeFields of(SchuelerCode entity) {
    if (entity == null) return null;

    return new SchuelerCodeFields(
        entity.getKuerzel(), entity.getBeschreibung(), entity.isSelektierbar());
  }

  public void mergeIntoEntity(SchuelerCode entity) {
    if (entity == null) return;

    entity.setKuerzel(kuerzel());
    entity.setBeschreibung(beschreibung());
    entity.setSelektierbar(selektierbar());
  }
}
