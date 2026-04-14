package ch.metzenthin.svm.domain.model.validation;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public class ValidationUtils {

  private ValidationUtils() {}

  public static ValidationResult validateNotEmpty(String value, Field field) {
    return (value == null || value.isBlank())
        ? new ValidationResult("Eintrag ist obligatorisch!", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateNotTooShort(String value, int minLength, Field field) {
    return (value.length() < minLength)
        ? new ValidationResult("Länge muss mindestens " + minLength + " sein!", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateNotTooLong(String value, int maxLength, Field field) {
    return (value.length() > maxLength)
        ? new ValidationResult("Länge darf höchstens " + maxLength + " sein!", Set.of(field))
        : new ValidationResult();
  }
}
