package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.Code;

/**
 * @author Hans Stamm
 */
public interface CreateCodeAssignmentModel<T extends Code> {

  SaveSuccessfulAndEntityIdOptional getSaveSuccessfulAndCodeIdOptional();

  T[] getSelectableCodes();

  ValidationResult validateCode(T code);

  ValidationResultsAndSubmitResult speichern(T code);
}
