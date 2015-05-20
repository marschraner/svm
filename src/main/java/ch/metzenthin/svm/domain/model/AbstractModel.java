package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Hans Stamm
 * todo check... und to... Methoden in Utils auslagern
 * todo PropertyChangeSupport (Observable?)
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

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    //------------------------------------------------------------------------------------------------------------------

    boolean checkNotNull(Object o) {
        return (o != null);
    }

    boolean checkNotEmpty(String s) {
        return checkNotNull(s) && !s.isEmpty();
    }

    boolean checkNumber(String s) {
        if (checkNotEmpty(s)) {
            try {
                //noinspection ResultOfMethodCallIgnored
                Integer.valueOf(s);
                return true;
            } catch (NumberFormatException ignore) {
            }
        }
        return false;
    }

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
