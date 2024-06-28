package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class KursanmeldungErfassenModelImpl extends AbstractModel implements KursanmeldungErfassenModel {

    private static final Logger LOGGER = LogManager.getLogger(KursanmeldungErfassenModelImpl.class);

    private Semester semester;
    private Wochentag wochentag;
    private Time zeitBeginn;
    private Mitarbeiter mitarbeiter;
    private Kurs kurs;
    private final Kursanmeldung kursanmeldung = new Kursanmeldung();
    private Kursanmeldung kursanmeldungOrigin;

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
            new AttributeAccessor<>() {
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
    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    @Override
    public void setMitarbeiter(Mitarbeiter mitarbeiter) throws SvmRequiredException {
        Mitarbeiter oldValue = this.mitarbeiter;
        this.mitarbeiter = mitarbeiter;
        firePropertyChange(Field.LEHRKRAFT, oldValue, this.mitarbeiter);
        if (mitarbeiter == null) {
            invalidate();
            throw new SvmRequiredException(Field.LEHRKRAFT);
        }
    }

    private final CalendarModelAttribute anmeldedatumModelAttribute = new CalendarModelAttribute(
            this,
            Field.ANMELDEDATUM, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return kursanmeldung.getAnmeldedatum();
                }

                @Override
                public void setValue(Calendar value) {
                    kursanmeldung.setAnmeldedatum(value);
                }
            }
    );

    @Override
    public Calendar getAnmeldedatum() {
        return anmeldedatumModelAttribute.getValue();
    }

    @Override
    public void setAnmeldedatum(String anmeldedatum) throws SvmValidationException {
        anmeldedatumModelAttribute.setNewValue(true, anmeldedatum, isBulkUpdate());
        if (!isBulkUpdate() && kursanmeldung.getAnmeldedatum() != null && kursanmeldung.getAbmeldedatum() != null && kursanmeldung.getAnmeldedatum().after(kursanmeldung.getAbmeldedatum())) {
            kursanmeldung.setAnmeldedatum(null);
            invalidate();
            throw new SvmValidationException(2022, "Keine gültige Periode", Field.ANMELDEDATUM);
        }
        if (!isBulkUpdate() && kursanmeldung.getAnmeldedatum() != null && kursanmeldung.getAnmeldedatum().before(semester.getSemesterbeginn())) {
            kursanmeldung.setAnmeldedatum(null);
            invalidate();
            throw new SvmValidationException(2026, "Datum darf nicht vor " + asString(semester.getSemesterbeginn()) + " liegen", Field.ANMELDEDATUM);
        }
        if (!isBulkUpdate() && kursanmeldung.getAnmeldedatum() != null && kursanmeldung.getAnmeldedatum().after(semester.getSemesterende())) {
            kursanmeldung.setAnmeldedatum(null);
            invalidate();
            throw new SvmValidationException(2027, "Datum darf nicht nach " + asString(semester.getSemesterende()) + " liegen", Field.ANMELDEDATUM);
        }
    }

    private final CalendarModelAttribute abmeldedatumModelAttribute = new CalendarModelAttribute(
            this,
            Field.ABMELDEDATUM, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return kursanmeldung.getAbmeldedatum();
                }

                @Override
                public void setValue(Calendar value) {
                    kursanmeldung.setAbmeldedatum(value);
                }
            }
    );

    @Override
    public Calendar getAbmeldedatum() {
        return abmeldedatumModelAttribute.getValue();
    }

    @Override
    public void setAbmeldedatum(String abmeldedatum) throws SvmValidationException {
        abmeldedatumModelAttribute.setNewValue(false, abmeldedatum, isBulkUpdate());
        if (!isBulkUpdate() && kursanmeldung.getAnmeldedatum() != null && kursanmeldung.getAbmeldedatum() != null && kursanmeldung.getAnmeldedatum().after(kursanmeldung.getAbmeldedatum())) {
            kursanmeldung.setAbmeldedatum(null);
            invalidate();
            throw new SvmValidationException(2024, "Keine gültige Periode", Field.ABMELDEDATUM);
        }
        // Spätestes gültiges Abmeldedatum ist ein Tag vor Beginn des neuen Semesters
        Calendar spaetestesGueltigesAbmeldedatum = (Calendar) getSemesterbeginnNaechstesSemester().clone();
        spaetestesGueltigesAbmeldedatum.add(Calendar.DAY_OF_YEAR, -1);
        if (!isBulkUpdate() && kursanmeldung.getAbmeldedatum() != null && kursanmeldung.getAbmeldedatum().after(spaetestesGueltigesAbmeldedatum)) {
            kursanmeldung.setAnmeldedatum(null);
            invalidate();
            throw new SvmValidationException(2028, "Datum darf nicht nach " + asString(spaetestesGueltigesAbmeldedatum) + " liegen", Field.ABMELDEDATUM);
        }
    }

    private final StringModelAttribute bemerkungenModelAttribute = new StringModelAttribute(
            this,
            Field.BEMERKUNGEN, 2, 100,
            new AttributeAccessor<>() {
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

    private Calendar getSemesterbeginnNaechstesSemester() {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindNextSemesterCommand findNextSemesterCommand = new FindNextSemesterCommand(semester);
        commandInvoker.executeCommand(findNextSemesterCommand);
        Semester nextSemester = findNextSemesterCommand.getNextSemester();

        // Nächstes Semester bereits erfasst
        Calendar semesterbeginnNaechstesSemester;
        if (nextSemester != null) {
            semesterbeginnNaechstesSemester = findNextSemesterCommand.getNextSemester().getSemesterbeginn();
        }

        // nächstes Semester noch nicht erfasst
        else {
            semesterbeginnNaechstesSemester = (Calendar) semester.getSemesterende().clone();
            if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
                semesterbeginnNaechstesSemester.add(Calendar.DAY_OF_YEAR, 16);   // 2 Wochen Sportferien
            } else {
                semesterbeginnNaechstesSemester.add(Calendar.DAY_OF_YEAR, 37);
            }
        }
        return semesterbeginnNaechstesSemester;

    }

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
    public Mitarbeiter[] getSelectableLehrkraftKursanmeldungOrigin() {
        return new Mitarbeiter[]{kursanmeldungOrigin.getKurs().getLehrkraefte().get(0)};
    }

    @Override
    public Semester getInitSemester(List<Semester> semesterList) {
        DetermineSemesterInitCommand determineSemesterInitCommand = new DetermineSemesterInitCommand(semesterList, 40);
        determineSemesterInitCommand.execute();
        return determineSemesterInitCommand.getSemesterInit();
    }

    @Override
    public boolean checkIfSemesterIsInPast() {
        Calendar today = new GregorianCalendar();
        return semester.getSemesterende().before(today);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public Calendar getInitAnmeldedatum(KursanmeldungenTableModel kursanmeldungenTableModel) {
        if (semester == null) {
            return null;
        }

        // Semesterbeginn als Initialisierungswert
        Calendar kursanmeldungInit = (Calendar) semester.getSemesterbeginn().clone();

        // Falls Anmeldedatum bereits gesetzt wurde, diese bei Semesterwechsel in jedem Fall aktualisieren
        if (kursanmeldung.getAnmeldedatum() != null) {
            return kursanmeldungInit;
        }

        // Prüfen, ob Schüler Kursanmeldungen im vorhergehenden Semester ohne Abmeldung hat. Wenn ja Semesterbeginn zurückgeben, sonst null.
        String schuljahrPreviousSemester;
        Semesterbezeichnung semesterbezeichnungPreviousSemester;
        if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
            schuljahrPreviousSemester = Schuljahre.getPreviousSchuljahr(semester.getSchuljahr());
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ZWEITES_SEMESTER;
        } else {
            schuljahrPreviousSemester = semester.getSchuljahr();
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ERSTES_SEMESTER;
        }
        List<Kursanmeldung> kursanmeldungen = kursanmeldungenTableModel.getKursanmeldungenTableData().getKursanmeldungen();
        for (Kursanmeldung kursanmeldung : kursanmeldungen) {
            if (kursanmeldung.getKurs().getSemester().getSchuljahr().equals(schuljahrPreviousSemester)
                    && kursanmeldung.getKurs().getSemester().getSemesterbezeichnung() == semesterbezeichnungPreviousSemester
                    && kursanmeldung.getAbmeldedatum() == null) {
                return kursanmeldungInit;
            }
        }

        return null;
    }

    @Override
    public FindKursCommand.Result findKurs() {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindKursCommand findKursCommand = new FindKursCommand(semester, wochentag, zeitBeginn, mitarbeiter);
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
    public boolean checkIfSchuelerIsAngemeldet(SchuelerDatenblattModel schuelerDatenblattModel) {
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        Anmeldung anmeldung = schueler.getAnmeldungen().get(0);
        return !(anmeldung.getAbmeldedatum() != null && !anmeldung.getAbmeldedatum().after(kursanmeldung.getAnmeldedatum()));
    }

    @Override
    public CalculateAnzWochenCommand.Result speichern(KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        kursanmeldung.setKurs(kurs);
        kursanmeldung.setSchueler(schuelerDatenblattModel.getSchueler());
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateKursanmeldungCommand saveOrUpdateKursanmeldungCommand = new SaveOrUpdateKursanmeldungCommand(kursanmeldung, kursanmeldungOrigin, schuelerDatenblattModel.getSchueler().getKursanmeldungenAsList());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateKursanmeldungCommand);
        // TableData mit von der Datenbank upgedateter Kursanmeldung updaten
        kursanmeldungenTableModel.getKursanmeldungenTableData().setKursanmeldungen(schuelerDatenblattModel.getSchueler().getKursanmeldungenAsList());

        return saveOrUpdateKursanmeldungCommand.getResult();
    }

    @Override
    public void initializeCompleted() {
        if (kursanmeldungOrigin != null) {
            setBulkUpdate(true);
            try {
                setSemester(kursanmeldungOrigin.getKurs().getSemester());
                setWochentag(kursanmeldungOrigin.getKurs().getWochentag());
                setZeitBeginn(asString(kursanmeldungOrigin.getKurs().getZeitBeginn()));
                setMitarbeiter(kursanmeldungOrigin.getKurs().getLehrkraefte().get(0));
                kurs = kursanmeldungOrigin.getKurs();
                setAnmeldedatum(asString(kursanmeldungOrigin.getAnmeldedatum()));
                setAbmeldedatum(asString(kursanmeldungOrigin.getAbmeldedatum()));
                setBemerkungen(kursanmeldungOrigin.getBemerkungen());
            } catch (SvmValidationException e) {
                LOGGER.error(e.getMessage());
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
    }
}
