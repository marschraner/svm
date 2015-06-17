package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.dataTypes.Field;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Hans Stamm
 */
public class SvmValidationException extends SvmException {

    private final Set<Field> affectedFields;

    public SvmValidationException(int errorId, String errorMsg, Field... affectedFields) {
        super(errorId, errorMsg);
        this.affectedFields = new HashSet<>(Arrays.asList(affectedFields));
    }

    public Set<Field> getAffectedFields() {
        return affectedFields;
    }

}
