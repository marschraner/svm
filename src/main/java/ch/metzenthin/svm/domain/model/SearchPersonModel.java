package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;

/**
 * @author Martin Schraner
 */
public interface SearchPersonModel {

  ValidationResult validateNachname(String nachname);

  ValidationResult validateVorname(String vorname);
}
