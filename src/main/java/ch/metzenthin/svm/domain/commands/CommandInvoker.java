package ch.metzenthin.svm.domain.commands;

/**
 * @author Hans Stamm
 */
public interface CommandInvoker {

    Command executeCommand(Command c);

    GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand);

    GenericDaoCommand executeCommandAsTransaction(GenericDaoCommand genericDaoCommand);

    GenericDaoCommand executeCommandAsTransactionWithOpenAndClose(GenericDaoCommand genericDaoCommand);

    void closeSession();

    void close();

}
