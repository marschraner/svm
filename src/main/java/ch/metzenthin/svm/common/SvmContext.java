package ch.metzenthin.svm.common;

import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.model.ModelFactory;
import ch.metzenthin.svm.domain.model.SvmModel;

/**
 * @author Hans Stamm
 */
public class SvmContext {

    private final ModelFactory modelFactory;
    private final CommandInvoker commandInvoker;
    private final SvmModel svmModel;

    public SvmContext(ModelFactory modelFactory, CommandInvoker commandInvoker, SvmModel svmModel) {
        this.modelFactory = modelFactory;
        this.commandInvoker = commandInvoker;
        this.svmModel = svmModel;
    }

    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    public SvmModel getSvmModel() {
        return svmModel;
    }
}
