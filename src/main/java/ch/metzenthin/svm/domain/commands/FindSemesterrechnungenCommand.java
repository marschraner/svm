package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.common.datatypes.Stipendium;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.model.SemesterrechnungModel;
import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class FindSemesterrechnungenCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(FindSemesterrechnungenCommand.class);

    private final DB db = DBFactory.getInstance();

    // input
    private final Semester semester;
    private final String nachname;
    private final String vorname;
    private final SemesterrechnungenSuchenModel.RolleSelected rolle;
    private final Wochentag wochentag;
    private final Time zeitBeginn;
    private final Mitarbeiter mitarbeiter;
    private final SemesterrechnungenSuchenModel.SemesterrechnungCodeJaNeinSelected semesterrechnungCodeJaNeinSelected;
    private final SemesterrechnungCode semesterrechnungCode;
    private final SemesterrechnungenSuchenModel.StipendiumJaNeinSelected stipendiumJaNeinSelected;
    private final Stipendium stipendium;
    private final Boolean gratiskinder;
    private final SemesterrechnungenSuchenModel.RechnungsdatumGesetztVorrechnungSelected rechnungsdatumGesetztVorrechnungSelected;
    private final SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected praezisierungRechnungsdatumVorrechnungSelected;
    private final Calendar rechnungsdatumVorrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected praezisierungErmaessigungVorrechnungSelected;
    private final BigDecimal ermaessigungVorrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected praezisierungZuschlagVorrechnungSelected;
    private final BigDecimal zuschlagVorrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected praezisierungWochenbetragVorrechnungSelected;
    private final SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenVorrechnungSelected praezisierungAnzahlWochenVorrechnungSelected;
    private final Integer anzahlWochenVorrechnung;
    private final BigDecimal wochenbetragVorrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragVorrechnungSelected praezisierungRechnungsbetragVorrechnungSelected;
    private final BigDecimal rechnungsbetragVorrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungRestbetragVorrechnungSelected praezisierungRestbetragVorrechnungSelected;
    private final BigDecimal restbetragVorrechnung;
    private final SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected sechsJahresRabattJaNeinVorrechnungSelected;
    private final SemesterrechnungenSuchenModel.RechnungsdatumGesetztNachrechnungSelected rechnungsdatumGesetztNachrechnungSelected;
    private final SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected praezisierungRechnungsdatumNachrechnungSelected;
    private final Calendar rechnungsdatumNachrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected praezisierungErmaessigungNachrechnungSelected;
    private final BigDecimal ermaessigungNachrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected praezisierungZuschlagNachrechnungSelected;
    private final BigDecimal zuschlagNachrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected praezisierungAnzahlWochenNachrechnungSelected;
    private final Integer anzahlWochenNachrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected praezisierungWochenbetragNachrechnungSelected;
    private final BigDecimal wochenbetragNachrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungRechnungsbetragNachrechnungSelected praezisierungRechnungsbetragNachrechnungSelected;
    private final BigDecimal rechnungsbetragNachrechnung;
    private final SemesterrechnungenSuchenModel.PraezisierungRestbetragNachrechnungSelected praezisierungRestbetragNachrechnungSelected;
    private final BigDecimal restbetragNachrechnung;
    private final SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected sechsJahresRabattJaNeinNachrechnungSelected;
    private final SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected praezisierungDifferenzSchulgeldSelected;
    private final BigDecimal differenzSchulgeld;
    private final Boolean geloescht;
    private StringBuilder selectStatementSb;
    private TypedQuery<Semesterrechnung> typedQuery;

    // output
    private List<Semesterrechnung> semesterrechnungenFound = new ArrayList<>();

    public FindSemesterrechnungenCommand(SemesterrechnungenSuchenModel semesterrechnungenSuchenModel) {
        this.semester = semesterrechnungenSuchenModel.getSemester();
        this.nachname = semesterrechnungenSuchenModel.getNachname();
        this.vorname = semesterrechnungenSuchenModel.getVorname();
        this.rolle = semesterrechnungenSuchenModel.getRolle();
        this.wochentag = semesterrechnungenSuchenModel.getWochentag();
        this.zeitBeginn = semesterrechnungenSuchenModel.getZeitBeginn();
        this.mitarbeiter = semesterrechnungenSuchenModel.getMitarbeiter();
        this.semesterrechnungCodeJaNeinSelected = semesterrechnungenSuchenModel.getSemesterrechnungCodeJaNeinSelected();
        this.semesterrechnungCode = semesterrechnungenSuchenModel.getSemesterrechnungCode();
        this.stipendiumJaNeinSelected = semesterrechnungenSuchenModel.getStipendiumJaNeinSelected();
        this.stipendium = semesterrechnungenSuchenModel.getStipendium();
        this.gratiskinder = semesterrechnungenSuchenModel.isGratiskinder();
        this.rechnungsdatumGesetztVorrechnungSelected = semesterrechnungenSuchenModel.getRechnungsdatumGesetztVorrechnungSelected();
        this.praezisierungRechnungsdatumVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungRechnungsdatumVorrechnungSelected();
        this.rechnungsdatumVorrechnung = semesterrechnungenSuchenModel.getRechnungsdatumVorrechnung();
        this.praezisierungErmaessigungVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungErmaessigungVorrechnungSelected();
        this.ermaessigungVorrechnung = semesterrechnungenSuchenModel.getErmaessigungVorrechnung();
        this.praezisierungZuschlagVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungZuschlagVorrechnungSelected();
        this.zuschlagVorrechnung = semesterrechnungenSuchenModel.getZuschlagVorrechnung();
        this.praezisierungAnzahlWochenVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungAnzahlWochenVorrechnungSelected();
        this.anzahlWochenVorrechnung = semesterrechnungenSuchenModel.getAnzahlWochenVorrechnung();
        this.praezisierungWochenbetragVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungWochenbetragVorrechnungSelected();
        this.wochenbetragVorrechnung = semesterrechnungenSuchenModel.getWochenbetragVorrechnung();
        this.praezisierungRechnungsbetragVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungRechnungsbetragVorrechnungSelected();
        this.rechnungsbetragVorrechnung = semesterrechnungenSuchenModel.getRechnungsbetragVorrechnung();
        this.praezisierungRestbetragVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungRestbetragVorrechnungSelected();
        this.restbetragVorrechnung = semesterrechnungenSuchenModel.getRestbetragVorrechnung();
        this.sechsJahresRabattJaNeinVorrechnungSelected = semesterrechnungenSuchenModel.getSechsJahresRabattJaNeinVorrechnungSelected();
        this.rechnungsdatumGesetztNachrechnungSelected = semesterrechnungenSuchenModel.getRechnungsdatumGesetztNachrechnungSelected();
        this.praezisierungRechnungsdatumNachrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungRechnungsdatumNachrechnungSelected();
        this.rechnungsdatumNachrechnung = semesterrechnungenSuchenModel.getRechnungsdatumNachrechnung();
        this.praezisierungErmaessigungNachrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungErmaessigungNachrechnungSelected();
        this.ermaessigungNachrechnung = semesterrechnungenSuchenModel.getErmaessigungNachrechnung();
        this.praezisierungZuschlagNachrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungZuschlagNachrechnungSelected();
        this.zuschlagNachrechnung = semesterrechnungenSuchenModel.getZuschlagNachrechnung();
        this.praezisierungAnzahlWochenNachrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungAnzahlWochenNachrechnungSelected();
        this.anzahlWochenNachrechnung = semesterrechnungenSuchenModel.getAnzahlWochenNachrechnung();
        this.praezisierungWochenbetragNachrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungWochenbetragNachrechnungSelected();
        this.wochenbetragNachrechnung = semesterrechnungenSuchenModel.getWochenbetragNachrechnung();
        this.praezisierungRechnungsbetragNachrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungRechnungsbetragNachrechnungSelected();
        this.rechnungsbetragNachrechnung = semesterrechnungenSuchenModel.getRechnungsbetragNachrechnung();
        this.praezisierungRestbetragNachrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungRestbetragNachrechnungSelected();
        this.restbetragNachrechnung = semesterrechnungenSuchenModel.getRestbetragNachrechnung();
        this.sechsJahresRabattJaNeinNachrechnungSelected = semesterrechnungenSuchenModel.getSechsJahresRabattJaNeinNachrechnungSelected();
        this.praezisierungDifferenzSchulgeldSelected = semesterrechnungenSuchenModel.getPraezisierungDifferenzSchulgeldSelected();
        this.differenzSchulgeld = semesterrechnungenSuchenModel.getDifferenzSchulgeld();
        this.geloescht = semesterrechnungenSuchenModel.isGeloescht();
    }

    @Override
    public void execute() {

        if (semester == null) {
            return;
        }

        selectStatementSb = new StringBuilder("select distinct semre from Semesterrechnung semre");

        // Inner-Joins erzeugen
        createJoinSchueler();
        createJoinKurs();

        // Selection-Statements
        selectStatementSb.append(" where semre.semester.semesterId = :semesterId and semre.deleted = :deleted and");
        createWhereSelections();

        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }

        LOGGER.trace("JPQL Select-Statement: {}", selectStatementSb);

        typedQuery = db.getCurrentEntityManager().createQuery(
                selectStatementSb.toString(), Semesterrechnung.class);

        // Suchparameter setzen
        setSelectionParameters();

        semesterrechnungenFound = typedQuery.getResultList();

        // Filter für restliche Suchparameter
        filterSemesterrechnungenFound();
    }

    private void createJoinSchueler() {
        if (rolle != SemesterrechnungenSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER || wochentag != Wochentag.ALLE || zeitBeginn != null || mitarbeiter != SemesterrechnungenSuchenModel.MITARBEITER_ALLE) {
            selectStatementSb.append(" join semre.rechnungsempfaenger.schuelerRechnungsempfaenger sch");
        }
    }

    private void createJoinKurs() {
        if (wochentag != Wochentag.ALLE || zeitBeginn != null || mitarbeiter != SemesterrechnungenSuchenModel.MITARBEITER_ALLE) {
            selectStatementSb.append(" join sch.kursanmeldungen kursanm");
            if (mitarbeiter != SemesterrechnungenSuchenModel.MITARBEITER_ALLE) {
                selectStatementSb.append(" join kursanm.kurs.lehrkraefte lkr");
            }
        }
    }

    @SuppressWarnings({"DuplicatedCode", "java:S3776", "java:S6541"})
    private void createWhereSelections() {

        if (checkNotEmpty(vorname)) {
            String selectRechnungsempfaenger = " lower(semre.rechnungsempfaenger.vorname) like :vorname";
            String selectSchueler = " lower(sch.vorname) like :vorname";
            String selectEltern = "(exists (select sch1 from Schueler sch1 where lower(sch1.mutter.vorname) like :vorname and sch1.personId = sch.personId) or exists (select sch2 from Schueler sch2 where lower(sch2.vater.vorname) like :vorname and sch2.personId = sch.personId))";
            switch (rolle) {
                case SCHUELER -> selectStatementSb.append(selectSchueler).append(" and");
                case ELTERN -> selectStatementSb.append(selectEltern).append(" and");
                case RECHNUNGSEMPFAENGER -> selectStatementSb.append(selectRechnungsempfaenger).append(" and");
                case ALLE ->
                        selectStatementSb.append("(").append(selectRechnungsempfaenger).append(" or ").append(selectSchueler).append(" or ").append(selectEltern).append(")").append(" and");
            }
        }
        if (checkNotEmpty(nachname)) {
            String selectRechnungsempfaenger = " lower(semre.rechnungsempfaenger.nachname) like :nachname";
            String selectSchueler = " lower(sch.nachname) like :nachname";
            String selectEltern = "(exists (select sch1 from Schueler sch1 where lower(sch1.mutter.nachname) like :nachname and sch1.personId = sch.personId) or exists (select sch2 from Schueler sch2 where lower(sch2.vater.nachname) like :nachname and sch2.personId = sch.personId))";
            switch (rolle) {
                case SCHUELER -> selectStatementSb.append(selectSchueler).append(" and");
                case ELTERN -> selectStatementSb.append(selectEltern).append(" and");
                case RECHNUNGSEMPFAENGER -> selectStatementSb.append(selectRechnungsempfaenger).append(" and");
                case ALLE ->
                        selectStatementSb.append("(").append(selectRechnungsempfaenger).append(" or ").append(selectSchueler).append(" or ").append(selectEltern).append(")").append(" and");
            }
        }
        if (wochentag != Wochentag.ALLE || zeitBeginn != null || mitarbeiter != SemesterrechnungenSuchenModel.MITARBEITER_ALLE) {
            selectStatementSb.append(" kursanm.kurs.semester.semesterId = :semesterId and");
        }
        if (wochentag != Wochentag.ALLE) {
            selectStatementSb.append(" kursanm.kurs.wochentag = :wochentag and");
        }
        if (zeitBeginn != null) {
            selectStatementSb.append(" kursanm.kurs.zeitBeginn = :zeitBeginn and");
        }
        if (mitarbeiter != SemesterrechnungenSuchenModel.MITARBEITER_ALLE) {
            selectStatementSb.append(" lkr.personId = :lehrkraftPersonId and");
        }
        switch (semesterrechnungCodeJaNeinSelected) {
            case JA -> {
                if (semesterrechnungCode != null && semesterrechnungCode != SemesterrechnungModel.SEMESTERRECHNUNG_CODE_ALLE) {
                    selectStatementSb.append(" semre.semesterrechnungCode.codeId = :semesterrechnungCodeId and");
                } else {
                    selectStatementSb.append(" semre.semesterrechnungCode is not null and");
                }
            }
            case NEIN -> selectStatementSb.append(" semre.semesterrechnungCode is null and");
            case ALLE -> {
                // Nothing to do
            }
        }
        switch (stipendiumJaNeinSelected) {
            case JA -> {
                if (stipendium != null && stipendium != Stipendium.ALLE) {
                    selectStatementSb.append(" semre.stipendium = :stipendium and");
                } else {
                    selectStatementSb.append(" semre.stipendium is not null and");
                }
            }
            case NEIN -> selectStatementSb.append(" semre.stipendium is null and");
            case ALLE -> {
                // Nothing to do
            }
        }
        if (gratiskinder == null || !gratiskinder) {
            selectStatementSb.append(" semre.gratiskinder = false and");
        } else {
            selectStatementSb.append(" semre.gratiskinder = true and");
        }
        switch (rechnungsdatumGesetztVorrechnungSelected) {
            case GESETZT -> {
                if (rechnungsdatumVorrechnung != null) {
                    switch (praezisierungRechnungsdatumVorrechnungSelected) {
                        case AM ->
                                selectStatementSb.append(" semre.rechnungsdatumVorrechnung = :rechnungsdatumVorrechnung and");
                        case VOR ->
                                selectStatementSb.append(" semre.rechnungsdatumVorrechnung < :rechnungsdatumVorrechnung and");
                        case NACH ->
                                selectStatementSb.append(" semre.rechnungsdatumVorrechnung > :rechnungsdatumVorrechnung and");
                    }
                } else {
                    selectStatementSb.append(" semre.rechnungsdatumVorrechnung is not null and");
                }
            }
            case NICHT_GESETZT -> selectStatementSb.append(" semre.rechnungsdatumVorrechnung is null and");
            case ALLE -> {
                // Nothing to do
            }
        }
        if (ermaessigungVorrechnung != null) {
            switch (praezisierungErmaessigungVorrechnungSelected) {
                case GLEICH ->
                        selectStatementSb.append(" semre.ermaessigungVorrechnung = :ermaessigungVorrechnung and");
                case KLEINER ->
                        selectStatementSb.append(" semre.ermaessigungVorrechnung < :ermaessigungVorrechnung and");
                case GROESSER ->
                        selectStatementSb.append(" semre.ermaessigungVorrechnung > :ermaessigungVorrechnung and");
            }
        }
        if (zuschlagVorrechnung != null) {
            switch (praezisierungZuschlagVorrechnungSelected) {
                case GLEICH -> selectStatementSb.append(" semre.zuschlagVorrechnung = :zuschlagVorrechnung and");
                case KLEINER -> selectStatementSb.append(" semre.zuschlagVorrechnung < :zuschlagVorrechnung and");
                case GROESSER -> selectStatementSb.append(" semre.zuschlagVorrechnung > :zuschlagVorrechnung and");
            }
        }
        if (anzahlWochenVorrechnung != null) {
            switch (praezisierungAnzahlWochenVorrechnungSelected) {
                case GLEICH ->
                        selectStatementSb.append(" semre.anzahlWochenVorrechnung = :anzahlWochenVorrechnung and");
                case KLEINER ->
                        selectStatementSb.append(" semre.anzahlWochenVorrechnung < :anzahlWochenVorrechnung and");
                case GROESSER ->
                        selectStatementSb.append(" semre.anzahlWochenVorrechnung > :anzahlWochenVorrechnung and");
            }
        }
        if (wochenbetragVorrechnung != null) {
            switch (praezisierungWochenbetragVorrechnungSelected) {
                case GLEICH ->
                        selectStatementSb.append(" semre.wochenbetragVorrechnung = :wochenbetragVorrechnung and");
                case KLEINER ->
                        selectStatementSb.append(" semre.wochenbetragVorrechnung < :wochenbetragVorrechnung and");
                case GROESSER ->
                        selectStatementSb.append(" semre.wochenbetragVorrechnung > :wochenbetragVorrechnung and");
            }
        }
        switch (rechnungsdatumGesetztNachrechnungSelected) {
            case GESETZT -> {
                if (rechnungsdatumNachrechnung != null) {
                    switch (praezisierungRechnungsdatumNachrechnungSelected) {
                        case AM ->
                                selectStatementSb.append(" semre.rechnungsdatumNachrechnung = :rechnungsdatumNachrechnung and");
                        case VOR ->
                                selectStatementSb.append(" semre.rechnungsdatumNachrechnung < :rechnungsdatumNachrechnung and");
                        case NACH ->
                                selectStatementSb.append(" semre.rechnungsdatumNachrechnung > :rechnungsdatumNachrechnung and");
                    }
                } else {
                    selectStatementSb.append(" semre.rechnungsdatumNachrechnung is not null and");
                }
            }
            case NICHT_GESETZT -> selectStatementSb.append(" semre.rechnungsdatumNachrechnung is null and");
            case ALLE -> {
                // Nothing to do
            }
        }
        if (ermaessigungNachrechnung != null) {
            switch (praezisierungErmaessigungNachrechnungSelected) {
                case GLEICH ->
                        selectStatementSb.append(" semre.ermaessigungNachrechnung = :ermaessigungNachrechnung and");
                case KLEINER ->
                        selectStatementSb.append(" semre.ermaessigungNachrechnung < :ermaessigungNachrechnung and");
                case GROESSER ->
                        selectStatementSb.append(" semre.ermaessigungNachrechnung > :ermaessigungNachrechnung and");
            }
        }
        if (zuschlagNachrechnung != null) {
            switch (praezisierungZuschlagNachrechnungSelected) {
                case GLEICH -> selectStatementSb.append(" semre.zuschlagNachrechnung = :zuschlagNachrechnung and");
                case KLEINER -> selectStatementSb.append(" semre.zuschlagNachrechnung < :zuschlagNachrechnung and");
                case GROESSER -> selectStatementSb.append(" semre.zuschlagNachrechnung > :zuschlagNachrechnung and");
            }
        }
        if (anzahlWochenNachrechnung != null) {
            switch (praezisierungAnzahlWochenNachrechnungSelected) {
                case GLEICH ->
                        selectStatementSb.append(" semre.anzahlWochenNachrechnung = :anzahlWochenNachrechnung and");
                case KLEINER ->
                        selectStatementSb.append(" semre.anzahlWochenNachrechnung < :anzahlWochenNachrechnung and");
                case GROESSER ->
                        selectStatementSb.append(" semre.anzahlWochenNachrechnung > :anzahlWochenNachrechnung and");
            }
        }
        if (wochenbetragNachrechnung != null) {
            switch (praezisierungWochenbetragNachrechnungSelected) {
                case GLEICH ->
                        selectStatementSb.append(" semre.wochenbetragNachrechnung = :wochenbetragNachrechnung and");
                case KLEINER ->
                        selectStatementSb.append(" semre.wochenbetragNachrechnung < :wochenbetragNachrechnung and");
                case GROESSER ->
                        selectStatementSb.append(" semre.wochenbetragNachrechnung > :wochenbetragNachrechnung and");
            }
        }

    }

    @SuppressWarnings({"DuplicatedCode", "java:S3776"})
    private void setSelectionParameters() {
        typedQuery.setParameter("semesterId", semester.getSemesterId());
        typedQuery.setParameter("deleted", geloescht);
        if (selectStatementSb.toString().contains(":vorname")) {
            typedQuery.setParameter("vorname", vorname.toLowerCase() + "%");
        }
        if (selectStatementSb.toString().contains(":nachname")) {
            typedQuery.setParameter("nachname", nachname.toLowerCase() + "%");
        }
        if (selectStatementSb.toString().contains(":wochentag")) {
            typedQuery.setParameter("wochentag", wochentag);
        }
        if (selectStatementSb.toString().contains(":zeitBeginn")) {
            typedQuery.setParameter("zeitBeginn", zeitBeginn);
        }
        if (selectStatementSb.toString().contains(":lehrkraftPersonId")) {
            typedQuery.setParameter("lehrkraftPersonId", mitarbeiter.getPersonId());
        }
        if (selectStatementSb.toString().contains(":semesterrechnungCodeId")) {
            typedQuery.setParameter("semesterrechnungCodeId", semesterrechnungCode.getCodeId());
        }
        if (selectStatementSb.toString().contains(":stipendium")) {
            typedQuery.setParameter("stipendium", stipendium);
        }
        if (selectStatementSb.toString().contains(":rechnungsdatumVorrechnung")) {
            typedQuery.setParameter("rechnungsdatumVorrechnung", rechnungsdatumVorrechnung);
        }
        if (selectStatementSb.toString().contains(":ermaessigungVorrechnung")) {
            typedQuery.setParameter("ermaessigungVorrechnung", ermaessigungVorrechnung);
        }
        if (selectStatementSb.toString().contains(":zuschlagVorrechnung")) {
            typedQuery.setParameter("zuschlagVorrechnung", zuschlagVorrechnung);
        }
        if (selectStatementSb.toString().contains(":anzahlWochenVorrechnung")) {
            typedQuery.setParameter("anzahlWochenVorrechnung", anzahlWochenVorrechnung);
        }
        if (selectStatementSb.toString().contains(":wochenbetragVorrechnung")) {
            typedQuery.setParameter("wochenbetragVorrechnung", wochenbetragVorrechnung);
        }
        if (selectStatementSb.toString().contains(":rechnungsdatumNachrechnung")) {
            typedQuery.setParameter("rechnungsdatumNachrechnung", rechnungsdatumNachrechnung);
        }
        if (selectStatementSb.toString().contains(":ermaessigungNachrechnung")) {
            typedQuery.setParameter("ermaessigungNachrechnung", ermaessigungNachrechnung);
        }
        if (selectStatementSb.toString().contains(":zuschlagNachrechnung")) {
            typedQuery.setParameter("zuschlagNachrechnung", zuschlagNachrechnung);
        }
        if (selectStatementSb.toString().contains(":anzahlWochenNachrechnung")) {
            typedQuery.setParameter("anzahlWochenNachrechnung", anzahlWochenNachrechnung);
        }
        if (selectStatementSb.toString().contains(":wochenbetragNachrechnung")) {
            typedQuery.setParameter("wochenbetragNachrechnung", wochenbetragNachrechnung);
        }
    }

    private void filterSemesterrechnungenFound() {
        filterRechnungsbetrag();
        filterRestbetrag();
        filterSechsJahresRabatt();
        filterDifferenzSchulgeld();
    }

    @SuppressWarnings({"java:S6541", "java:S3776"})
    private void filterRechnungsbetrag() {
        if (rechnungsbetragVorrechnung != null) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (praezisierungRechnungsbetragVorrechnungSelected) {
                case GLEICH -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRechnungsbetragVorrechnung() == null
                                || semesterrechnungIt.getRechnungsbetragVorrechnung().compareTo(rechnungsbetragVorrechnung) != 0) {
                            it.remove();
                        }
                    }
                }
                case KLEINER -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRechnungsbetragVorrechnung() == null
                                || semesterrechnungIt.getRechnungsbetragVorrechnung().compareTo(rechnungsbetragVorrechnung) >= 0) {
                            it.remove();
                        }
                    }
                }
                case GROESSER -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRechnungsbetragVorrechnung() == null
                                || semesterrechnungIt.getRechnungsbetragVorrechnung().compareTo(rechnungsbetragVorrechnung) <= 0) {
                            it.remove();
                        }
                    }
                }
            }
        }
        if (rechnungsbetragNachrechnung != null) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (praezisierungRechnungsbetragNachrechnungSelected) {
                case GLEICH -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRechnungsbetragNachrechnung() == null
                                || semesterrechnungIt.getRechnungsbetragNachrechnung().compareTo(rechnungsbetragNachrechnung) != 0) {
                            it.remove();
                        }
                    }
                }
                case KLEINER -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRechnungsbetragNachrechnung() == null
                                || semesterrechnungIt.getRechnungsbetragNachrechnung().compareTo(rechnungsbetragNachrechnung) >= 0) {
                            it.remove();
                        }
                    }
                }
                case GROESSER -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRechnungsbetragNachrechnung() == null
                                || semesterrechnungIt.getRechnungsbetragNachrechnung().compareTo(rechnungsbetragNachrechnung) <= 0) {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings({"java:S3776", "java:S6541"})
    private void filterRestbetrag() {
        if (restbetragVorrechnung != null) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (praezisierungRestbetragVorrechnungSelected) {
                case GLEICH -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRestbetragVorrechnung() == null
                                || semesterrechnungIt.getRestbetragVorrechnung().compareTo(restbetragVorrechnung) != 0) {
                            it.remove();
                        }
                    }
                }
                case KLEINER -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRestbetragVorrechnung() == null
                                || semesterrechnungIt.getRestbetragVorrechnung().compareTo(restbetragVorrechnung) >= 0) {
                            it.remove();
                        }
                    }
                }
                case GROESSER -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRestbetragVorrechnung() == null
                                || semesterrechnungIt.getRestbetragVorrechnung().compareTo(restbetragVorrechnung) <= 0) {
                            it.remove();
                        }
                    }
                }
            }
        }
        if (restbetragNachrechnung != null) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (praezisierungRestbetragNachrechnungSelected) {
                case GLEICH -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRestbetragNachrechnung() == null
                                || semesterrechnungIt.getRestbetragNachrechnung().compareTo(restbetragNachrechnung) != 0) {
                            it.remove();
                        }
                    }
                }
                case KLEINER -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRestbetragNachrechnung() == null
                                || semesterrechnungIt.getRestbetragNachrechnung().compareTo(restbetragNachrechnung) >= 0) {
                            it.remove();
                        }
                    }
                }
                case GROESSER -> {
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRestbetragNachrechnung() == null
                                || semesterrechnungIt.getRestbetragNachrechnung().compareTo(restbetragNachrechnung) <= 0) {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("java:S3776")
    private void filterSechsJahresRabatt() {
        if (sechsJahresRabattJaNeinVorrechnungSelected != SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.ALLE) {
            FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(semester);
            findPreviousSemesterCommand.execute();
            Semester previousSemester = findPreviousSemesterCommand.getPreviousSemester();
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            if (sechsJahresRabattJaNeinVorrechnungSelected
                    == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.JA) {
                while (it.hasNext()) {
                    Semesterrechnung semesterrechnungIt = it.next();
                    CheckIfSemesterrechnungContainsSechsJahresRabattCommand checkIfSemesterrechnungContainsSechsJahresRabattCommand = new CheckIfSemesterrechnungContainsSechsJahresRabattCommand(semesterrechnungIt, previousSemester, Rechnungstyp.VORRECHNUNG);
                    checkIfSemesterrechnungContainsSechsJahresRabattCommand.execute();
                    if (!checkIfSemesterrechnungContainsSechsJahresRabattCommand.isSemesterrechnungContainsSechsJahresRabatt()) {
                        it.remove();
                    }
                }
            } else if (sechsJahresRabattJaNeinVorrechnungSelected
                    == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinVorrechnungSelected.NEIN) {
                while (it.hasNext()) {
                    Semesterrechnung semesterrechnungIt = it.next();
                    CheckIfSemesterrechnungContainsSechsJahresRabattCommand checkIfSemesterrechnungContainsSechsJahresRabattCommand = new CheckIfSemesterrechnungContainsSechsJahresRabattCommand(semesterrechnungIt, previousSemester, Rechnungstyp.VORRECHNUNG);
                    checkIfSemesterrechnungContainsSechsJahresRabattCommand.execute();
                    if (checkIfSemesterrechnungContainsSechsJahresRabattCommand.isSemesterrechnungContainsSechsJahresRabatt()) {
                        it.remove();
                    }
                }
            }
        }
        if (sechsJahresRabattJaNeinNachrechnungSelected != SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.ALLE) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            if (sechsJahresRabattJaNeinNachrechnungSelected == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.JA) {
                while (it.hasNext()) {
                    Semesterrechnung semesterrechnungIt = it.next();
                    CheckIfSemesterrechnungContainsSechsJahresRabattCommand checkIfSemesterrechnungContainsSechsJahresRabattCommand = new CheckIfSemesterrechnungContainsSechsJahresRabattCommand(semesterrechnungIt, semester, Rechnungstyp.NACHRECHNUNG);
                    checkIfSemesterrechnungContainsSechsJahresRabattCommand.execute();
                    if (!checkIfSemesterrechnungContainsSechsJahresRabattCommand.isSemesterrechnungContainsSechsJahresRabatt()) {
                        it.remove();
                    }
                }
            } else if (sechsJahresRabattJaNeinNachrechnungSelected == SemesterrechnungenSuchenModel.SechsJahresRabattJaNeinNachrechnungSelected.NEIN) {
                while (it.hasNext()) {
                    Semesterrechnung semesterrechnungIt = it.next();
                    CheckIfSemesterrechnungContainsSechsJahresRabattCommand checkIfSemesterrechnungContainsSechsJahresRabattCommand = new CheckIfSemesterrechnungContainsSechsJahresRabattCommand(semesterrechnungIt, semester, Rechnungstyp.NACHRECHNUNG);
                    checkIfSemesterrechnungContainsSechsJahresRabattCommand.execute();
                    if (checkIfSemesterrechnungContainsSechsJahresRabattCommand.isSemesterrechnungContainsSechsJahresRabatt()) {
                        it.remove();
                    }
                }
            }
        }

    }

    @SuppressWarnings("java:S3776")
    private void filterDifferenzSchulgeld() {
        if (differenzSchulgeld == null) {
            return;
        }
        Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
        switch (praezisierungDifferenzSchulgeldSelected) {
            case GLEICH -> {
                while (it.hasNext()) {
                    Semesterrechnung semesterrechnungIt = it.next();
                    if (semesterrechnungIt.getDifferenzSchulgeld() == null
                            || semesterrechnungIt.getDifferenzSchulgeld().compareTo(differenzSchulgeld) != 0) {
                        it.remove();
                    }
                }
            }
            case KLEINER -> {
                while (it.hasNext()) {
                    Semesterrechnung semesterrechnungIt = it.next();
                    if (semesterrechnungIt.getDifferenzSchulgeld() == null
                            || semesterrechnungIt.getDifferenzSchulgeld().compareTo(differenzSchulgeld) >= 0) {
                        it.remove();
                    }
                }
            }
            case GROESSER -> {
                while (it.hasNext()) {
                    Semesterrechnung semesterrechnungIt = it.next();
                    if (semesterrechnungIt.getDifferenzSchulgeld() == null
                            || semesterrechnungIt.getDifferenzSchulgeld().compareTo(differenzSchulgeld) <= 0) {
                        it.remove();
                    }
                }
            }
        }
    }


    List<Semesterrechnung> getSemesterrechnungenFound() {
        return semesterrechnungenFound;
    }
}
