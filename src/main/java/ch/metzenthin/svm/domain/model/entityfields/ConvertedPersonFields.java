package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Person;
import java.util.Calendar;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedPersonFields(
    Anrede anrede,
    String vorname,
    String nachname,
    Calendar geburtsdatum,
    String festnetz,
    String natel,
    String email) {

  public static ConvertedPersonFields of(Person entity) {
    if (entity == null) return null;

    return new ConvertedPersonFields(
        entity.getAnrede(),
        entity.getVorname(),
        entity.getNachname(),
        entity.getGeburtsdatum(),
        entity.getFestnetz(),
        entity.getNatel(),
        entity.getEmail());
  }

  public void mergeIntoEntity(Person entity) {
    if (entity == null) return;

    entity.setAnrede(anrede());
    entity.setVorname(vorname());
    entity.setNachname(nachname());
    entity.setGeburtsdatum(geburtsdatum());
    entity.setFestnetz(festnetz());
    entity.setNatel(natel());
    entity.setEmail(email());
  }
}
