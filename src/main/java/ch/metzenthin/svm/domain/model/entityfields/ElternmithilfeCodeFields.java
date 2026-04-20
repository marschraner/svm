package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ElternmithilfeCodeFields(String kuerzel, String beschreibung, boolean selektierbar) {

  public static ElternmithilfeCodeFields of(ElternmithilfeCode entity) {
    if (entity == null) return null;

    return new ElternmithilfeCodeFields(
        entity.getKuerzel(), entity.getBeschreibung(), entity.isSelektierbar());
  }

  public void mergeIntoEntity(ElternmithilfeCode entity) {
    if (entity == null) return;

    entity.setKuerzel(kuerzel());
    entity.setBeschreibung(beschreibung());
    entity.setSelektierbar(selektierbar());
  }
}
