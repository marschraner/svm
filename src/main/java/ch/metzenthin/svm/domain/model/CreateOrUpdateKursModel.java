package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.model.entityfields.KursFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.sql.Time;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateKursModel {

  boolean isNeu();

  KursFields getKursFields();

  Kurstyp getKurstyp();

  Kursort getKursort();

  Mitarbeiter getLehrkraft1();

  Mitarbeiter getLehrkraft2();

  Kurstyp[] getSelectableKurstypen();

  Kursort[] getSelectableKursorte();

  Mitarbeiter[] getSelectableLehrkraefte1();

  Mitarbeiter[] getSelectableLehrkraefte2();

  ValidationResult validateKurstyp(Kurstyp kurstyp);

  ValidationResult validateAltersbereich(String altersbereich);

  ValidationResult validateStufe(String stufe);

  ValidationResult validateWochentag(Wochentag wochentag);

  ValidationResult validateZeitBeginn(Time zeitBeginn);

  ValidationResult validateZeitEnde(Time zeitEnde);

  ValidationResult validateKursort(Kursort kursort);

  ValidationResult validateLehrkraft1(Mitarbeiter lehrkraft1);

  ValidationResult validateBemerkungen(String bemerkungen);

  ValidationResultsAndSaveResult speichern(
      KursFields kursFields,
      Kurstyp kurstyp,
      Kursort kursort,
      Mitarbeiter lehrkraft1,
      Mitarbeiter lehrkraft2);
}
