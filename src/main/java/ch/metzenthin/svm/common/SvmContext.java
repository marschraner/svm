package ch.metzenthin.svm.common;

import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.model.ModelFactory;

/**
 * @author Hans Stamm
 */
public class SvmContext {

    private final ModelFactory modelFactory;
    private final CommandInvoker commandInvoker;

    public SvmContext(ModelFactory modelFactory, CommandInvoker commandInvoker) {
        this.modelFactory = modelFactory;
        this.commandInvoker = commandInvoker;
    }

    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

}
