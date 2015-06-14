package ch.metzenthin.svm.domain;

/**
 * @author Hans Stamm
 */
public class SvmValidationException extends SvmException {

    private final String[] affectedProperties;

    public SvmValidationException(int errorId, String errorMsg, String... affectedProperties) {
        super(errorId, errorMsg);
        this.affectedProperties = affectedProperties;
    }

    public String[] getAffectedProperties() {
        return affectedProperties.clone();
    }

}
