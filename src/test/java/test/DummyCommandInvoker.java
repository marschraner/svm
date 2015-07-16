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
    public GenericDaoCommand executeCommandAsTransactionWithOpenAndClose(GenericDaoCommand genericDaoCommand) {
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

    @Override
    public void openSession() {

    }

    @Override
    public void closeSession() {

    }

    @Override
    public GenericDaoCommand executeCommandWithinSession(GenericDaoCommand genericDaoCommand) {
        return null;
    }

}