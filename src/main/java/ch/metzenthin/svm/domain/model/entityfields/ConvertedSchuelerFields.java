package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Schueler;
import java.util.Calendar;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedSchuelerFields(
    Geschlecht geschlecht,
    String bemerkungen,
    Anrede anrede,
    String vorname,
    String nachname,
    Calendar geburtsdatum,
    String festnetz,
    String natel,
    String email) {

  public static ConvertedSchuelerFields of(Schueler entity) {
    if (entity == null) return null;

    return new ConvertedSchuelerFields(
        entity.getGeschlecht(),
        entity.getBemerkungen(),
        entity.getAnrede(),
        entity.getVorname(),
        entity.getNachname(),
        entity.getGeburtsdatum(),
        entity.getFestnetz(),
        entity.getNatel(),
        entity.getEmail());
  }

  public void mergeIntoEntity(Schueler entity) {
    if (entity == null) return;

    entity.setGeschlecht(geschlecht());
    entity.setBemerkungen(bemerkungen());
    entity.setAnrede(anrede());
    entity.setVorname(vorname());
    entity.setNachname(nachname());
    entity.setGeburtsdatum(geburtsdatum());
    entity.setFestnetz(festnetz());
    entity.setNatel(natel());
    entity.setEmail(email());
  }
}
