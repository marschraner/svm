package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.commands.CommandInvoker;

/**
 * @author Hans Stamm
 */
abstract class AbstractModel {

    private final CommandInvoker commandInvoker;

    CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    AbstractModel(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    boolean checkNotEmpty(String s) {
        return (s != null) && !s.isEmpty();
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

    Integer toInteger(String s) {
        return Integer.valueOf(s);
    }

}
