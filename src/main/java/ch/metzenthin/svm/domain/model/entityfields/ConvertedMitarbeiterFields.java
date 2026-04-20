package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.Calendar;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedMitarbeiterFields(
    String ahvNummer,
    String ibanNummer,
    boolean lehrkraft,
    String vertretungsmoeglichkeiten,
    String bemerkungen,
    boolean aktiv,
    Anrede anrede,
    String vorname,
    String nachname,
    Calendar geburtsdatum,
    String festnetz,
    String natel,
    String email) {

  public static ConvertedMitarbeiterFields of(Mitarbeiter entity) {
    if (entity == null) return null;

    return new ConvertedMitarbeiterFields(
        entity.getAhvNummer(),
        entity.getIbanNummer(),
        entity.isLehrkraft(),
        entity.getVertretungsmoeglichkeiten(),
        entity.getBemerkungen(),
        entity.isAktiv(),
        entity.getAnrede(),
        entity.getVorname(),
        entity.getNachname(),
        entity.getGeburtsdatum(),
        entity.getFestnetz(),
        entity.getNatel(),
        entity.getEmail());
  }

  public void mergeIntoEntity(Mitarbeiter entity) {
    if (entity == null) return;

    entity.setAhvNummer(ahvNummer());
    entity.setIbanNummer(ibanNummer());
    entity.setLehrkraft(lehrkraft());
    entity.setVertretungsmoeglichkeiten(vertretungsmoeglichkeiten());
    entity.setBemerkungen(bemerkungen());
    entity.setAktiv(aktiv());
    entity.setAnrede(anrede());
    entity.setVorname(vorname());
    entity.setNachname(nachname());
    entity.setGeburtsdatum(geburtsdatum());
    entity.setFestnetz(festnetz());
    entity.setNatel(natel());
    entity.setEmail(email());
  }
}
