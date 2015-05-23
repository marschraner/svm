package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;

/**
 * @author Hans Stamm
 */
public class ModelFactoryImpl implements ModelFactory {

    private final CommandInvoker commandInvoker;

    public ModelFactoryImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    @Override
    public SchuelerModel createSchuelerModel() {
        return new SchuelerModelImpl(commandInvoker);
    }

    @Override
    public AngehoerigerModel createAngehoerigerModel() {
        return new AngehoerigerModelImpl(commandInvoker);
    }

    @Override
    public SchuelerErfassenModel createSchuelerErfassenModel() {
        return new SchuelerErfassenModelImpl(commandInvoker);
    }

}
