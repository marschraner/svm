package ch.metzenthin.svm.domain;

/**
 * @author Hans Stamm
 */
public class SvmRequiredException extends SvmValidationException {

    public SvmRequiredException(String affectedProperty) {
        super(1000, "Attribut ist obligatorisch", affectedProperty);
    }

    public String getAffectedProperty() {
        return getAffectedProperties()[0];
    }

}
