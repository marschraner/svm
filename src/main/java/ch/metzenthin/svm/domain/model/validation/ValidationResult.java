package ch.metzenthin.svm.domain.model.validation;

import ch.metzenthin.svm.common.datatypes.Field;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public record ValidationResult(boolean isValid, String errorMessage, Set<Field> affectedFields) {}
