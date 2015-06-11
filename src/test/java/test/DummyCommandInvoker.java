package test;

import ch.metzenthin.svm.domain.commands.Command;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.GenericDaoCommand;
import ch.metzenthin.svm.persistence.SvmDbException;

/**
 * @author Hans Stamm
 */
public class DummyCommandInvoker implements CommandInvoker {
    @Override
    public Command executeCommand(Command c) {
        return null;
    }
    @Override
    public GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand) throws SvmDbException {
        return null;
    }

    @Override
    public void beginTransaction() {

    }

    @Override
    public GenericDaoCommand executeCommandWithinTransaction(GenericDaoCommand genericDaoCommand) {
        return null;
    }

    @Override
    public void commitTransaction() {

    }

    @Override
    public void rollbackTransaction() {

    }

}