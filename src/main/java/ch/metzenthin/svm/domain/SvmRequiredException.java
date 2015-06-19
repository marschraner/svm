package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.dataTypes.Field;

/**
 * @author Hans Stamm
 */
public class SvmRequiredException extends SvmValidationException {

    public SvmRequiredException(Field affectedField) {
        super(1000, "Attribut ist obligatorisch", affectedField);
    }

}
