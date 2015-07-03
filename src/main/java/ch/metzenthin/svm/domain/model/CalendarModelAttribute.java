package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Hans Stamm
 */
public class CalendarModelAttribute {

    private static final Logger LOGGER = Logger.getLogger(CalendarModelAttribute.class);

    private final ModelAttributeListener modelAttributeListener;
    private final AttributeAccessor<Calendar> attributeAccessor;
    private final Field field;
    private final Calendar earliestValidDate;
    private final Calendar latestValidDate;
    private final String dateFormatString;
    private final Formatter<String> jahreszahlFormatter = new JahreszahlFormatter();

    CalendarModelAttribute(ModelAttributeListener modelAttributeListener, Field field, Calendar earliestValidDate, Calendar latestValidDate, AttributeAccessor<Calendar> attributeAccessor) {
        this(modelAttributeListener, field, earliestValidDate, latestValidDate, attributeAccessor, Converter.DD_MM_YYYY_DATE_FORMAT_STRING);
    }

    CalendarModelAttribute(ModelAttributeListener modelAttributeListener, Field field, Calendar earliestValidDate, Calendar latestValidDate, AttributeAccessor<Calendar> attributeAccessor, String dateFormatString) {
        this.modelAttributeListener = modelAttributeListener;
        this.attributeAccessor = attributeAccessor;
        this.field = field;
        this.earliestValidDate = earliestValidDate;
        this.latestValidDate = latestValidDate;
        this.dateFormatString = dateFormatString;
    }

    Calendar getValue() {
        return attributeAccessor.getValue();
    }

    String getValueAsString() {
        return nullAsEmptyString(asString(getValue(), dateFormatString));
    }

    void setNewValue(boolean isRequired, String newValue) throws SvmValidationException {
        String newValueTrimmed = (newValue != null) ? newValue.trim() : "";
        checkRequired(isRequired, newValueTrimmed);
        String newValueFormatted = newValueTrimmed;
        Calendar newValueAsCalendar = null;
        if (checkNotEmpty(newValueTrimmed)) {
            if (dateFormatString.contains("yyyy")) {
                newValueFormatted = jahreszahlFormatter.format(newValueTrimmed);
            }
            try {
                newValueAsCalendar = toCalendar(newValueFormatted, dateFormatString);
            } catch (ParseException e) {
                modelAttributeListener.invalidate();
                throw new SvmValidationException(1200, e.getMessage(), field);
            }
            checkEarliestValidDate(newValueAsCalendar);
            checkLatestValidDate(newValueAsCalendar);
        }
        String oldValue = getValueAsString();
        attributeAccessor.setValue(newValueAsCalendar);
        if (!equalsNullSafe(newValueTrimmed, newValueFormatted) && equalsNullSafe(oldValue, getValue())) {
            // Der Wert wurde formatiert und das Resultat entspricht dem alten Wert. Dann wird kein PropertyChangeEvent
            // ausgelöst. Damit trotzdem ein Event ausgelöst wird, wird der alte Wert auf den nicht formatierten Wert gesetzt.
            oldValue = newValueTrimmed;
            LOGGER.trace("setNewValue: Alten Wert auf Eingabewert gesetzt, damit PropertyChangeEvent ausgelöst wird. Alter Wert=" + oldValue + ", neuer Wert=" + getValue());
        }
        modelAttributeListener.firePropertyChange(field, oldValue, getValueAsString());
    }

    private void checkEarliestValidDate(Calendar newValueAsCalendar) throws SvmValidationException {
        if (newValueAsCalendar.before(earliestValidDate)) {
            modelAttributeListener.invalidate();
            throw new SvmValidationException(1200, "Datum darf nicht vor " + asString(earliestValidDate, dateFormatString) + " liegen", field);
        }
    }

    private void checkLatestValidDate(Calendar newValueAsCalendar) throws SvmValidationException {
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
