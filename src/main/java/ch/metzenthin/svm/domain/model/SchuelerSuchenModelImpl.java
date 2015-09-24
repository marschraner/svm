package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

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
    private Semester semesterKurs;
    private Wochentag wochentag;
    private Time zeitBeginn;
    private Mitarbeiter mitarbeiter;
    private boolean kursFuerSucheBeruecksichtigen;
    private Maerchen maerchen;
    private Gruppe gruppe;
    private String rollen;
    private ElternmithilfeCode elternmithilfeCode;
    private Integer kuchenVorstellung;
    private String zusatzattributMaerchen;
    private boolean maerchenFuerSucheBeruecksichtigen;
    private SchuelerCode schuelerCode;

    SchuelerSuchenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    static {
        MITARBEITER_ALLE.setVorname("");
        MITARBEITER_ALLE.setNachname("");
        SCHUELER_CODE_ALLE.setKuerzel("");
        SCHUELER_CODE_ALLE.setBeschreibung("");
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
    public Calendar getStichtag() {
        return stichtagModelAttribute.getValue();
    }

    @Override
    public void setStichtag(String stichtag) throws SvmValidationException {
        stichtagModelAttribute.setNewValue(true, stichtag, isBulkUpdate());
    }

    @Override
    public Semester getSemesterKurs() {
        return semesterKurs;
    }

    @Override
    public void setSemesterKurs(Semester semesterKurs) {
        Semester oldValue = this.semesterKurs;
        this.semesterKurs = semesterKurs;
        firePropertyChange(Field.SEMESTER_KURS, oldValue, this.semesterKurs);
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
    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    @Override
    public void setMitarbeiter(Mitarbeiter mitarbeiter) {
        Mitarbeiter oldValue = this.mitarbeiter;
        this.mitarbeiter = mitarbeiter;
        firePropertyChange(Field.LEHRKRAFT, oldValue, this.mitarbeiter);
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
    public SchuelerCode getSchuelerCode() {
        return schuelerCode;
    }

    @Override
    public void setSchuelerCode(SchuelerCode schuelerCode) {
        SchuelerCode oldValue = this.schuelerCode;
        this.schuelerCode = schuelerCode;
        firePropertyChange(Field.SCHUELER_CODE, oldValue, this.schuelerCode);
    }

    @Override
    public Maerchen getMaerchen() {
        return maerchen;
    }

    @Override
    public void setMaerchen(Maerchen maerchen) {
        Maerchen oldValue = this.maerchen;
        this.maerchen = maerchen;
        firePropertyChange(Field.MAERCHEN, oldValue, this.maerchen);
    }

    @Override
    public Gruppe getGruppe() {
        return gruppe;
    }

    @Override
    public void setGruppe(Gruppe gruppe) {
        Gruppe oldValue = this.gruppe;
        this.gruppe = gruppe;
        firePropertyChange(Field.GRUPPE, oldValue, this.gruppe);
    }

    private final StringModelAttribute rollenModelAttribute = new StringModelAttribute(
            this,
            Field.ROLLEN, 1, 1000,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return rollen;
                }

                @Override
                public void setValue(String value) {
                    rollen = value;
                }
            }
    );

    @Override
    public String getRollen() {
        return rollenModelAttribute.getValue();
    }

    @Override
    public void setRollen(String rollen) throws SvmValidationException {
        rollenModelAttribute.setNewValue(false, rollen, isBulkUpdate());
    }

    @Override
    public ElternmithilfeCode getElternmithilfeCode() {
        return elternmithilfeCode;
    }

    @Override
    public void setElternmithilfeCode(ElternmithilfeCode elternmithilfeCode) {
        ElternmithilfeCode oldValue = this.elternmithilfeCode;
        this.elternmithilfeCode = elternmithilfeCode;
        firePropertyChange(Field.ELTERNMITHILFE_CODE, oldValue, this.elternmithilfeCode);
    }

    private IntegerModelAttribute kuchenVorstellungModelAttribute = new IntegerModelAttribute(
            this,
            Field.KUCHEN_VORSTELLUNG, 1, 9,
            new AttributeAccessor<Integer>() {
                @Override
                public Integer getValue() {
                    return kuchenVorstellung;
                }

                @Override
                public void setValue(Integer value) {
                    kuchenVorstellung = value;
                }
            }
    );

    @Override
    public Integer getKuchenVorstellung() {
        return kuchenVorstellungModelAttribute.getValue();
    }

    @Override
    public void setKuchenVorstellung(String kuchenVorstellung) throws SvmValidationException {
        try {
            if (!isBulkUpdate() && Integer.parseInt(kuchenVorstellung) > maerchen.getAnzahlVorstellungen()) {
                this.kuchenVorstellung = null;
                invalidate();
                throw new SvmValidationException(2032, "Das Märchen hat nur " + maerchen.getAnzahlVorstellungen() + " Vorstellungen", Field.KUCHEN_VORSTELLUNG);
            }
        } catch (NumberFormatException ignore) {
            // wird im nachfolgenden Methoden-Aufruf behandelt
        }
        kuchenVorstellungModelAttribute.setNewValue(false, kuchenVorstellung, isBulkUpdate());
    }

    private final StringModelAttribute zusatzattributMaerchenModelAttribute = new StringModelAttribute(
            this,
            Field.ZUSATZATTRIBUT_MAERCHEN, 1, 30,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return zusatzattributMaerchen;
                }

                @Override
                public void setValue(String value) {
                    zusatzattributMaerchen = value;
                }
            }
    );

    @Override
    public String getZusatzattributMaerchen() {
        return zusatzattributMaerchenModelAttribute.getValue();
    }

    @Override
    public void setZusatzattributMaerchen(String zusatzattributMaerchen) throws SvmValidationException {
        zusatzattributMaerchenModelAttribute.setNewValue(false, zusatzattributMaerchen, isBulkUpdate());
    }

    @Override
    public boolean isMaerchenFuerSucheBeruecksichtigen() {
        return maerchenFuerSucheBeruecksichtigen;
    }

    @Override
    public void setMaerchenFuerSucheBeruecksichtigen(boolean isSelected) {
        boolean oldValue = maerchenFuerSucheBeruecksichtigen;
        maerchenFuerSucheBeruecksichtigen = isSelected;
        firePropertyChange(Field.MAERCHEN_FUER_SUCHE_BERUECKSICHTIGEN, oldValue, maerchenFuerSucheBeruecksichtigen);
    }
   
    @Override
    public boolean searchForSpecificKurs() {
        return (wochentag != null && wochentag != Wochentag.ALLE) && zeitBeginn != null && (mitarbeiter != null && !mitarbeiter.equals(MITARBEITER_ALLE));
    }

    @Override
    public boolean checkIfKurseExist() {
        CommandInvoker commandInvoker = getCommandInvoker();
        Wochentag wochentagFind = (wochentag == Wochentag.ALLE ? null : wochentag);
        Mitarbeiter mitarbeiterFind = (mitarbeiter.equals(MITARBEITER_ALLE) ? null : mitarbeiter);
        FindKurseCommand findKurseCommand = new FindKurseCommand(semesterKurs, wochentagFind, zeitBeginn, mitarbeiterFind);
        commandInvoker.executeCommand(findKurseCommand);
        return findKurseCommand.getResult() == FindKurseCommand.Result.KURSE_GEFUNDEN;
    }

    @Override
    public SchuelerSuchenTableData suchen(SvmModel svmModel) {
        SchuelerSuchenCommand schuelerSuchenCommand = new SchuelerSuchenCommand(this);
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.executeCommand(schuelerSuchenCommand);
        List<Schueler> schuelerList = schuelerSuchenCommand.getSchuelerFound();
        Semester semesterTableData = determineSemesterTableData(svmModel);
        Map<Schueler, List<Kurs>> kurseMapTableData = determineKurseMapTableData(schuelerList, semesterTableData, wochentag, zeitBeginn, mitarbeiter);
        Maerchen maerchenTableData = determineMaerchenTableData(svmModel, semesterTableData);
        Map<Schueler, Maercheneinteilung> maercheneinteilungenMapTableData = determineMaercheneinteilungenMapTableData(schuelerList, maerchenTableData);
        return new SchuelerSuchenTableData(schuelerList, kurseMapTableData, semesterTableData, (wochentag == Wochentag.ALLE ? null : wochentag),
                zeitBeginn, (mitarbeiter == MITARBEITER_ALLE ? null : mitarbeiter), maercheneinteilungenMapTableData, maerchenTableData,
                (gruppe == Gruppe.ALLE ? null : gruppe), (elternmithilfeCode == ELTERNMITHILFE_CODE_ALLE ? null : elternmithilfeCode), maerchenFuerSucheBeruecksichtigen, (rollen != null));
    }

    private Semester determineSemesterTableData(SvmModel svmModel) {
        if (kursFuerSucheBeruecksichtigen) {
            return semesterKurs;
        } else {
            CommandInvoker commandInvoker = getCommandInvoker();
            List<Semester> erfassteSemester = svmModel.getSemestersAll();
            FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(stichtag, erfassteSemester);
            commandInvoker.executeCommand(findSemesterForCalendarCommand);
            Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
            Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
            // Wenn in Ferien zwischen 2 Semestern Folgesemester nehmen
            return  (currentSemester == null ? nextSemester : currentSemester);
        }
    }

    private Map<Schueler, List<Kurs>> determineKurseMapTableData(List<Schueler> schuelerList, Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter) {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindKurseMapSchuelerSemesterCommand findKurseMapSchuelerSemesterCommand = new FindKurseMapSchuelerSemesterCommand(schuelerList, semester, (wochentag == Wochentag.ALLE ? null : wochentag), zeitBeginn, (mitarbeiter == MITARBEITER_ALLE ? null : mitarbeiter));
        commandInvoker.executeCommand(findKurseMapSchuelerSemesterCommand);
        return findKurseMapSchuelerSemesterCommand.getKurseMap();
    }

    private Maerchen determineMaerchenTableData(SvmModel svmModel, Semester semesterTableData) {
        if (semesterTableData == null) {
            return null;
        }
        if (maerchenFuerSucheBeruecksichtigen) {
            return maerchen;
        }
        // Kein Märchen für 2. Semester
        if (semesterTableData.getSemesterbezeichnung() == Semesterbezeichnung.ZWEITES_SEMESTER) {
            return null;
        }
        List<Maerchen> erfassteMaerchen = svmModel.getMaerchensAll();
        for (Maerchen maerchen : erfassteMaerchen) {
            if (maerchen.getSchuljahr().equals(semesterTableData.getSchuljahr())) {
                return maerchen;
            }
        }
        return null;
    }

    private Map<Schueler,Maercheneinteilung> determineMaercheneinteilungenMapTableData(List<Schueler> schuelerList, Maerchen maerchen) {
        FindMaercheneinteilungenMapSchuelerSemesterCommand findMaercheneinteilungenMapSchuelerSemesterCommand = new FindMaercheneinteilungenMapSchuelerSemesterCommand(schuelerList, maerchen);
        findMaercheneinteilungenMapSchuelerSemesterCommand.execute();
        return findMaercheneinteilungenMapSchuelerSemesterCommand.getMaercheneinteilungenMap();
    }

    @Override
    public void invalidateGeburtsdatumSuchperiode() {
        geburtsdatumSuchperiodeBeginnAttribute.initValue("", geburtsdatumSuchperiodeDateFormatString);
    }

    @Override
    public Mitarbeiter[] getSelectableLehrkraefte(SvmModel svmModel) {
        List<Mitarbeiter> lehrkraefteList = svmModel.getAktiveLehrkraefteAll();
        // Lehrkraft alle auch erlaubt
        lehrkraefteList.add(0, MITARBEITER_ALLE);
        return lehrkraefteList.toArray(new Mitarbeiter[lehrkraefteList.size()]);
    }

    @Override
    public SchuelerCode[] getSelectableSchuelerCodes(SvmModel svmModel) {
        List<SchuelerCode> codesList = svmModel.getSelektierbareSchuelerCodesAll();
        // SchuelerCode alle auch erlaubt
        codesList.add(0, SCHUELER_CODE_ALLE);
        return codesList.toArray(new SchuelerCode[codesList.size()]);
    }

    @Override
    public ElternmithilfeCode[] getSelectableElternmithilfeCodes(SvmModel svmModel) {
        List<ElternmithilfeCode> codesList = svmModel.getSelektierbareElternmithilfeCodesAll();
        // ElternmithilfeCode alle auch erlaubt
        if (codesList.isEmpty() || !codesList.get(0).isIdenticalWith(ELTERNMITHILFE_CODE_ALLE)) {
            codesList.add(0, ELTERNMITHILFE_CODE_ALLE);
        }
        return codesList.toArray(new ElternmithilfeCode[codesList.size()]);
    }

    @Override
    public Semester getSemesterInit(SvmModel svmModel) {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmModel.getSemestersAll());
        findSemesterForCalendarCommand.execute();
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        // Innerhalb Semester
        if (currentSemester != null) {
            return currentSemester;
        }
        // Ferien zwischen 2 Semestern
        if (nextSemester != null) {
            return nextSemester;
        }
        // Kein passendes Semester erfasst
        return svmModel.getSemestersAll().get(0);
    }

    @Override
    public Maerchen getMaerchenInit(SvmModel svmModel) {
        DetermineMaerchenInitCommand determineMaerchenInitCommand = new DetermineMaerchenInitCommand(svmModel);
        determineMaerchenInitCommand.execute();
        return determineMaerchenInitCommand.getMaerchenInit();
    }

    @Override
    public boolean isCompleted() {
        return !(maerchenFuerSucheBeruecksichtigen && isSetKuchenVorstellung() && kuchenVorstellung > maerchen.getAnzahlVorstellungen());
    }

    @Override
    public void doValidate() throws SvmValidationException {
        if (maerchenFuerSucheBeruecksichtigen && isSetKuchenVorstellung() && kuchenVorstellung > maerchen.getAnzahlVorstellungen()) {
            throw new SvmValidationException(2032, "Das Märchen hat nur " + maerchen.getAnzahlVorstellungen() + " Vorstellungen", Field.KUCHEN_VORSTELLUNG);
        }
    }

    @Override
    public boolean isAdresseRequired() {
        return false;
    }

    private boolean isSetKuchenVorstellung() {
        return kuchenVorstellung != null;
    }
}
