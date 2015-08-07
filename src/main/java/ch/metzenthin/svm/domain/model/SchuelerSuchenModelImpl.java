package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;

import java.sql.Time;
import java.text.ParseException;
import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.*;

/**
 * @author Martin Schraner
 */
final class SchuelerSuchenModelImpl extends PersonModelImpl implements SchuelerSuchenModel {

    private final PersonSuchen person = new PersonSuchen();
    private RolleSelected rolle;
    private AnmeldestatusSelected anmeldestatus;
    private DispensationSelected dispensation;
    private Calendar stichtag;
    private GeschlechtSelected geschlecht;
    private Calendar geburtsdatumSuchperiodeBeginn;
    private Calendar geburtsdatumSuchperiodeEnde;
    private String geburtsdatumSuchperiodeDateFormatString;
    private String schuljahrKurs;
    private Semesterbezeichnung semesterbezeichnung;
    private Wochentag wochentag;
    private Time zeitBeginn;
    private Lehrkraft lehrkraft;
    private boolean kursFuerSucheBeruecksichtigen;
    private Code code;

    SchuelerSuchenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    static {
        LEHRKRAFT_ALLE.setVorname("");
        LEHRKRAFT_ALLE.setNachname("");
        CODE_ALLE.setKuerzel("");
        CODE_ALLE.setBeschreibung("");
    }

    @Override
    public PersonSuchen getPerson() {
        return person;
    }

    @Override
    public String getGeburtsdatumSuchperiode() {
        if (geburtsdatumSuchperiodeBeginn == null && geburtsdatumSuchperiodeEnde == null) {
            return "";
        } else if (geburtsdatumSuchperiodeEnde == null) {
            return asString(getGeburtsdatumSuchperiodeBeginn(), geburtsdatumSuchperiodeDateFormatString);
        } else {
            return asString(getGeburtsdatumSuchperiodeBeginn(), geburtsdatumSuchperiodeDateFormatString) + " - " + asString(getGeburtsdatumSuchperiodeEnde(), geburtsdatumSuchperiodeDateFormatString);
        }
    }

    @Override
    public void setGeburtsdatumSuchperiode(String geburtsdatumSuchperiode) throws SvmValidationException {
        try {
            geburtsdatumSuchperiodeDateFormatString = getPeriodeDateFormatString(geburtsdatumSuchperiode);
            setGeburtsdatumSuchperiodeBeginn(getPeriodeBeginn(geburtsdatumSuchperiode));
            setGeburtsdatumSuchperiodeEnde(getPeriodeEnde(geburtsdatumSuchperiode));
        } catch (ParseException e) {
            invalidate();
            throw new SvmValidationException(1200, e.getMessage(), Field.GEBURTSDATUM_SUCHPERIODE);
        }
    }

