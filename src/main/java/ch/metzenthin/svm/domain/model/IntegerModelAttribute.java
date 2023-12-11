package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Hans Stamm
 */
public class IntegerModelAttribute {

    private static final Logger LOGGER = LogManager.getLogger(IntegerModelAttribute.class);

    private final ModelAttributeListener modelAttributeListener;
    private final AttributeAccessor<Integer> attributeAccessor;
    private final Field field;
    private final int minValidValue;
    private final int maxValidValue;

    IntegerModelAttribute(ModelAttributeListener modelAttributeListener, Field field, int minValidValue, int maxValidValue, AttributeAccessor<Integer> attributeAccessor) {
        this.modelAttributeListener = modelAttributeListener;
        this.attributeAccessor = attributeAccessor;
        this.field = field;
        this.minValidValue = minValidValue;
        this.maxValidValue = maxValidValue;
    }

    Integer getValue() {
        return attributeAccessor.getValue();
    }

    String getValueAsString() {
        if (attributeAccessor.getValue() == null) {
            return null;
        }
        return nullAsEmptyString(Integer.toString(getValue()));
    }

    void setNewValue(boolean isRequired, String newValue, boolean isBulkUpdate) throws SvmValidationException {
        String newValueTrimmed = (newValue != null) ? newValue.trim() : "";
        if (!isBulkUpdate) {
            checkRequired(isRequired, newValueTrimmed);
        }
        Integer newValueAsInteger = null;
        if (checkNotEmpty(newValueTrimmed)) {
            try {
                newValueAsInteger = Integer.valueOf(newValueTrimmed);
            } catch (NumberFormatException e) {
                modelAttributeListener.invalidate();
                throw new SvmValidationException(1300, "Eingabe muss eine ganze Zahl sein", field);
            }
            if (!isBulkUpdate) {
                checkMinValidValue(newValueAsInteger);
                checkMaxValidValue(newValueAsInteger);
            }
        }
        String oldValue = getValueAsString();
        attributeAccessor.setValue(newValueAsInteger);
        if (newValueAsInteger != null && !equalsNullSafe(newValueTrimmed, nullAsEmptyString(Integer.toString(newValueAsInteger))) && equalsNullSafe(oldValue, getValueAsString())) {
            // Der Wert wurde formatiert und das Resultat entspricht dem alten Wert. Dann wird kein PropertyChangeEvent
            // ausgelöst. Damit trotzdem ein Event ausgelöst wird, wird der alte Wert auf den nicht formatierten Wert gesetzt.
            oldValue = newValueTrimmed;
            LOGGER.trace("setNewValue: Alten Wert auf Eingabewert gesetzt, damit PropertyChangeEvent ausgelöst wird. Alter Wert=" + oldValue + ", neuer Wert=" + getValue());
        }
        modelAttributeListener.firePropertyChange(field, oldValue, getValueAsString());
    }

    /**
     * Achtung: Neuer Wert wird nicht geprüft!
     */
    void initValue(String newValue) {
        String oldValue = getValueAsString();
        Integer newValueAsInteger = null;
        if (checkNotEmpty(newValue)) {
            try {
                newValueAsInteger = Integer.valueOf(newValue);
            } catch (NumberFormatException e) {
                // Neuer Wert wird nicht geprüft!
            }
        }
        attributeAccessor.setValue(newValueAsInteger);
        modelAttributeListener.firePropertyChange(field, oldValue, getValueAsString());
    }

    private void checkMinValidValue(Integer newValueAsInteger) throws SvmValidationException {
        if (newValueAsInteger < minValidValue) {
            modelAttributeListener.invalidate();
            throw new SvmValidationException(1301, "Wert darf nicht kleiner als " + minValidValue + " sein", field);
        }
    }

    private void checkMaxValidValue(Integer newValueAsInteger) throws SvmValidationException {
        if (newValueAsInteger > maxValidValue) {
            modelAttributeListener.invalidate();
            throw new SvmValidationException(1302, "Wert darf nicht grösser als " + maxValidValue + " sein", field);
        }
    }

    private void checkRequired(boolean isRequired, String newValue) throws SvmRequiredException {
        if (isRequired && !checkNotEmpty(newValue)) {
            modelAttributeListener.invalidate();
            throw new SvmRequiredException(field);
        }
    }

}
