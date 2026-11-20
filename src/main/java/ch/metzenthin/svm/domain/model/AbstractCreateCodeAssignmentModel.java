package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotNull;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.persistence.entities.Code;

/**
 * @author Hans Stamm
 */
public abstract class AbstractCreateCodeAssignmentModel<T extends Code>
    implements CreateCodeAssignmentModel<T> {

  @Override
  public ValidationResult validateCode(T code) {
    return validateNotNull(code, Field.CODE);
  }
}
