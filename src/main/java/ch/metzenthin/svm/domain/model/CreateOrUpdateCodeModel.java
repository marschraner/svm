package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.CodeFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateCodeModel {

  boolean isNeu();

  CodeFields getCodeFields();

  ValidationResult validateKuerzel(String kuerzel);

  ValidationResult validateBeschreibung(String beschreibung);

  ValidationResultsAndSaveResult speichern(CodeFields codeFields);
}
