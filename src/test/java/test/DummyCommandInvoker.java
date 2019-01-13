package test;

import ch.metzenthin.svm.domain.commands.Command;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.GenericDaoCommand;

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
    public void closeSession() {}

    @Override
    public void close() {}
}