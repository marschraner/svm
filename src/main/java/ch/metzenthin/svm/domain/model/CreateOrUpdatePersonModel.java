package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdatePersonModel {

  boolean isNeu();

  ValidationResult validateAnrede(Anrede anrede);

  ValidationResult validateVorname(String vorname);

  ValidationResult validateNachname(String nachname);

  ValidationResult validateGeburtsdatum(Calendar geburtsdatum);

  ValidationResult validateStrasse(String strasse);

  ValidationResult validateHausnummer(String hausnummer);

  ValidationResult validatePlz(String plz);

  ValidationResult validateOrt(String ort);

  ValidationResult validateFestnetz(String festnetz);

  ValidationResult validateNatel(String natel);

  ValidationResult validateEmail(String email);
}
