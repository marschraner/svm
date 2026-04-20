package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.persistence.entities.Anmeldung;
import java.util.Calendar;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedAnmeldungFields(Calendar anmeldedatum, Calendar abmeldedatum) {

  public static ConvertedAnmeldungFields of(Anmeldung entity) {
    if (entity == null) return null;

    return new ConvertedAnmeldungFields(entity.getAnmeldedatum(), entity.getAbmeldedatum());
  }

  public void mergeIntoEntity(Anmeldung entity) {
    if (entity == null) return;

    entity.setAnmeldedatum(anmeldedatum());
    entity.setAbmeldedatum(abmeldedatum());
  }
}
