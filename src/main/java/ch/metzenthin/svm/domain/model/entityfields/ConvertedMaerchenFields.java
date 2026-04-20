package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.Maerchen;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedMaerchenFields(
    String schuljahr, String bezeichnung, Integer anzahlVorstellungen) {

  public static ConvertedMaerchenFields of(Maerchen entity) {
    if (entity == null) return null;

    return new ConvertedMaerchenFields(
        entity.getSchuljahr(), entity.getBezeichnung(), entity.getAnzahlVorstellungen());
  }

  public void mergeIntoEntity(Maerchen entity) {
    if (entity == null) return;

    entity.setSchuljahr(schuljahr());
    entity.setBezeichnung(bezeichnung());
    entity.setAnzahlVorstellungen(anzahlVorstellungen());
  }
}
