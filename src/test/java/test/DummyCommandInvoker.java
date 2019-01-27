package test;

import ch.metzenthin.svm.domain.commands.Command;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.GenericDaoCommand;

import javax.persistence.EntityManager;

/**
 * @author Hans Stamm
 */
public class DummyCommandInvoker implements CommandInvoker {
    @Override
    public Command executeCommand(Command c) {
        return null;
    }

    @Override
    public GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand) {
        return null;
    }

    @Override
    public GenericDaoCommand executeCommandAsTransaction(GenericDaoCommand genericDaoCommand) {
        return null;
    }

    @Override
    public GenericDaoCommand executeCommandAsTransactionWithOpenAndClose(GenericDaoCommand genericDaoCommand) {
        return null;
    }

    @Override
    public void openSession() {}

    @Override
    public void closeSession() {}

    @Override
    public void closeSessionAndEntityManagerFactory() {}

    @Override
    public EntityManager getEntityManager() {
        return null;
    }
}