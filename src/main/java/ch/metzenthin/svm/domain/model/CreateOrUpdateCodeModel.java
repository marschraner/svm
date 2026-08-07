package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.CodeFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateCodeModel {

  boolean isNeu();

  CodeFields getCodeFields();

  ValidationResult validateKuerzel(String kuerzel);

  ValidationResult validateBeschreibung(String beschreibung);

  ValidationResultsAndSubmitResult speichern(CodeFields codeFields);
}
