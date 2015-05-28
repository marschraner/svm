package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.SvmDbException;

/**
 * @author Hans Stamm
 */
public interface CommandInvoker {

    Command executeCommand(Command c);

    GenericDaoCommand executeCommand(GenericDaoCommand genericDaoCommand) throws SvmDbException;

}