    private final CalendarModelAttribute geburtsdatumSuchperiodeBeginnAttribute = new CalendarModelAttribute(
            this,
            Field.GEBURTSDATUM_SUCHPERIODE, new GregorianCalendar(1980, Calendar.JANUARY, 1), new GregorianCalendar(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return geburtsdatumSuchperiodeBeginn;
                }

                @Override
                public void setValue(Calendar value) {
                    geburtsdatumSuchperiodeBeginn = value;
                }
            }
    );

    @Override
    public Calendar getGeburtsdatumSuchperiodeBeginn() {
        return geburtsdatumSuchperiodeBeginnAttribute.getValue();
    }

    public void setGeburtsdatumSuchperiodeBeginn(String geburtsdatumSuchperiodeBeginn) throws SvmValidationException {
        geburtsdatumSuchperiodeBeginnAttribute.setNewValue(false, geburtsdatumSuchperiodeBeginn, geburtsdatumSuchperiodeDateFormatString, isBulkUpdate());
    }

    private final CalendarModelAttribute geburtsdatumSuchperiodeEndeAttribute = new CalendarModelAttribute(
            this,
            Field.GEBURTSDATUM_SUCHPERIODE, new GregorianCalendar(1980, Calendar.JANUARY, 1), new GregorianCalendar(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return geburtsdatumSuchperiodeEnde;
                }

                @Override
                public void setValue(Calendar value) {
                    geburtsdatumSuchperiodeEnde = value;
                }
            }
    );

    @Override
    public Calendar getGeburtsdatumSuchperiodeEnde() {
        return geburtsdatumSuchperiodeEndeAttribute.getValue();
    }

    public void setGeburtsdatumSuchperiodeEnde(String geburtsdatumSuchperiodeEnde) throws SvmValidationException {
        geburtsdatumSuchperiodeEndeAttribute.setNewValue(false, geburtsdatumSuchperiodeEnde, geburtsdatumSuchperiodeDateFormatString, isBulkUpdate());
    }

    @Override
    public String getGeburtsdatumSuchperiodeDateFormatString() {
        return geburtsdatumSuchperiodeDateFormatString;
    }

    @Override
    public GeschlechtSelected getGeschlecht() {
        return geschlecht;
    }

    @Override
    public void setGeschlecht(GeschlechtSelected geschlecht) {
        GeschlechtSelected oldValue = this.geschlecht;
        this.geschlecht = geschlecht;
        firePropertyChange(Field.GESCHLECHT, oldValue, this.geschlecht);
    }

    @Override
    public RolleSelected getRolle() {
        return rolle;
    }

    @Override
    public void setRolle(RolleSelected rolle) {
        RolleSelected oldValue = this.rolle;
        this.rolle = rolle;
        firePropertyChange(Field.ROLLE, oldValue, this.rolle);
    }

    @Override
    public AnmeldestatusSelected getAnmeldestatus() {
        return anmeldestatus;
    }

    public void setAnmeldestatus(AnmeldestatusSelected anmeldestatus) {
        AnmeldestatusSelected oldValue = this.anmeldestatus;
        this.anmeldestatus = anmeldestatus;
        firePropertyChange(Field.ANMELDESTATUS, oldValue, this.anmeldestatus);
    }

    @Override
    public DispensationSelected getDispensation() {
        return dispensation;
    }

    @Override
    public void setDispensation(DispensationSelected dispensation) {
        DispensationSelected oldValue = this.dispensation;
        this.dispensation = dispensation;
        firePropertyChange(Field.DISPENSATION, oldValue, this.dispensation);
    }

    @Override
    public Calendar getStichtag() {
        return stichtagModelAttribute.getValue();
    }

    @Override
    public void setStichtag(String stichtag) throws SvmValidationException {
        stichtagModelAttribute.setNewValue(true, stichtag, isBulkUpdate());
    }

    private final StringModelAttribute schuljahrKursModelAttribute = new StringModelAttribute(
            this,
            Field.SCHULJAHR_KURS, 9, 9,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schuljahrKurs;
                }

                @Override
                public void setValue(String value) {
                    schuljahrKurs = value;
                }
            }
    );

    @Override
    public String getSchuljahrKurs() {
        return schuljahrKursModelAttribute.getValue();
    }

    @Override
    public void setSchuljahrKurs(String schuljahrKurs) throws SvmValidationException {
        schuljahrKursModelAttribute.setNewValue(true, schuljahrKurs, isBulkUpdate());
    }

    @Override
    public Semesterbezeichnung getSemesterbezeichnung() {
        return semesterbezeichnung;
    }

    @Override
    public void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung) {
        Semesterbezeichnung oldValue = this.semesterbezeichnung;
        this.semesterbezeichnung = semesterbezeichnung;
        firePropertyChange(Field.SEMESTERBEZEICHNUNG, oldValue, this.semesterbezeichnung);
    }

    @Override
    public Wochentag getWochentag() {
        return wochentag;
    }

    @Override
    public void setWochentag(Wochentag wochentag) {
        Wochentag oldValue = this.wochentag;
        this.wochentag = wochentag;
        firePropertyChange(Field.WOCHENTAG, oldValue, this.wochentag);
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
        zeitBeginnModelAttribute.setNewValue(false, zeitBeginn, isBulkUpdate());
    }

    @Override
    public Lehrkraft getLehrkraft() {
        return lehrkraft;
    }

    @Override
    public void setLehrkraft(Lehrkraft lehrkraft) {
        Lehrkraft oldValue = this.lehrkraft;
        this.lehrkraft = lehrkraft;
        firePropertyChange(Field.LEHRKRAFT, oldValue, this.lehrkraft);
    }

    @Override
    public boolean isKursFuerSucheBeruecksichtigen() {
        return kursFuerSucheBeruecksichtigen;
    }

    @Override
    public void setKursFuerSucheBeruecksichtigen(boolean isSelected) {
        boolean oldValue = kursFuerSucheBeruecksichtigen;
        kursFuerSucheBeruecksichtigen = isSelected;
        firePropertyChange(Field.KURS_FUER_SUCHE_BERUECKSICHTIGEN, oldValue, kursFuerSucheBeruecksichtigen);
    }

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public void setCode(Code code) {
        Code oldValue = this.code;
        this.code = code;
        firePropertyChange(Field.CODE, oldValue, this.code);
    }

    private final CalendarModelAttribute stichtagModelAttribute = new CalendarModelAttribute(
            this,
            Field.STICHTAG, new GregorianCalendar(2000, Calendar.JANUARY, 1), new GregorianCalendar(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return stichtag;
                }

                @Override
                public void setValue(Calendar value) {
                    stichtag = value;
                }
            }
    );

    @Override
    public SchuelerSuchenTableData suchen(SvmModel svmModel) {
        SchuelerSuchenCommand schuelerSuchenCommand = new SchuelerSuchenCommand(this);
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.executeCommand(schuelerSuchenCommand);
        List<Schueler> schuelerList = schuelerSuchenCommand.getSchuelerFound();
        Semester semester = determineSemester(svmModel);
        Map<Schueler, List<Kurs>> kurseMapTableData = determineKurseMapTableData(schuelerList, semester, wochentag, zeitBeginn, lehrkraft);
        return new SchuelerSuchenTableData(schuelerList, kurseMapTableData, semester, (wochentag == Wochentag.ALLE ? null : wochentag), zeitBeginn, (lehrkraft == LEHRKRAFT_ALLE ? null : lehrkraft));
    }

    private Semester determineSemester(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        List<Semester> erfassteSemester = svmModel.getSemestersAll();
        if (kursFuerSucheBeruecksichtigen) {
            FindSemesterForSchuljahrSemesterbezeichnungCommand findSemesterForSchuljahrSemesterbezeichnungCommand = new FindSemesterForSchuljahrSemesterbezeichnungCommand(schuljahrKurs, semesterbezeichnung, erfassteSemester);
            commandInvoker.executeCommand(findSemesterForSchuljahrSemesterbezeichnungCommand);
            return findSemesterForSchuljahrSemesterbezeichnungCommand.getSemesterFound();
        } else {
            FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(stichtag, erfassteSemester);
            commandInvoker.executeCommand(findSemesterForCalendarCommand);
            Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
            Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
            // Wenn in Ferien zwischen 2 Semestern Folgesemester nehmen
            return  (currentSemester == null ? nextSemester : currentSemester);
        }
    }

    private Map<Schueler, List<Kurs>> determineKurseMapTableData(List<Schueler> schuelerList, Semester semester, Wochentag wochentag, Time zeitBeginn, Lehrkraft lehrkraft) {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindKurseMapSchuelerSemesterCommand findKurseMapSchuelerSemesterCommand = new FindKurseMapSchuelerSemesterCommand(schuelerList, semester, (wochentag == Wochentag.ALLE ? null : wochentag), zeitBeginn, (lehrkraft == LEHRKRAFT_ALLE ? null : lehrkraft));
        commandInvoker.executeCommand(findKurseMapSchuelerSemesterCommand);
        return findKurseMapSchuelerSemesterCommand.getKurseMap();
    }

    @Override
    public void invalidateGeburtsdatumSuchperiode() {
        geburtsdatumSuchperiodeBeginnAttribute.initValue("", geburtsdatumSuchperiodeDateFormatString);
    }

    @Override
    public Lehrkraft[] getSelectableLehrkraefte(SvmModel svmModel) {
        List<Lehrkraft> lehrkraefteList = svmModel.getAktiveLehrkraefteAll();
        // Lehrkraft alle auch erlaubt
        lehrkraefteList.add(0, LEHRKRAFT_ALLE);
        return lehrkraefteList.toArray(new Lehrkraft[lehrkraefteList.size()]);
    }

    @Override
    public Code[] getSelectableCodes(SvmModel svmModel) {
        List<Code> codesList = svmModel.getCodesAll();
        // Code alle auch erlaubt
        codesList.add(0, CODE_ALLE);
        return codesList.toArray(new Code[codesList.size()]);
    }

    @Override
    public boolean isCompleted() {
        return stichtag != null;
    }

    @Override
    public void doValidate() throws SvmValidationException {
        if (stichtag == null) {
            throw new SvmValidationException(2000, "Stichtag obligatorisch", Field.STICHTAG);
        }
    }

    @Override
    public boolean isAdresseRequired() {
        return false;
    }
}
