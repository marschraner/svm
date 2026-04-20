package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record SemesterrechnungCodeFields(
    String kuerzel, String beschreibung, boolean selektierbar) {

  public static SemesterrechnungCodeFields of(SemesterrechnungCode entity) {
    if (entity == null) return null;

    return new SemesterrechnungCodeFields(
        entity.getKuerzel(), entity.getBeschreibung(), entity.isSelektierbar());
  }

  public void mergeIntoEntity(SemesterrechnungCode entity) {
    if (entity == null) return;

    entity.setKuerzel(kuerzel());
    entity.setBeschreibung(beschreibung());
    entity.setSelektierbar(selektierbar());
  }
}
