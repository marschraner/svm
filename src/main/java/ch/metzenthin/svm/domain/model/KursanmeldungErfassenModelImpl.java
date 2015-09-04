package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.FindKursCommand;
import ch.metzenthin.svm.domain.commands.FindSemesterForCalendarCommand;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateKursanmeldungCommand;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class KursanmeldungErfassenModelImpl extends AbstractModel implements KursanmeldungErfassenModel {

    private Semester semester;
    private Wochentag wochentag;
    private Time zeitBeginn;
    private Lehrkraft lehrkraft;
    private Kurs kurs;
    private Kursanmeldung kursanmeldung = new Kursanmeldung();
    private Kursanmeldung kursanmeldungOrigin;

    public KursanmeldungErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public void setKursanmeldungOrigin(Kursanmeldung kursanmeldungOrigin) {
        this.kursanmeldungOrigin = kursanmeldungOrigin;
    }

    @Override
    public Semester getSemester() {
        return semester;
    }

    @Override
    public void setSemester(Semester semester) {
        Semester oldValue = this.semester;
        this.semester = semester;
        firePropertyChange(Field.SEMESTER, oldValue, this.semester);
    }

    @Override
    public Wochentag getWochentag() {
        return wochentag;
    }

    @Override
    public void setWochentag(Wochentag wochentag) throws SvmRequiredException {
        Wochentag oldValue = this.wochentag;
        this.wochentag = wochentag;
        firePropertyChange(Field.WOCHENTAG, oldValue, this.wochentag);
        if (wochentag == null) {
            invalidate();
            throw new SvmRequiredException(Field.WOCHENTAG);
        }
    }

    private final TimeModelAttribute zeitBeginnModelAttribute = new TimeModelAttribute(
            this,
            Field.ZEIT_BEGINN,
            new AttributeAccessor<Time>() {
                @Override
                public Time getValue() {
                    return zeitBeginn;
                }

                @Override
                public void setValue(Time value) {
                    zeitBeginn = value;
                }
            }
    );

    @Override
    public Time getZeitBeginn() {
        return zeitBeginnModelAttribute.getValue();
    }

    @Override
    public void setZeitBeginn(String zeitBeginn) throws SvmValidationException {
        zeitBeginnModelAttribute.setNewValue(true, zeitBeginn, isBulkUpdate());
    }

    @Override
    public Lehrkraft getLehrkraft() {
        return lehrkraft;
    }

    @Override
    public void setLehrkraft(Lehrkraft lehrkraft) throws SvmRequiredException {
        Lehrkraft oldValue = this.lehrkraft;
        this.lehrkraft = lehrkraft;
        firePropertyChange(Field.LEHRKRAFT, oldValue, this.lehrkraft);
        if (lehrkraft == null) {
            invalidate();
            throw new SvmRequiredException(Field.LEHRKRAFT);
        }
    }

    @Override
    public void setAbmeldungPerEndeSemester(Boolean isSelected) {
        Boolean oldValue = kursanmeldung.getAbmeldungPerEndeSemester();
        kursanmeldung.setAbmeldungPerEndeSemester(isSelected);
        firePropertyChange(Field.ABMELDUNG_PER_ENDE_SEMESTER, oldValue, isSelected);
    }

    @Override
    public Boolean isAbmeldungPerEndeSemester() {
        return kursanmeldung.getAbmeldungPerEndeSemester();
    }

    private final StringModelAttribute bemerkungenModelAttribute = new StringModelAttribute(
            this,
            Field.BEMERKUNGEN, 2, 100,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return kursanmeldung.getBemerkungen();
                }

                @Override
                public void setValue(String value) {
                    kursanmeldung.setBemerkungen(value);
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
    public Semester[] getSelectableSemesterKursanmeldungOrigin() {
        return new Semester[]{kursanmeldungOrigin.getKurs().getSemester()};
    }

    @Override
    public Lehrkraft[] getSelectableLehrkraftKursanmeldungOrigin() {
        return new Lehrkraft[]{kursanmeldungOrigin.getKurs().getLehrkraefte().get(0)};
    }

    @Override
    public Semester getInitSemester(SvmModel svmModel) {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmModel.getSemestersAll());
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        Calendar dayToShowNextSemster = new GregorianCalendar();
        dayToShowNextSemster.add(Calendar.DAY_OF_YEAR, 40);
        Semester initSemester;
        if (currentSemester == null) {
            // Ferien zwischen 2 Semestern
            initSemester = nextSemester;
        } else if (dayToShowNextSemster.after(currentSemester.getSemesterende()) && nextSemester != null) {
            // weniger als 40 Tage vor Semesterende
            initSemester = nextSemester;
        } else {
            // Neues Semester noch nicht erfasst
            initSemester = currentSemester;
        }
        if (initSemester != null) {
            return initSemester;
        } else {
            return svmModel.getSemestersAll().get(0);
        }
    }

    @Override
    public boolean checkIfSemesterIsInPast() {
        Calendar today = new GregorianCalendar();
        return semester.getSemesterende().before(today);
    }
    
    @Override
    public FindKursCommand.Result findKurs() {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindKursCommand findKursCommand = new FindKursCommand(semester, wochentag, zeitBeginn, lehrkraft);
        commandInvoker.executeCommand(findKursCommand);
        FindKursCommand.Result result = findKursCommand.getResult();
        if (result == FindKursCommand.Result.KURS_GEFUNDEN) {
            kurs = findKursCommand.getKursFound();
        }
        return result;
    }
    
    @Override
    public boolean checkIfKursBereitsErfasst(SchuelerDatenblattModel schuelerDatenblattModel) {
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungen()) {
            if (kursanmeldung.getKurs().isIdenticalWith(kurs)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void speichern(KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        kursanmeldung.setKurs(kurs);
        kursanmeldung.setSchueler(schuelerDatenblattModel.getSchueler());
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateKursanmeldungCommand saveOrUpdateKursanmeldungCommand = new SaveOrUpdateKursanmeldungCommand(kursanmeldung, kursanmeldungOrigin, schuelerDatenblattModel.getSchueler().getKursanmeldungenAsList());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursanmeldungCommand);
        // TableData mit von der Datenbank upgedateter Kursanmeldung updaten
        kursanmeldungenTableModel.getKursanmeldungenTableData().setKursanmeldungen(schuelerDatenblattModel.getSchueler().getKursanmeldungenAsList());
    }

    @Override
    public void initializeCompleted() {
        if (kursanmeldungOrigin != null) {
            setBulkUpdate(true);
            try {
                setSemester(kursanmeldungOrigin.getKurs().getSemester());
                setWochentag(kursanmeldungOrigin.getKurs().getWochentag());
                setZeitBeginn(asString(kursanmeldungOrigin.getKurs().getZeitBeginn()));
                setLehrkraft(kursanmeldungOrigin.getKurs().getLehrkraefte().get(0));
                kurs = kursanmeldungOrigin.getKurs();
                setAbmeldungPerEndeSemester(!kursanmeldungOrigin.getAbmeldungPerEndeSemester());   // damit PropertyChange ausgelöst wird!
                setAbmeldungPerEndeSemester(kursanmeldungOrigin.getAbmeldungPerEndeSemester());
                setBemerkungen(kursanmeldungOrigin.getBemerkungen());
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
    void doValidate() throws SvmValidationException {}
}
