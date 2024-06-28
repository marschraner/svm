package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.common.datatypes.Field;

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

    public String getMessage() {
        return super.getMessage();
    }

    public String getMessageLong() {
        String msg = super.getMessage();
        if ((getAffectedFields() != null) && !getAffectedFields().isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(msg);
            stringBuilder.append(": [");
            boolean first = true;
            for (Field field : getAffectedFields()) {
                if (!first) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(field);
                first = false;
            }
            stringBuilder.append("]");
            msg = stringBuilder.toString();
        }
        return msg;
    }

}
