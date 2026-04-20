package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.Calendar;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedSemesterFields(
    String schuljahr,
    Semesterbezeichnung semesterbezeichnung,
    Calendar semesterbeginn,
    Calendar semesterende,
    Calendar ferienbeginn1,
    Calendar ferienende1,
    Calendar ferienbeginn2,
    Calendar ferienende2) {

  public static ConvertedSemesterFields of(Semester entity) {
    if (entity == null) return null;

    return new ConvertedSemesterFields(
        entity.getSchuljahr(),
        entity.getSemesterbezeichnung(),
        entity.getSemesterbeginn(),
        entity.getSemesterende(),
        entity.getFerienbeginn1(),
        entity.getFerienende1(),
        entity.getFerienbeginn2(),
        entity.getFerienende2());
  }

  public void mergeIntoEntity(Semester entity) {
    if (entity == null) return;

    entity.setSchuljahr(schuljahr());
    entity.setSemesterbezeichnung(semesterbezeichnung());
    entity.setSemesterbeginn(semesterbeginn());
    entity.setSemesterende(semesterende());
    entity.setFerienbeginn1(ferienbeginn1());
    entity.setFerienende1(ferienende1());
    entity.setFerienbeginn2(ferienbeginn2());
    entity.setFerienende2(ferienende2());
  }
}
