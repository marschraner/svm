package ch.metzenthin.svm.domain.commands;

/**
 * @author Hans Stamm
 */
public interface CommandInvoker {

    Command executeCommand(Command c);

    GenericDaoCommand executeCommandAsTransactionWithOpenAndClose(GenericDaoCommand genericDaoCommand);

    void beginTransaction();

    GenericDaoCommand executeCommandWithinTransaction(GenericDaoCommand genericDaoCommand);

    void commitTransaction();

    void rollbackTransaction();

    void openSession();

    void closeSession();

    GenericDaoCommand executeCommandWithinSession(GenericDaoCommand genericDaoCommand);
}
