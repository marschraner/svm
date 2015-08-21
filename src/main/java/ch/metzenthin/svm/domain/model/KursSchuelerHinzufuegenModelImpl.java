package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.AddKursToSchuelerAndSaveCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.FindSemesterForCalendarCommand;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Martin Schraner
 */
public class KursSchuelerHinzufuegenModelImpl extends AbstractModel implements KursSchuelerHinzufuegenModel {

    private Semester semester;
    private Wochentag wochentag;
    private Time zeitBeginn;
    private Lehrkraft lehrkraft;

    public KursSchuelerHinzufuegenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
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
    public Semester getInitSemester(SvmModel svmModel) {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmModel.getSemestersAll());
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        Calendar dayToShowNextSemster = new GregorianCalendar();
        dayToShowNextSemster.add(Calendar.DAY_OF_YEAR, 40);
        Semester defaultSemester;
        if (currentSemester == null) {
            // Ferien zwischen 2 Semestern
            defaultSemester = nextSemester;
        } else if (dayToShowNextSemster.after(currentSemester.getSemesterende()) && nextSemester != null) {
            // weniger als 40 Tage vor Semesterende
            defaultSemester = nextSemester;
        } else {
            // Neues Semester noch nicht erfasst
            defaultSemester = currentSemester;
        }
        if (defaultSemester != null) {
            return defaultSemester;
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
    public AddKursToSchuelerAndSaveCommand.Result hinzufuegen(KurseTableModel kurseTableModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        CommandInvoker commandInvoker = getCommandInvoker();
        AddKursToSchuelerAndSaveCommand addKursToSchuelerAndSaveCommand = new AddKursToSchuelerAndSaveCommand(semester, wochentag, zeitBeginn, lehrkraft, schueler);
        commandInvoker.executeCommandAsTransaction(addKursToSchuelerAndSaveCommand);
        Schueler schuelerUpdated = addKursToSchuelerAndSaveCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedatetem Schüler updaten
        if (schuelerUpdated != null) {
            kurseTableModel.getKurseTableData().setKurse(schuelerUpdated.getKurseAsList());
        }
        return addKursToSchuelerAndSaveCommand.getResult();
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {}
}
