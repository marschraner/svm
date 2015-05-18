package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.commands.CommandInvoker;

/**
 * @author Hans Stamm
 */
public class ModelFactoryImpl implements ModelFactory {

    protected CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    private final CommandInvoker commandInvoker;

    public ModelFactoryImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public SchuelerModel createSchuelerModel() {
        return new SchuelerModelImpl(commandInvoker);
    }

}
