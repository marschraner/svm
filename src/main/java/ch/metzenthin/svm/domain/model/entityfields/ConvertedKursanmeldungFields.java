package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import java.util.Calendar;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedKursanmeldungFields(
    Calendar anmeldedatum, Calendar abmeldedatum, String bemerkungen) {

  public static ConvertedKursanmeldungFields of(Kursanmeldung entity) {
    if (entity == null) return null;

    return new ConvertedKursanmeldungFields(
        entity.getAnmeldedatum(), entity.getAbmeldedatum(), entity.getBemerkungen());
  }

  public void mergeIntoEntity(Kursanmeldung entity) {
    if (entity == null) return;

    entity.setAnmeldedatum(anmeldedatum());
    entity.setAbmeldedatum(abmeldedatum());
    entity.setBemerkungen(bemerkungen());
  }
}
