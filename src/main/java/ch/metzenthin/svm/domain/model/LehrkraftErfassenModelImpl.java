package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckLehrkraftBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateLehrkraftCommand;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Person;

import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.Converter.getNYearsBeforeNow;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class LehrkraftErfassenModelImpl extends PersonModelImpl implements LehrkraftErfassenModel {

    private Lehrkraft lehrkraft = new Lehrkraft();
    private Lehrkraft lehrkraftOrigin;

    public LehrkraftErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    Person getPerson() {
        return lehrkraft;
    }

    @Override
    public Lehrkraft getLehrkraft() {
        return lehrkraft;
    }

    @Override
    public void setLehrkraftOrigin(Lehrkraft lehrkraftOrigin) {
        this.lehrkraftOrigin = lehrkraftOrigin;
    }

    @Override
    public void setEmail(String email) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(email)) {
            invalidate();
            throw new SvmRequiredException(Field.EMAIL);
        }
        super.setEmail(email);
    }

    @Override
    protected Calendar getEarliestValidDateGeburtstag() {
        return getNYearsBeforeNow(80);
    }

    @Override
    protected Calendar getLatestValidDateGeburtstag() {
        return getNYearsBeforeNow(10);
    }

    @Override
    public void setGeburtsdatum(String geburtsdatum) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(geburtsdatum)) {
            invalidate();
            throw new SvmRequiredException(Field.GEBURTSDATUM);
        }
        super.setGeburtsdatum(geburtsdatum);
    }

    private final StringModelAttribute ahvNummerModelAttribute = new StringModelAttribute(
            this,
            Field.AHV_NUMMER, 16, 16,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return lehrkraft.getAhvNummer();
                }

                @Override
                public void setValue(String value) {
                    lehrkraft.setAhvNummer(value);
                }
            }
    );

    @Override
    public String getAhvNummer() {
        return ahvNummerModelAttribute.getValue();
    }

    @Override
    public void setAhvNummer(String ahvNummer) throws SvmValidationException {
        ahvNummerModelAttribute.setNewValue(true, ahvNummer, isBulkUpdate());
    }

    private final StringModelAttribute vertretungsmoeglichkeitenModelAttribute = new StringModelAttribute(
            this,
            Field.VERTRETUNGSMOEGLICHKEITEN, 2, 100,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return lehrkraft.getVertretungsmoeglichkeiten();
                }

                @Override
                public void setValue(String value) {
                    lehrkraft.setVertretungsmoeglichkeiten(value);
                }
            }
    );

    @Override
    public String getVertretungsmoeglichkeiten() {
        return vertretungsmoeglichkeitenModelAttribute.getValue();
    }

    @Override
    public void setVertretungsmoeglichkeiten(String vertretungsmoeglichkeiten) throws SvmValidationException {
        vertretungsmoeglichkeitenModelAttribute.setNewValue(false, vertretungsmoeglichkeiten, isBulkUpdate());
    }

    @Override
    public void setAktiv(Boolean isSelected) {
        Boolean oldValue = lehrkraft.isAktiv();
        lehrkraft.setAktiv(isSelected);
        firePropertyChange(Field.AKTIV, oldValue, isSelected);
    }

    @Override
    public Boolean isAktiv() {
        return lehrkraft.isAktiv();
    }

    @Override
    public boolean checkLehrkraftBereitsErfasst(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckLehrkraftBereitsErfasstCommand checkLehrkraftBereitsErfasstCommand = new CheckLehrkraftBereitsErfasstCommand(lehrkraft, lehrkraftOrigin, svmModel.getLehrkraefteAll());
        commandInvoker.executeCommand(checkLehrkraftBereitsErfasstCommand);
        return checkLehrkraftBereitsErfasstCommand.isBereitsErfasst();
    }

    @Override
    public void speichern(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateLehrkraftCommand saveOrUpdateLehrkraftCommand = new SaveOrUpdateLehrkraftCommand(lehrkraft, getAdresse(), lehrkraftOrigin, svmModel.getLehrkraefteAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLehrkraftCommand);
    }

    @Override
    public void initializeCompleted() {
        if (lehrkraftOrigin != null) {
            setBulkUpdate(true);
            try {
                setAnrede(lehrkraftOrigin.getAnrede());
                setNachname(lehrkraftOrigin.getNachname());
                setVorname(lehrkraftOrigin.getVorname());
                setStrasseHausnummer(lehrkraftOrigin.getAdresse().getStrasseHausnummer());
                setPlz(lehrkraftOrigin.getAdresse().getPlz());
                setOrt(lehrkraftOrigin.getAdresse().getOrt());
                setFestnetz(lehrkraftOrigin.getFestnetz());
                setNatel(lehrkraftOrigin.getNatel());
                setEmail(lehrkraftOrigin.getEmail());
                setGeburtsdatum(asString(lehrkraftOrigin.getGeburtsdatum()));
                setAhvNummer(lehrkraftOrigin.getAhvNummer());
                setVertretungsmoeglichkeiten(lehrkraftOrigin.getVertretungsmoeglichkeiten());
                setAktiv(!lehrkraftOrigin.isAktiv()); // damit PropertyChange ausgelöst wird!
                setAktiv(lehrkraftOrigin.isAktiv());
            } catch (SvmValidationException ignore) {
                ignore.printStackTrace();
            }
            setBulkUpdate(false);
        } else {
            super.initializeCompleted();
        }
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public void doValidate() throws SvmValidationException {}

    @Override
    public boolean isAdresseRequired() {
        return true;
    }
}
