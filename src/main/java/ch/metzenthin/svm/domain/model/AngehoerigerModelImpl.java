package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveAngehoerigeCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Person;

import java.util.Collections;

/**
 * @author Hans Stamm
 */
public class AngehoerigerModelImpl extends PersonModelImpl implements AngehoerigerModel {

    private Angehoeriger angehoeriger;

    private boolean isRechnungsempfaenger;

    AngehoerigerModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        angehoeriger = new Angehoeriger();
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted();
    }

    @Override
    public void save() {
        SaveAngehoerigeCommand saveAngehoerigeCommand = new SaveAngehoerigeCommand(Collections.singletonList(angehoeriger));
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

    @Override
    public boolean isRechnungsempfaenger() {
        return isRechnungsempfaenger;
    }

    /**
     * Für Angehörige ist die Adresse obligatorisch, falls er Rechnungsempfänger ist.
     * @return true, falls der Angehörige Rechnungsempfänger ist
     */
    @Override
    public boolean isAdresseRequired() {
        return isRechnungsempfaenger;
    }

    @Override
    public void setIsRechnungsempfaenger(boolean isSelected) {
        boolean oldValue = isRechnungsempfaenger;
        isRechnungsempfaenger = isSelected;
        firePropertyChange("Rechnungsempfaenger", oldValue, isRechnungsempfaenger);
    }

}
