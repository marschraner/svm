package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNumber;

/**
 * @author Hans Stamm
 */
abstract class AbstractModel implements Model {

    private final CommandInvoker commandInvoker;

    AbstractModel(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Property change support
    //------------------------------------------------------------------------------------------------------------------

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    //------------------------------------------------------------------------------------------------------------------


    Integer toIntegerOrNull(String s) {
        Integer i = null;
        if (checkNumber(s)) {
            i = toInteger(s);
        }
        return i;
    }

    Integer toInteger(String s) {
        return Integer.valueOf(s);
    }

    Calendar toCalendar(String s) throws ParseException {
        if (!checkNotEmpty(s)) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        calendar.setTime(formatter.parse(s));
        return calendar;
    }

    Calendar toCalendarIgnoreException(String s) {
        if (!checkNotEmpty(s)) {
            return null;
        }
        Calendar calendar = null;
        try {
            calendar = toCalendar(s);
        } catch (ParseException ignore) {
        }
        return calendar;
    }

}
