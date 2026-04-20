package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import java.util.Calendar;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedAngehoerigerFields(
    Boolean wuenschtEmails,
    Anrede anrede,
    String vorname,
    String nachname,
    Calendar geburtsdatum,
    String festnetz,
    String natel,
    String email) {

  public static ConvertedAngehoerigerFields of(Angehoeriger entity) {
    if (entity == null) return null;

    return new ConvertedAngehoerigerFields(
        entity.getWuenschtEmails(),
        entity.getAnrede(),
        entity.getVorname(),
        entity.getNachname(),
        entity.getGeburtsdatum(),
        entity.getFestnetz(),
        entity.getNatel(),
        entity.getEmail());
  }

  public void mergeIntoEntity(Angehoeriger entity) {
    if (entity == null) return;

    entity.setWuenschtEmails(wuenschtEmails());
    entity.setAnrede(anrede());
    entity.setVorname(vorname());
    entity.setNachname(nachname());
    entity.setGeburtsdatum(geburtsdatum());
    entity.setFestnetz(festnetz());
    entity.setNatel(natel());
    entity.setEmail(email());
  }
}
