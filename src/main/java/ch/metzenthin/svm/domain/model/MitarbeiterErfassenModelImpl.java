package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckMitarbeiterBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateMitarbeiterCommand;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.Converter.getNYearsBeforeNow;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class MitarbeiterErfassenModelImpl extends PersonModelImpl implements MitarbeiterErfassenModel {

    private Mitarbeiter mitarbeiter = new Mitarbeiter();
    private Mitarbeiter mitarbeiterOrigin;
    private Set<MitarbeiterCode> mitarbeiterCodes = new HashSet<>();

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
    public Set<MitarbeiterCode> getMitarbeiterCodes() {
        return mitarbeiterCodes;
    }

    @Override
    public List<MitarbeiterCode> getMitarbeiterCodesAsList() {
        List<MitarbeiterCode> mitarbeiterCodesAsList = new ArrayList<>(mitarbeiterCodes);
        Collections.sort(mitarbeiterCodesAsList);
        return  mitarbeiterCodesAsList;
    }

    @Override
    public CodesTableData getCodesTableData() {
        return new CodesTableData(getMitarbeiterCodesAsList(), true);
    }

    @Override
    public void setMitarbeiterOrigin(Mitarbeiter mitarbeiterOrigin) {
        this.mitarbeiterOrigin = mitarbeiterOrigin;
    }

    @Override
    public void setNachname(String nachname) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(nachname)) {
            invalidate();
            throw new SvmRequiredException(Field.NACHNAME);
        }
        super.setNachname(nachname);
    }

    @Override
    public void setVorname(String vorname) throws SvmValidationException {
        if (!isBulkUpdate() && !checkNotEmpty(vorname)) {
            invalidate();
            throw new SvmRequiredException(Field.VORNAME);
        }
        super.setVorname(vorname);
    }


    @Override
    protected Calendar getEarliestValidDateGeburtstag() {
        return getNYearsBeforeNow(80);
    }

    @Override
    protected Calendar getLatestValidDateGeburtstag() {
        return getNYearsBeforeNow(10);
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

    @Override
    public void setLehrkraft(Boolean isSelected) {
        Boolean oldValue = mitarbeiter.getLehrkraft();
        mitarbeiter.setLehrkraft(isSelected);
        firePropertyChange(Field.LEHRKRAFT, oldValue, isSelected);
    }

    @Override
    public Boolean isLehrkraft() {
        return mitarbeiter.getLehrkraft();
    }

    private final StringModelAttribute vertretungsmoeglichkeitenModelAttribute = new StringModelAttribute(
            this,
            Field.VERTRETUNGSMOEGLICHKEITEN, 0, 1000,
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

    private final StringModelAttribute bemerkungenModelAttribute = new StringModelAttribute(
            this,
            Field.BEMERKUNGEN, 0, 1000,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return mitarbeiter.getBemerkungen();
                }

                @Override
                public void setValue(String value) {
                    mitarbeiter.setBemerkungen(value);
                }
            }
    );

    @Override
    public String getBemerkungen() {
        return bemerkungenModelAttribute.getValue();
    }

    @Override
    public void setBemerkungen(String bemerkungen) throws SvmValidationException {
        bemerkungenModelAttribute.setNewValue(false, bemerkungen, isBulkUpdate());
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
    public String getCodesAsStr() {
        if (mitarbeiterCodes.isEmpty()) {
            return "-";
        }
        StringBuilder mitarbeiterCodesAsStr = new StringBuilder(getMitarbeiterCodesAsList().get(0).getKuerzel());
        for (int i = 1; i < getMitarbeiterCodesAsList().size(); i++) {
            mitarbeiterCodesAsStr.append(", ").append(getMitarbeiterCodesAsList().get(i).getKuerzel());
        }
        return mitarbeiterCodesAsStr.toString();
    }

    @Override
    public String getCodesBearbeitenTitle() {
        String title = "Mitarbeiter-Codes";
        if (checkNotEmpty(mitarbeiter.getNachname()) && checkNotEmpty(mitarbeiter.getVorname())) {
            title = title + " " + mitarbeiter.getVorname() + " " + mitarbeiter.getNachname();
        }
        return title;
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
        SaveOrUpdateMitarbeiterCommand saveOrUpdateMitarbeiterCommand = new SaveOrUpdateMitarbeiterCommand(mitarbeiter, getAdresse(), mitarbeiterCodes, mitarbeiterOrigin, svmModel.getMitarbeitersAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMitarbeiterCommand);
        saveOrUpdateMitarbeiterCommand.getMitarbeiterSaved();
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
                if (mitarbeiterOrigin.getAdresse() != null) {
                    setStrasseHausnummer(mitarbeiterOrigin.getAdresse().getStrasseHausnummer());
                    setPlz(mitarbeiterOrigin.getAdresse().getPlz());
                    setOrt(mitarbeiterOrigin.getAdresse().getOrt());
                }
                setFestnetz(mitarbeiterOrigin.getFestnetz());
                setNatel(mitarbeiterOrigin.getNatel());
                setEmail(mitarbeiterOrigin.getEmail());
                setGeburtsdatum(asString(mitarbeiterOrigin.getGeburtsdatum()));
                setAhvNummer(mitarbeiterOrigin.getAhvNummer());
                setLehrkraft(!mitarbeiterOrigin.getLehrkraft()); // damit PropertyChange ausgelöst wird!
                setLehrkraft(mitarbeiterOrigin.getLehrkraft());
                setVertretungsmoeglichkeiten(mitarbeiterOrigin.getVertretungsmoeglichkeiten());
                setBemerkungen(mitarbeiterOrigin.getBemerkungen());
                setAktiv(!mitarbeiterOrigin.getAktiv()); // damit PropertyChange ausgelöst wird!
                setAktiv(mitarbeiterOrigin.getAktiv());
                for (MitarbeiterCode mitarbeiterCode : mitarbeiterOrigin.getMitarbeiterCodes()) {
                    mitarbeiterCodes.add(mitarbeiterCode);
                }
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
        return super.isCompleted();
    }

    @Override
    public void doValidate() throws SvmValidationException {
        super.doValidate();
    }

    @Override
    public boolean isAdresseRequired() {
        return false;
    }
}
