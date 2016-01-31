package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Schuljahre;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.SemestersTableModel;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.Converter.asString;

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
        if (!isBulkUpdate() && semester.getSemesterbeginn() != null && semester.getSemesterbeginn().get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            semester.setSemesterbeginn(null);
            invalidate();
            throw new SvmValidationException(2042, "Semesterbeginn muss ein Montag sein", Field.SEMESTERBEGINN);
        }
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
        if (!isBulkUpdate() && semester.getSemesterende() != null && semester.getSemesterende().get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            semester.setSemesterende(null);
            invalidate();
            throw new SvmValidationException(2044, "Semesterende muss ein Samstag sein", Field.SEMESTERENDE);
        }
        if (!isBulkUpdate() && semester.getSemesterbeginn() != null && semester.getSemesterende() != null && semester.getSemesterbeginn().after(semester.getSemesterende())) {
            semester.setSemesterende(null);
            invalidate();
            throw new SvmValidationException(2024, "Keine gültige Periode", Field.SEMESTERENDE);
        }
    }

    private CalendarModelAttribute ferienbeginn1ModelAttribute = new CalendarModelAttribute(
            this,
            Field.FERIENBEGINN1, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return semester.getFerienbeginn1();
                }

                @Override
                public void setValue(Calendar value) {
                    semester.setFerienbeginn1(value);
                }
            }
    );

    @Override
    public Calendar getFerienbeginn1() {
        return ferienbeginn1ModelAttribute.getValue();
    }

    @Override
    public void setFerienbeginn1(String ferienbeginn1) throws SvmValidationException {
        ferienbeginn1ModelAttribute.setNewValue(true, ferienbeginn1, isBulkUpdate());
        if (!isBulkUpdate() && semester.getFerienbeginn1() != null && semester.getFerienbeginn1().get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            semester.setFerienbeginn1(null);
            invalidate();
            throw new SvmValidationException(2045, "Ferienbeginn muss ein Montag sein", Field.FERIENBEGINN1);
        }
        if (!isBulkUpdate() && semester.getFerienbeginn1() != null && semester.getFerienende1() != null && semester.getFerienbeginn1().after(semester.getFerienende1())) {
            semester.setFerienbeginn1(null);
            invalidate();
            throw new SvmValidationException(2025, "Keine gültige Periode", Field.FERIENBEGINN1);
        }
    }

    private CalendarModelAttribute ferienende1ModelAttribute = new CalendarModelAttribute(
            this,
            Field.FERIENENDE1, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return semester.getFerienende1();
                }

                @Override
                public void setValue(Calendar value) {
                    semester.setFerienende1(value);
                }
            }
    );

    @Override
    public Calendar getFerienende1() {
        return ferienende1ModelAttribute.getValue();
    }

    @Override
    public void setFerienende1(String ferienende1) throws SvmValidationException {
        ferienende1ModelAttribute.setNewValue(true, ferienende1, isBulkUpdate());
        if (!isBulkUpdate() && semester.getFerienende1() != null && semester.getFerienende1().get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            semester.setFerienende1(null);
            invalidate();
            throw new SvmValidationException(2046, "Ferienende muss ein Samstag sein", Field.FERIENENDE1);
        }
        if (!isBulkUpdate() && semester.getFerienbeginn1() != null && semester.getFerienende1() != null && semester.getFerienbeginn1().after(semester.getFerienende1())) {
            semester.setFerienende1(null);
            invalidate();
            throw new SvmValidationException(2026, "Keine gültige Periode", Field.FERIENENDE1);
        }
    }

    private CalendarModelAttribute ferienbeginn2ModelAttribute = new CalendarModelAttribute(
            this,
            Field.FERIENBEGINN2, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return semester.getFerienbeginn2();
                }

                @Override
                public void setValue(Calendar value) {
                    semester.setFerienbeginn2(value);
                }
            }
    );

    @Override
    public Calendar getFerienbeginn2() {
        return ferienbeginn2ModelAttribute.getValue();
    }

    @Override
    public void setFerienbeginn2(String ferienbeginn2) throws SvmValidationException {
        ferienbeginn2ModelAttribute.setNewValue(false, ferienbeginn2, isBulkUpdate());
        if (!isBulkUpdate() && semester.getFerienbeginn2() != null && semester.getFerienbeginn2().get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            semester.setFerienbeginn2(null);
            invalidate();
            throw new SvmValidationException(2047, "Ferienbeginn muss ein Montag sein", Field.FERIENBEGINN2);
        }
        if (!isBulkUpdate() && semester.getFerienbeginn2() != null && semester.getFerienende2() != null && semester.getFerienbeginn2().after(semester.getFerienende2())) {
            semester.setFerienbeginn2(null);
            invalidate();
            throw new SvmValidationException(2027, "Keine gültige Periode", Field.FERIENBEGINN2);
        }
    }

    private CalendarModelAttribute ferienende2ModelAttribute = new CalendarModelAttribute(
            this,
            Field.FERIENENDE2, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return semester.getFerienende2();
                }

                @Override
                public void setValue(Calendar value) {
                    semester.setFerienende2(value);
                }
            }
    );

    @Override
    public Calendar getFerienende2() {
        return ferienende2ModelAttribute.getValue();
    }

    @Override
    public void setFerienende2(String ferienende2) throws SvmValidationException {
        ferienende2ModelAttribute.setNewValue(false, ferienende2, isBulkUpdate());
        if (!isBulkUpdate() && semester.getFerienende2() != null && semester.getFerienende2().get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            semester.setFerienende2(null);
            invalidate();
            throw new SvmValidationException(2044, "Ferienende muss ein Samstag sein", Field.FERIENENDE2);
        }
        if (!isBulkUpdate() && semester.getFerienbeginn2() != null && semester.getFerienende2() != null && semester.getFerienbeginn2().after(semester.getFerienende2())) {
            semester.setFerienende2(null);
            invalidate();
            throw new SvmValidationException(2028, "Keine gültige Periode", Field.FERIENENDE2);
        }
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
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckSemesterUeberlapptAndereSemesterCommand checkSemesterUeberlapptAndereSemesterCommand = new CheckSemesterUeberlapptAndereSemesterCommand(semester, semesterOrigin, svmModel.getSemestersAll());
        commandInvoker.executeCommand(checkSemesterUeberlapptAndereSemesterCommand);
        return checkSemesterUeberlapptAndereSemesterCommand.isUeberlappt();
    }

    @Override
    public Semester getNaechstesNochNichtErfasstesSemester(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        DetermineNaechstesNochNichtErfasstesSemesterCommand determineNaechstesNochNichtErfasstesSemesterCommand = new DetermineNaechstesNochNichtErfasstesSemesterCommand(svmModel.getSemestersAll());
        commandInvoker.executeCommand(determineNaechstesNochNichtErfasstesSemesterCommand);
        return determineNaechstesNochNichtErfasstesSemesterCommand.getNaechstesNochNichtErfasstesSemester();
    }

    @Override
    public boolean checkIfUpdateAffectsSemesterrechnungen() {
        return !(semesterOrigin == null || semester.getAnzahlSchulwochen() == semesterOrigin.getAnzahlSchulwochen() || semesterOrigin.getSemesterrechnungen().isEmpty());
    }

    @Override
    public void updateAnzWochenSemesterrechnungen() {
        CommandInvoker commandInvoker = getCommandInvoker();
        UpdateAnzWochenSemesterrechnungenCommand updateAnzWochenSemesterrechnungenCommand = new UpdateAnzWochenSemesterrechnungenCommand(semesterOrigin.getSemesterrechnungen(), semester);
        commandInvoker.executeCommandAsTransaction(updateAnzWochenSemesterrechnungenCommand);
    }

    @Override
    public void speichern(SvmModel svmModel, SemestersTableModel semestersTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateSemesterCommand saveOrUpdateSemesterCommand = new SaveOrUpdateSemesterCommand(semester, semesterOrigin, svmModel.getSemestersAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSemesterCommand);
        // TableData mit von der Datenbank upgedateten Semesters updaten
        semestersTableModel.getSemestersTableData().setSemesters(svmModel.getSemestersAll());
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
                setFerienbeginn1(asString(semesterOrigin.getFerienbeginn1()));
                setFerienende1(asString(semesterOrigin.getFerienende1()));
                setFerienbeginn2(asString(semesterOrigin.getFerienbeginn2()));
                setFerienende2(asString(semesterOrigin.getFerienende2()));
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
            throw new SvmValidationException(2031, "Datum liegt nicht im Schuljahr " + semester.getSchuljahr(), Field.SEMESTERBEGINN);
        }
        if (!isBulkUpdate() && semester.getSchuljahr() != null && semester.getSemesterende() != null && !semester.getSchuljahr().contains(Integer.toString(semester.getSemesterende().get(Calendar.YEAR)))) {
            throw new SvmValidationException(2032, "Datum liegt nicht im Schuljahr " + semester.getSchuljahr(), Field.SEMESTERENDE);
        }
        if (semester.getFerienbeginn1() != null && semester.getSemesterbeginn() != null && !semester.getFerienbeginn1().after(semester.getSemesterbeginn())) {
            throw new SvmValidationException(2061, "Ferienbeginn muss nach Semesterbeginn liegen", Field.FERIENBEGINN1);
        }
        if (semester.getFerienbeginn2() != null && semester.getSemesterbeginn() != null && !semester.getFerienbeginn2().after(semester.getSemesterbeginn())) {
            throw new SvmValidationException(2062, "Ferienbeginn muss nach Semesterbeginn liegen", Field.FERIENBEGINN2);
        }
        if (semester.getFerienende1() != null && semester.getSemesterende() != null && !semester.getFerienende1().before(semester.getSemesterende())) {
            throw new SvmValidationException(2063, "Ferienende muss vor Semesterende liegen", Field.FERIENENDE1);
        }
        if (semester.getFerienende2() != null && semester.getSemesterende() != null && !semester.getFerienende2().before(semester.getSemesterende())) {
            throw new SvmValidationException(2064, "Ferienende muss vor Semesterende liegen", Field.FERIENENDE2);
        }
    }
}
