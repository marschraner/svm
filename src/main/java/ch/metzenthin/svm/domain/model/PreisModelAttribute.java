package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Hans Stamm
 */
public class PreisModelAttribute {

    private static final Logger LOGGER = LogManager.getLogger(PreisModelAttribute.class);

    private final ModelAttributeListener modelAttributeListener;
    private final AttributeAccessor<BigDecimal> attributeAccessor;
    private final Field field;
    private final BigDecimal minValidValue;
    private final BigDecimal maxValidValue;

    PreisModelAttribute(ModelAttributeListener modelAttributeListener, Field field, BigDecimal minValidValue, BigDecimal maxValidValue, AttributeAccessor<BigDecimal> attributeAccessor) {
        this.modelAttributeListener = modelAttributeListener;
        this.attributeAccessor = attributeAccessor;
        this.field = field;
        this.minValidValue = minValidValue;
        this.maxValidValue = maxValidValue;
    }

    BigDecimal getValue() {
        return attributeAccessor.getValue();
    }

    String getValueAsString() {
        if (attributeAccessor.getValue() == null) {
            return null;
        }
        return nullAsEmptyString(getValue().toString());
    }

    void setNewValue(boolean isRequired, String newValue, boolean isBulkUpdate) throws SvmValidationException {
        String newValueTrimmed = (newValue != null) ? newValue.trim() : "";
        if (!isBulkUpdate) {
            checkRequired(isRequired, newValueTrimmed);
        }
        BigDecimal newValueAsBigDecimal = null;
        if (checkNotEmpty(newValueTrimmed)) {
            if (newValueTrimmed.matches("^\\d+$")) {
                // .00 anhängen, falls Ganzzahl übergeben
                newValueTrimmed = newValueTrimmed + ".00";
            }
            String errMsg = "Kein gültiger Preis im Format 'Fr.Rp'";
            if (!newValueTrimmed.matches("^\\d+\\.\\d{2}$")) {
                throw new SvmValidationException(1320, errMsg, field);
            }
            try {
                newValueAsBigDecimal = new BigDecimal(newValueTrimmed);
            } catch (NumberFormatException e) {
                modelAttributeListener.invalidate();
                throw new SvmValidationException(1321, errMsg, field);
            }
            if (!isBulkUpdate) {
                checkMinValidValue(newValueAsBigDecimal);
                checkMaxValidValue(newValueAsBigDecimal);
            }
        }
        String oldValue = getValueAsString();
        attributeAccessor.setValue(newValueAsBigDecimal);
        if (newValueAsBigDecimal != null && !equalsNullSafe(newValueTrimmed, nullAsEmptyString(newValueAsBigDecimal.toString())) && equalsNullSafe(oldValue, getValueAsString())) {
            // Der Wert wurde formatiert und das Resultat entspricht dem alten Wert. Dann wird kein PropertyChangeEvent
            // ausgelöst. Damit trotzdem ein Event ausgelöst wird, wird der alte Wert auf den nicht formatierten Wert gesetzt.
            oldValue = newValueTrimmed;
            LOGGER.trace("setNewValue: Alten Wert auf Eingabewert gesetzt, damit PropertyChangeEvent ausgelöst wird. Alter Wert={}, neuer Wert={}", oldValue, getValue());
        }
        modelAttributeListener.firePropertyChange(field, oldValue, getValueAsString());
    }

    /**
     * Achtung: Neuer Wert wird nicht geprüft!
     */
    @SuppressWarnings("SameParameterValue")
    void initValue(String newValue) {
        String oldValue = getValueAsString();
        BigDecimal newValueAsBigDecimal = null;
        if (checkNotEmpty(newValue)) {
            try {
                newValueAsBigDecimal = new BigDecimal(newValue);
            } catch (NumberFormatException e) {
                // Neuer Wert wird nicht geprüft!
            }
        }
        attributeAccessor.setValue(newValueAsBigDecimal);
        modelAttributeListener.firePropertyChange(field, oldValue, getValueAsString());
    }

    private void checkMinValidValue(BigDecimal newValueAsBigDecimal) throws SvmValidationException {
        if (newValueAsBigDecimal.compareTo(minValidValue) < 0) {
            modelAttributeListener.invalidate();
            throw new SvmValidationException(1301, "Wert darf nicht kleiner als " + minValidValue + " sein", field);
        }
    }

    private void checkMaxValidValue(BigDecimal newValueAsBigDecimal) throws SvmValidationException {
        if (newValueAsBigDecimal.compareTo(maxValidValue) > 0) {
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
