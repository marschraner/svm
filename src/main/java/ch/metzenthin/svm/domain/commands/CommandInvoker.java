package ch.metzenthin.svm.domain.commands;

import javax.persistence.EntityManager;

/**
 * @author Hans Stamm
 */
public interface CommandInvoker {

    Command executeCommand(Command c);

    GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand);

    GenericDaoCommand executeCommandAsTransaction(GenericDaoCommand genericDaoCommand);

    GenericDaoCommand executeCommandAsTransactionWithOpenAndClose(GenericDaoCommand genericDaoCommand);

    void openSession();

    void closeSession();

    void closeSessionAndEntityManagerFactory();

    EntityManager getEntityManager();
}
