package ch.metzenthin.svm.domain.commands;

/**
 * @author Hans Stamm
 */
public interface CommandInvoker {

    Command executeCommand(Command c);

    GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand);

    GenericDaoCommand executeCommandAsTransactionWithOpenAndCloseSvmTest(GenericDaoCommand genericDaoCommand);

    GenericDaoCommand executeCommandAsTransaction(GenericDaoCommand genericDaoCommand);

    void openSession();

    void openSessionSvmTest();

    void closeSession();

    void clear();

}
