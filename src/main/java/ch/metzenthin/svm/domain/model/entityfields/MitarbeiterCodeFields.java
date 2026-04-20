package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record MitarbeiterCodeFields(String kuerzel, String beschreibung, boolean selektierbar) {

  public static MitarbeiterCodeFields of(MitarbeiterCode entity) {
    if (entity == null) return null;

    return new MitarbeiterCodeFields(
        entity.getKuerzel(), entity.getBeschreibung(), entity.isSelektierbar());
  }

  public void mergeIntoEntity(MitarbeiterCode entity) {
    if (entity == null) return;

    entity.setKuerzel(kuerzel());
    entity.setBeschreibung(beschreibung());
    entity.setSelektierbar(selektierbar());
  }
}
