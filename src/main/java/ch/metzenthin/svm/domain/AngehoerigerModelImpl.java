package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.commands.CommandInvoker;
import ch.metzenthin.svm.commands.SaveAngehoerigeCommand;
import ch.metzenthin.svm.model.entities.Angehoeriger;
import ch.metzenthin.svm.model.entities.Person;

import java.util.Arrays;

/**
 * @author Hans Stamm
 */
public class AngehoerigerModelImpl extends PersonModelImpl implements AngehoerigerModel {

    private Angehoeriger angehoeriger;

    AngehoerigerModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        angehoeriger = new Angehoeriger();
    }

    @Override
    public String getBeruf() {
        return angehoeriger.getBeruf();
    }

    @Override
    public void setBeruf(String beruf) {
        angehoeriger.setBeruf(beruf);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public void save() {
        SaveAngehoerigeCommand saveAngehoerigeCommand = new SaveAngehoerigeCommand(Arrays.asList(angehoeriger));
        getCommandInvoker().executeCommand(saveAngehoerigeCommand);
    }

    @Override
    Person getPerson() {
        return angehoeriger;
    }
}
