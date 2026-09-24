package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationUtils;

/**
 * @author Hans Stamm
 */
public class SearchPersonModelImpl implements SearchPersonModel {

  @Override
  public ValidationResult validateNachname(String nachname) {
    return ValidationUtils.validateLengthIfNotEmpty(nachname, 1, 50, Field.NACHNAME);
  }

  @Override
  public ValidationResult validateVorname(String vorname) {
    return ValidationUtils.validateLengthIfNotEmpty(vorname, 1, 50, Field.VORNAME);
  }
}
