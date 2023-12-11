package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Hans Stamm
 */
public class CalendarModelAttribute {

    private static final Logger LOGGER = LogManager.getLogger(CalendarModelAttribute.class);

    private final ModelAttributeListener modelAttributeListener;
    private final AttributeAccessor<Calendar> attributeAccessor;
    private final Field field;
    private final Calendar earliestValidDate;
    private final Calendar latestValidDate;
    private final Formatter<String> jahreszahlFormatter = new JahreszahlFormatter();

    CalendarModelAttribute(ModelAttributeListener modelAttributeListener, Field field, Calendar earliestValidDate, Calendar latestValidDate, AttributeAccessor<Calendar> attributeAccessor) {
        this.modelAttributeListener = modelAttributeListener;
        this.attributeAccessor = attributeAccessor;
        this.field = field;
        this.earliestValidDate = earliestValidDate;
        this.latestValidDate = latestValidDate;
    }

    Calendar getValue() {
        return attributeAccessor.getValue();
    }

    String getValueAsString(String dateFormatString) {
        if (attributeAccessor.getValue() == null) {
            return null;
        }
        return nullAsEmptyString(asString(getValue(), (dateFormatString != null) ? dateFormatString : ""));
    }

    void setNewValue(boolean isRequired, String newValue, boolean isBulkUpdate) throws SvmValidationException {
        setNewValue(isRequired, newValue, Converter.DD_MM_YYYY_DATE_FORMAT_STRING, isBulkUpdate);
    }

    void setNewValue(boolean isRequired, String newValue, String dateFormatString, boolean isBulkUpdate) throws SvmValidationException {
        String newValueTrimmed = (newValue != null) ? newValue.trim() : "";
        if (!isBulkUpdate) {
            checkRequired(isRequired, newValueTrimmed);
        }
        String newValueFormatted = newValueTrimmed;
        Calendar newValueAsCalendar = null;
        if (checkNotEmpty(newValueTrimmed)) {
            if (dateFormatString.contains("y")) {
                newValueFormatted = jahreszahlFormatter.format(newValueTrimmed);
            }
            try {
                newValueAsCalendar = toCalendar(newValueFormatted, dateFormatString);
            } catch (ParseException e) {
                modelAttributeListener.invalidate();
                throw new SvmValidationException(1200, e.getMessage(), field);
            }
            if (!isBulkUpdate && dateFormatString.contains("y")) {
                checkEarliestValidDate(newValueAsCalendar, dateFormatString);
                checkLatestValidDate(newValueAsCalendar, dateFormatString);
            }
        }
        String oldValue = getValueAsString(dateFormatString);
        attributeAccessor.setValue(newValueAsCalendar);
        if (!equalsNullSafe(newValueTrimmed, nullAsEmptyString(asString(newValueAsCalendar, dateFormatString))) && equalsNullSafe(oldValue, getValueAsString(dateFormatString))) {
            // Der Wert wurde formatiert und das Resultat entspricht dem alten Wert. Dann wird kein PropertyChangeEvent
            // ausgelöst. Damit trotzdem ein Event ausgelöst wird, wird der alte Wert auf den nicht formatierten Wert gesetzt.
            oldValue = newValueTrimmed;
            LOGGER.trace("setNewValue: Alten Wert auf Eingabewert gesetzt, damit PropertyChangeEvent ausgelöst wird. Alter Wert=" + oldValue + ", neuer Wert=" + getValue());
        }
        modelAttributeListener.firePropertyChange(field, oldValue, getValueAsString(dateFormatString));
    }

    /**
     * Achtung: Neuer Wert wird nicht geprüft!
     */
    void initValue(String newValue) {
        initValue(newValue, Converter.DD_MM_YYYY_DATE_FORMAT_STRING);
    }

    void initValue(String newValue, String dateFormatString) {
        String oldValue = getValueAsString(dateFormatString);
        Calendar newValueAsCalendar = null;
        if (checkNotEmpty(newValue)) {
            try {
                newValueAsCalendar = toCalendar(newValue, dateFormatString);
            } catch (ParseException e) {
                // Neuer Wert wird nicht geprüft!
            }
        }
        attributeAccessor.setValue(newValueAsCalendar);
        modelAttributeListener.firePropertyChange(field, oldValue, getValueAsString(dateFormatString));
    }

    private void checkEarliestValidDate(Calendar newValueAsCalendar, String dateFormatString) throws SvmValidationException {
        if (newValueAsCalendar.before(earliestValidDate)) {
            modelAttributeListener.invalidate();
            throw new SvmValidationException(1200, "Datum darf nicht vor " + asString(earliestValidDate, dateFormatString) + " liegen", field);
        }
    }

    private void checkLatestValidDate(Calendar newValueAsCalendar, String dateFormatString) throws SvmValidationException {
        if (newValueAsCalendar.after(latestValidDate)) {
            modelAttributeListener.invalidate();
            throw new SvmValidationException(1200, "Datum darf nicht nach " + asString(latestValidDate, dateFormatString) + " liegen", field);
        }
    }

    private void checkRequired(boolean isRequired, String newValue) throws SvmRequiredException {
        if (isRequired && !checkNotEmpty(newValue)) {
            modelAttributeListener.invalidate();
            throw new SvmRequiredException(field);
        }
    }

}
