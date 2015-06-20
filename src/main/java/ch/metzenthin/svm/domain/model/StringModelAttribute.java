package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;

import static ch.metzenthin.svm.common.utils.Converter.emptyStringAsNull;
import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Hans Stamm
 */
public class StringModelAttribute {

    private final ModelAttributeListener modelAttributeListener;
    private final AttributeAccessor<String> attributeAccessor;
    private final Field field;
    private final int minLength;
    private final int maxLength;

    StringModelAttribute(ModelAttributeListener modelAttributeListener, Field field, int minLength, int maxLength, AttributeAccessor<String> attributeAccessor) {
        this.modelAttributeListener = modelAttributeListener;
        this.attributeAccessor = attributeAccessor;
        this.field = field;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    String getValue() {
        return nullAsEmptyString(attributeAccessor.getValue());
    }

    void setNewValue(boolean isRequired, String newValue) throws SvmValidationException {
        String newValueTrimmed = (newValue != null) ? newValue.trim() : "";
        checkRequired(isRequired, newValueTrimmed);
        if (checkNotEmpty(newValueTrimmed)) {
            checkMinLength(newValueTrimmed);
            checkMaxLength(newValueTrimmed);
        }
        String oldValue = getValue();
        attributeAccessor.setValue(emptyStringAsNull(newValueTrimmed));
        modelAttributeListener.firePropertyChange(field, oldValue, getValue());
    }

    /**
     * Achtung: Neuer Wert wird nicht geprüft!
     */
    void initValue(String newValue) {
        String oldValue = getValue();
        attributeAccessor.setValue(emptyStringAsNull(newValue));
        modelAttributeListener.firePropertyChange(field, oldValue, getValue());
    }

    private void checkMaxLength(String newValue) throws SvmValidationException {
        if (newValue.length() > maxLength) {
            modelAttributeListener.invalidate();
            throw new SvmValidationException(1100, "Länge darf höchstens " + maxLength + " sein", field);
        }
    }

    private void checkMinLength(String newValue) throws SvmValidationException {
        if (newValue.length() < minLength) {
            modelAttributeListener.invalidate();
            throw new SvmValidationException(1100, "Länge muss mindestens " + minLength + " sein", field);
        }
    }

    private void checkRequired(boolean isRequired, String newValue) throws SvmRequiredException {
        if (isRequired && !checkNotEmpty(newValue)) {
            modelAttributeListener.invalidate();
            throw new SvmRequiredException(field);
        }
    }

}
