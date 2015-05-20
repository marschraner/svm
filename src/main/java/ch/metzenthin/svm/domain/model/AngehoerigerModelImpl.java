package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveAngehoerigeCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Person;

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

    @Override
    public Angehoeriger getAngehoeriger() {
        return angehoeriger;
    }
}
