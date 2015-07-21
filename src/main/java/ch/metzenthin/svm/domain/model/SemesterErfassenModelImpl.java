package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Schuljahre;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckSemesterBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CheckSemesterUeberlapptAndereSemesterCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateSemesterCommand;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.getNumberOfWeeksBetween;

/**
 * @author Martin Schraner
 */
public class SemesterErfassenModelImpl extends AbstractModel implements SemesterErfassenModel {

    private Semester semester = new Semester();
    private Semester semesterOrigin;

    public SemesterErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public Semester getSemester() {
        return semester;
    }

    @Override
    public void setSemesterOrigin(Semester semesterOrigin) {
        this.semesterOrigin = semesterOrigin;
    }

    private final StringModelAttribute schuljahrModelAttribute = new StringModelAttribute(
            this,
            Field.SCHULJAHR, 9, 9,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return semester.getSchuljahr();
                }

                @Override
                public void setValue(String value) {
                    semester.setSchuljahr(value);
                }
            }
    );

    @Override
    public String getSchuljahr() {
        return schuljahrModelAttribute.getValue();
    }

    @Override
    public void setSchuljahr(String schuljahr) throws SvmValidationException {
        schuljahrModelAttribute.setNewValue(true, schuljahr, isBulkUpdate());
    }

    @Override
    public Semesterbezeichnung getSemesterbezeichnung() {
        return semester.getSemesterbezeichnung();
    }

    @Override
    public void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung) {
        Semesterbezeichnung oldValue = semester.getSemesterbezeichnung();
        semester.setSemesterbezeichnung(semesterbezeichnung);
        firePropertyChange(Field.SEMESTERBEZEICHNUNG, oldValue, semester.getSemesterbezeichnung());
    }

    private CalendarModelAttribute semesterbeginnModelAttribute = new CalendarModelAttribute(
            this,
            Field.SEMESTERBEGINN, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return semester.getSemesterbeginn();
                }

                @Override
                public void setValue(Calendar value) {
                    semester.setSemesterbeginn(value);
                }
            }
    );

    @Override
    public Calendar getSemesterbeginn() {
        return semesterbeginnModelAttribute.getValue();
    }

    @Override
    public void setSemesterbeginn(String semesterbeginn) throws SvmValidationException {
        semesterbeginnModelAttribute.setNewValue(true, semesterbeginn, isBulkUpdate());
        if (!isBulkUpdate() && semester.getSemesterbeginn() != null && semester.getSemesterende() != null && semester.getSemesterbeginn().after(semester.getSemesterende())) {
            semester.setSemesterbeginn(null);
            invalidate();
            throw new SvmValidationException(2022, "Keine gültige Periode", Field.SEMESTERBEGINN);
        }
    }

    private CalendarModelAttribute semesterendeModelAttribute = new CalendarModelAttribute(
            this,
            Field.SEMESTERENDE, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return semester.getSemesterende();
                }

                @Override
                public void setValue(Calendar value) {
                    semester.setSemesterende(value);
                }
            }
    );

    @Override
    public Calendar getSemesterende() {
        return semesterendeModelAttribute.getValue();
    }

    @Override
    public void setSemesterende(String semesterende) throws SvmValidationException {
        semesterendeModelAttribute.setNewValue(true, semesterende, isBulkUpdate());
        if (!isBulkUpdate() && semester.getSemesterbeginn() != null && semester.getSemesterende() != null && semester.getSemesterbeginn().after(semester.getSemesterende())) {
            semester.setSemesterende(null);
            invalidate();
            throw new SvmValidationException(2024, "Keine gültige Periode", Field.SEMESTERENDE);
        }
    }

    private final IntegerModelAttribute anzahlSchulwochenModelAttribute = new IntegerModelAttribute(
            this,
            Field.ANZAHL_SCHULWOCHEN, 10, 50,
            new AttributeAccessor<Integer>() {
                @Override
                public Integer getValue() {
                    return semester.getAnzahlSchulwochen();
                }

                @Override
                public void setValue(Integer value) {
                    semester.setAnzahlSchulwochen(value);
                }
            }
    );

    @Override
    public Integer getAnzahlSchulwochen() {
        return anzahlSchulwochenModelAttribute.getValue();
    }

    @Override
    public void setAnzahlSchulwochen(String anzahlSchulwochen) throws SvmValidationException {
        anzahlSchulwochenModelAttribute.setNewValue(true, anzahlSchulwochen, isBulkUpdate());
    }

    @Override
    public boolean checkSemesterBereitsErfasst(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckSemesterBereitsErfasstCommand checkSemesterBereitsErfasstCommand = new CheckSemesterBereitsErfasstCommand(semester, semesterOrigin, svmModel.getSemestersAll());
        commandInvoker.executeCommand(checkSemesterBereitsErfasstCommand);
        return checkSemesterBereitsErfasstCommand.isBereitsErfasst();
    }

    @Override
    public boolean checkSemesterUeberlapptAndereSemester(SvmModel svmModel) {
        CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand = new CheckSemesterUeberlapptAndereSemesterCommand(semester, semesterOrigin, svmModel.getSemestersAll());
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
        return checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt();
    }

    @Override
    public void speichern(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester, semesterOrigin, svmModel.getSemestersAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
    }

    @Override
    public void initializeCompleted() {
        if (semesterOrigin != null) {
            setBulkUpdate(true);
            try {
                setSchuljahr(semesterOrigin.getSchuljahr());
                setSemesterbezeichnung(semesterOrigin.getSemesterbezeichnung());
                setSemesterbeginn(asString(semesterOrigin.getSemesterbeginn()));
                setSemesterende(asString(semesterOrigin.getSemesterende()));
                setAnzahlSchulwochen(Integer.toString(semesterOrigin.getAnzahlSchulwochen()));
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
    void doValidate() throws SvmValidationException {
        if (!isBulkUpdate() && semester.getSchuljahr() != null && semester.getSemesterbeginn() != null && !semester.getSchuljahr().contains(Integer.toString(semester.getSemesterbeginn().get(Calendar.YEAR)))) {
            throw new SvmValidationException(2023, "Datum liegt nicht im Schuljahr " + semester.getSchuljahr(), Field.SEMESTERBEGINN);
        }
        if (!isBulkUpdate() && semester.getSchuljahr() != null && semester.getSemesterende() != null && !semester.getSchuljahr().contains(Integer.toString(semester.getSemesterende().get(Calendar.YEAR)))) {
            throw new SvmValidationException(2025, "Datum liegt nicht im Schuljahr " + semester.getSchuljahr(), Field.SEMESTERENDE);
        }
        int anzahlSchulwochenMaxValid = getNumberOfWeeksBetween(semester.getSemesterbeginn(), semester.getSemesterende());
        if (!isBulkUpdate() && (semester.getAnzahlSchulwochen() > anzahlSchulwochenMaxValid)) {
            throw new SvmValidationException(2026, "Wert darf nicht grösser als  " + anzahlSchulwochenMaxValid + " sein", Field.ANZAHL_SCHULWOCHEN);
        }
    }
}
