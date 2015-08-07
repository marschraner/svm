package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import org.apache.log4j.Logger;

import java.sql.Time;
import java.text.ParseException;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.Converter.toTime;
import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class TimeModelAttribute {

    private static final Logger LOGGER = Logger.getLogger(TimeModelAttribute.class);

    private final ModelAttributeListener modelAttributeListener;
    private final AttributeAccessor<Time> attributeAccessor;
    private final Field field;

    TimeModelAttribute(ModelAttributeListener modelAttributeListener, Field field, AttributeAccessor<Time> attributeAccessor) {
        this.modelAttributeListener = modelAttributeListener;
        this.attributeAccessor = attributeAccessor;
        this.field = field;
    }

    Time getValue() {
        return attributeAccessor.getValue();
    }

    String getValueAsString() {
        if (attributeAccessor.getValue() == null) {
            return null;
        }
        return nullAsEmptyString(asString(getValue()));
    }

    void setNewValue(boolean isRequired, String newValue, boolean isBulkUpdate) throws SvmValidationException {
        String newValueTrimmed = (newValue != null) ? newValue.trim() : "";
        if (!isBulkUpdate) {
            checkRequired(isRequired, newValueTrimmed);
        }
        Time newValueAsTime = null;
        if (checkNotEmpty(newValueTrimmed)) {
            try {
                newValueAsTime = toTime(newValueTrimmed);
            } catch (ParseException e) {
                modelAttributeListener.invalidate();
                throw new SvmValidationException(1300, e.getMessage(), field);
            }
        }
        String oldValue = getValueAsString();
        attributeAccessor.setValue(newValueAsTime);
        if (newValueAsTime != null && !equalsNullSafe(newValueTrimmed, nullAsEmptyString(asString(newValueAsTime))) && equalsNullSafe(oldValue, getValueAsString())) {
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
        Time newValueAsTime = null;
        if (checkNotEmpty(newValue)) {
            try {
                newValueAsTime = toTime(newValue);
            } catch (ParseException e) {
                // Neuer Wert wird nicht geprüft!
            }
        }
        attributeAccessor.setValue(newValueAsTime);
        modelAttributeListener.firePropertyChange(field, oldValue, getValueAsString());
    }

    private void checkRequired(boolean isRequired, String newValue) throws SvmRequiredException {
        if (isRequired && !checkNotEmpty(newValue)) {
            modelAttributeListener.invalidate();
            throw new SvmRequiredException(field);
        }
    }

}
