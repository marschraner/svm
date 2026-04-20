package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.entities.Kurs;
import java.sql.Time;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedKursFields(
    String altersbereich,
    String stufe,
    Wochentag wochentag,
    Time zeitBeginn,
    Time zeitEnde,
    String bemerkungen) {

  public static ConvertedKursFields of(Kurs entity) {
    if (entity == null) return null;

    return new ConvertedKursFields(
        entity.getAltersbereich(),
        entity.getStufe(),
        entity.getWochentag(),
        entity.getZeitBeginn(),
        entity.getZeitEnde(),
        entity.getBemerkungen());
  }

  public void mergeIntoEntity(Kurs entity) {
    if (entity == null) return;

    entity.setAltersbereich(altersbereich());
    entity.setStufe(stufe());
    entity.setWochentag(wochentag());
    entity.setZeitBeginn(zeitBeginn());
    entity.setZeitEnde(zeitEnde());
    entity.setBemerkungen(bemerkungen());
  }
}
