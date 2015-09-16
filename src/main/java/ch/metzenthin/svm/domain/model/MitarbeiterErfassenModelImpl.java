package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckMitarbeiterBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateMitarbeiterCommand;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.Converter.getNYearsBeforeNow;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class MitarbeiterErfassenModelImpl extends PersonModelImpl implements MitarbeiterErfassenModel {

    private Mitarbeiter mitarbeiter = new Mitarbeiter();
    private Mitarbeiter mitarbeiterOrigin;

    public MitarbeiterErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    Person getPerson() {
        return mitarbeiter;
    }

    @Override
    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    @Override
    public void setMitarbeiterOrigin(Mitarbeiter mitarbeiterOrigin) {
        this.mitarbeiterOrigin = mitarbeiterOrigin;
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
                    return mitarbeiter.getAhvNummer();
                }

                @Override
                public void setValue(String value) {
                    mitarbeiter.setAhvNummer(value);
                }
            }
    );

    @Override
    public String getAhvNummer() {
        return ahvNummerModelAttribute.getValue();
    }

    @Override
    public void setAhvNummer(String ahvNummer) throws SvmValidationException {
        ahvNummerModelAttribute.setNewValue(false, ahvNummer, isBulkUpdate());
    }

    private final StringModelAttribute vertretungsmoeglichkeitenModelAttribute = new StringModelAttribute(
            this,
            Field.VERTRETUNGSMOEGLICHKEITEN, 2, 100,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return mitarbeiter.getVertretungsmoeglichkeiten();
                }

                @Override
                public void setValue(String value) {
                    mitarbeiter.setVertretungsmoeglichkeiten(value);
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
        Boolean oldValue = mitarbeiter.getAktiv();
        mitarbeiter.setAktiv(isSelected);
        firePropertyChange(Field.AKTIV, oldValue, isSelected);
    }

    @Override
    public Boolean isAktiv() {
        return mitarbeiter.getAktiv();
    }

    @Override
    public boolean checkMitarbeiterBereitsErfasst(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckMitarbeiterBereitsErfasstCommand checkMitarbeiterBereitsErfasstCommand = new CheckMitarbeiterBereitsErfasstCommand(mitarbeiter, mitarbeiterOrigin, svmModel.getMitarbeitersAll());
        commandInvoker.executeCommand(checkMitarbeiterBereitsErfasstCommand);
        return checkMitarbeiterBereitsErfasstCommand.isBereitsErfasst();
    }

    @Override
    public void speichern(SvmModel svmModel, MitarbeitersTableModel mitarbeitersTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter, getAdresse(), mitarbeiterOrigin, svmModel.getMitarbeitersAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);
        // TableData mit von der Datenbank upgedateten Mitarbeitern updaten
        mitarbeitersTableModel.getMitarbeitersTableData().setMitarbeiters(svmModel.getMitarbeitersAll());
    }

    @Override
    public void initializeCompleted() {
        if (mitarbeiterOrigin != null) {
            setBulkUpdate(true);
            try {
                setAnrede(mitarbeiterOrigin.getAnrede());
                setNachname(mitarbeiterOrigin.getNachname());
                setVorname(mitarbeiterOrigin.getVorname());
                setStrasseHausnummer(mitarbeiterOrigin.getAdresse().getStrasseHausnummer());
                setPlz(mitarbeiterOrigin.getAdresse().getPlz());
                setOrt(mitarbeiterOrigin.getAdresse().getOrt());
                setFestnetz(mitarbeiterOrigin.getFestnetz());
                setNatel(mitarbeiterOrigin.getNatel());
                setEmail(mitarbeiterOrigin.getEmail());
                setGeburtsdatum(asString(mitarbeiterOrigin.getGeburtsdatum()));
                setAhvNummer(mitarbeiterOrigin.getAhvNummer());
                setVertretungsmoeglichkeiten(mitarbeiterOrigin.getVertretungsmoeglichkeiten());
                setAktiv(!mitarbeiterOrigin.getAktiv()); // damit PropertyChange ausgelöst wird!
                setAktiv(mitarbeiterOrigin.getAktiv());
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
