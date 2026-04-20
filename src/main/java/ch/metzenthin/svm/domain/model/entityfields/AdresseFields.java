package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.Adresse;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record AdresseFields(String strasse, String hausnummer, String plz, String ort) {

  public static AdresseFields of(Adresse entity) {
    if (entity == null) return null;

    return new AdresseFields(
        entity.getStrasse(), entity.getHausnummer(), entity.getPlz(), entity.getOrt());
  }

  public void mergeIntoEntity(Adresse entity) {
    if (entity == null) return;

    entity.setStrasse(strasse());
    entity.setHausnummer(hausnummer());
    entity.setPlz(plz());
    entity.setOrt(ort());
  }
}
