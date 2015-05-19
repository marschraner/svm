package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Hans Stamm
 * todo check... und to... Methoden in Utils auslagern
 * todo PropertyChangeSupport (Observable?)
 */
abstract class AbstractModel {

    private final CommandInvoker commandInvoker;

    CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    AbstractModel(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

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
