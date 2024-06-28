package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.common.datatypes.Field;

/**
 * @author Hans Stamm
 */
public class SvmRequiredException extends SvmValidationException {

    public SvmRequiredException(Field affectedField) {
        super(1000, "Eintrag ist obligatorisch", affectedField);
    }

}
