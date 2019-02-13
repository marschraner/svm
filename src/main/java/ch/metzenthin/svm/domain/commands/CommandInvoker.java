package ch.metzenthin.svm.domain.commands;

/**
 * @author Hans Stamm
 */
public interface CommandInvoker {

    Command executeCommand(Command command);

    Command executeCommandAsTransaction(Command command);
}
