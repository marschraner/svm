package ch.metzenthin.svm.domain.model.validation;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public record ValidationResult(boolean isValid, String errorMessage, Set<Field> affectedFields) {

  public ValidationResult() {
    this(true, null, null);
  }

  public ValidationResult(String errorMessage, Set<Field> affectedFields) {
    this(false, errorMessage, affectedFields);
  }

  public boolean affectedFieldsContains(Field field) {
    return affectedFields != null && affectedFields.contains(field);
  }
}
