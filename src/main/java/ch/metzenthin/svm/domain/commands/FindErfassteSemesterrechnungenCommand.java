package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class FindErfassteSemesterrechnungenCommand extends GenericDaoCommand {

    private static final Logger LOGGER = Logger.getLogger(FindErfassteSemesterrechnungenCommand.class);

    // input
    private Semester semester;
    private String nachname;
    private String vorname;
    private SemesterrechnungenSuchenModel.RolleSelected rolle;
    private SemesterrechnungCode semesterrechnungCode;
    private Stipendium stipendium;
    private Boolean gratiskinder;
    private SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumVorrechnungSelected praezisierungRechnungsdatumVorrechnungSelected;
    private Calendar rechnungsdatumVorrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungErmaessigungVorrechnungSelected praezisierungErmaessigungVorrechnungSelected;
    private BigDecimal ermaessigungVorrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungZuschlagVorrechnungSelected praezisierungZuschlagVorrechnungSelected;
    private BigDecimal zuschlagVorrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungWochenbetragVorrechnungSelected praezisierungWochenbetragVorrechnungSelected;
    private BigDecimal wochenbetragVorrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungSchulgeldVorrechnungSelected praezisierungSchulgeldVorrechnungSelected;
    private BigDecimal schulgeldVorrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungRechnungsdatumNachrechnungSelected praezisierungRechnungsdatumNachrechnungSelected;
    private SemesterrechnungenSuchenModel.VollstaendigkeitVorrechnungSelected vollstaendigkeitVorrechnungSelected;
    private Calendar rechnungsdatumNachrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungErmaessigungNachrechnungSelected praezisierungErmaessigungNachrechnungSelected;
    private BigDecimal ermaessigungNachrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungZuschlagNachrechnungSelected praezisierungZuschlagNachrechnungSelected;
    private BigDecimal zuschlagNachrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungAnzahlWochenNachrechnungSelected praezisierungAnzahlWochenNachrechnungSelected;
    private Integer anzahlWochenNachrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungWochenbetragNachrechnungSelected praezisierungWochenbetragNachrechnungSelected;
    private BigDecimal wochenbetragNachrechnung;
    private SemesterrechnungenSuchenModel.PraezisierungSchulgeldNachrechnungSelected praezisierungSchulgeldNachrechnungSelected;
    private BigDecimal schulgeldNachrechnung;
    private SemesterrechnungenSuchenModel.VollstaendigkeitNachrechnungSelected vollstaendigkeitNachrechnungSelected;
    private SemesterrechnungenSuchenModel.PraezisierungDifferenzSchulgeldSelected praezisierungDifferenzSchulgeldSelected;
    private BigDecimal differenzSchulgeld;
    private SemesterrechnungenSuchenModel.PraezisierungRestbetragSelected praezisierungRestbetragSelected;
    private BigDecimal restbetrag;
    private StringBuilder selectStatementSb;
    TypedQuery<Semesterrechnung> typedQuery;

    // output
    private List<Semesterrechnung> semesterrechnungenFound = new ArrayList<>();

    public FindErfassteSemesterrechnungenCommand(SemesterrechnungenSuchenModel semesterrechnungenSuchenModel) {
        this.semester = semesterrechnungenSuchenModel.getSemester();
        this.nachname = semesterrechnungenSuchenModel.getNachname();
        this.vorname = semesterrechnungenSuchenModel.getVorname();
        this.rolle = semesterrechnungenSuchenModel.getRolle();
        this.semesterrechnungCode = semesterrechnungenSuchenModel.getSemesterrechnungCode();
        this.stipendium = semesterrechnungenSuchenModel.getStipendium();
        this.gratiskinder = semesterrechnungenSuchenModel.isGratiskinder();
        this.praezisierungRechnungsdatumVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungRechnungsdatumVorrechnungSelected();
        this.rechnungsdatumVorrechnung = semesterrechnungenSuchenModel.getRechnungsdatumVorrechnung();
        this.praezisierungErmaessigungVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungErmaessigungVorrechnungSelected();
        this.ermaessigungVorrechnung = semesterrechnungenSuchenModel.getErmaessigungVorrechnung();
        this.praezisierungZuschlagVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungZuschlagVorrechnungSelected();
        this.zuschlagVorrechnung = semesterrechnungenSuchenModel.getZuschlagVorrechnung();
        this.praezisierungWochenbetragVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungWochenbetragVorrechnungSelected();
        this.wochenbetragVorrechnung = semesterrechnungenSuchenModel.getWochenbetragVorrechnung();
        this.praezisierungSchulgeldVorrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungSchulgeldVorrechnungSelected();
        this.schulgeldVorrechnung = semesterrechnungenSuchenModel.getSchulgeldVorrechnung();
        this.vollstaendigkeitVorrechnungSelected = semesterrechnungenSuchenModel.getVollstaendigkeitVorrechnungSelected();
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
        this.praezisierungSchulgeldNachrechnungSelected = semesterrechnungenSuchenModel.getPraezisierungSchulgeldNachrechnungSelected();
        this.schulgeldNachrechnung = semesterrechnungenSuchenModel.getSchulgeldNachrechnung();
        this.vollstaendigkeitNachrechnungSelected = semesterrechnungenSuchenModel.getVollstaendigkeitNachrechnungSelected();
        this.praezisierungDifferenzSchulgeldSelected = semesterrechnungenSuchenModel.getPraezisierungDifferenzSchulgeldSelected();
        this.differenzSchulgeld = semesterrechnungenSuchenModel.getDifferenzSchulgeld();
        this.praezisierungRestbetragSelected = semesterrechnungenSuchenModel.getPraezisierungRestbetragSelected();
        this.restbetrag = semesterrechnungenSuchenModel.getRestbetrag();
    }

    @Override
    public void execute() {

        selectStatementSb = new StringBuilder("select distinct semre from Semesterrechnung semre");

        // Inner-Joins erzeugen
        createJoinSchueler();

        // Selection-Statements
        selectStatementSb.append(" where semre.semester.semesterId = :semesterId and");
        createWhereSelections();

        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }

        LOGGER.trace("JPQL Select-Statement: " + selectStatementSb.toString());

        typedQuery = entityManager.createQuery(selectStatementSb.toString(), Semesterrechnung.class);

        // Suchparameter setzen
        setSelectionParameters();

        semesterrechnungenFound = typedQuery.getResultList();

        // Filter für restliche Suchparameter
        filterSemesterrechnungenFound();
    }

    private void filterSemesterrechnungenFound() {

        if (schulgeldVorrechnung != null) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (praezisierungSchulgeldVorrechnungSelected) {
                case GLEICH:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getSchulgeldVorrechnung() == null || semesterrechnungIt.getSchulgeldVorrechnung().compareTo(schulgeldVorrechnung) != 0) {
                            it.remove();
                        }
                    }
                    break;
                case KLEINER:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getSchulgeldVorrechnung() == null || semesterrechnungIt.getSchulgeldVorrechnung().compareTo(schulgeldVorrechnung) != -1) {
                            it.remove();
                        }
                    }
                    break;
                case GROESSER:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getSchulgeldVorrechnung() == null || semesterrechnungIt.getSchulgeldVorrechnung().compareTo(schulgeldVorrechnung) != 1) {
                            it.remove();
                        }
                    }
                    break;
            }
        }
        if (vollstaendigkeitVorrechnungSelected != SemesterrechnungenSuchenModel.VollstaendigkeitVorrechnungSelected.ALLE) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (vollstaendigkeitVorrechnungSelected) {
                case VOLLSTAENDIG:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (!semesterrechnungIt.isVollstaendigVorrechnung()) {
                            it.remove();
                        }
                    }
                    break;
                case UNVOLLSTAENDIG:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.isVollstaendigVorrechnung()) {
                            it.remove();
                        }
                    }
                    break;
            }
        }
        if (schulgeldNachrechnung != null) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (praezisierungSchulgeldNachrechnungSelected) {
                case GLEICH:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getSchulgeldNachrechnung() == null || semesterrechnungIt.getSchulgeldNachrechnung().compareTo(schulgeldNachrechnung) != 0) {
                            it.remove();
                        }
                    }
                    break;
                case KLEINER:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getSchulgeldNachrechnung() == null || semesterrechnungIt.getSchulgeldNachrechnung().compareTo(schulgeldNachrechnung) != -1) {
                            it.remove();
                        }
                    }
                    break;
                case GROESSER:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getSchulgeldNachrechnung() == null || semesterrechnungIt.getSchulgeldNachrechnung().compareTo(schulgeldNachrechnung) != 1) {
                            it.remove();
                        }
                    }
                    break;
            }
        }
        if (vollstaendigkeitNachrechnungSelected != SemesterrechnungenSuchenModel.VollstaendigkeitNachrechnungSelected.ALLE) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (vollstaendigkeitNachrechnungSelected) {
                case VOLLSTAENDIG:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (!semesterrechnungIt.isVollstaendigNachrechnung()) {
                            it.remove();
                        }
                    }
                    break;
                case UNVOLLSTAENDIG:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.isVollstaendigNachrechnung()) {
                            it.remove();
                        }
                    }
                    break;
            }
        }
        if (differenzSchulgeld != null) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (praezisierungDifferenzSchulgeldSelected) {
                case GLEICH:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getDifferenzSchulgeld() == null || semesterrechnungIt.getDifferenzSchulgeld().compareTo(differenzSchulgeld) != 0) {
                            it.remove();
                        }
                    }
                    break;
                case KLEINER:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getDifferenzSchulgeld() == null || semesterrechnungIt.getDifferenzSchulgeld().compareTo(differenzSchulgeld) != -1) {
                            it.remove();
                        }
                    }
                    break;
                case GROESSER:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getDifferenzSchulgeld() == null || semesterrechnungIt.getDifferenzSchulgeld().compareTo(differenzSchulgeld) != 1) {
                            it.remove();
                        }
                    }
                    break;
            }
        }
        if (restbetrag != null) {
            Iterator<Semesterrechnung> it = semesterrechnungenFound.iterator();
            switch (praezisierungRestbetragSelected) {
                case GLEICH:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRestbetrag() == null || semesterrechnungIt.getRestbetrag().compareTo(restbetrag) != 0) {
                            it.remove();
                        }
                    }
                    break;
                case KLEINER:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRestbetrag() == null || semesterrechnungIt.getRestbetrag().compareTo(restbetrag) != -1) {
                            it.remove();
                        }
                    }
                    break;
                case GROESSER:
                    while (it.hasNext()) {
                        Semesterrechnung semesterrechnungIt = it.next();
                        if (semesterrechnungIt.getRestbetrag() == null || semesterrechnungIt.getRestbetrag().compareTo(restbetrag) != 1) {
                            it.remove();
                        }
                    }
                    break;
            }
        }
    }

    private void createJoinSchueler() {
        if (rolle != SemesterrechnungenSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER) {
            selectStatementSb.append(" join semre.rechnungsempfaenger.schuelerRechnungsempfaenger sch");
        }
    }

    private void createWhereSelections() {

        if (checkNotEmpty(vorname)) {
            String selectRechnungsempfaenger = " lower(semre.rechnungsempfaenger.vorname) = :vorname";
            String selectSchueler = " lower(sch.vorname) = :vorname";
            String selectEltern = "(exists (select sch1 from Schueler sch1 where lower(sch1.mutter.vorname) = :vorname and sch1.personId = sch.personId) or exists (select sch2 from Schueler sch2 where lower(sch2.vater.vorname) = :vorname and sch2.personId = sch.personId))";
            switch (rolle) {
                case SCHUELER:
                    selectStatementSb.append(selectSchueler).append(" and");
                    break;
                case ELTERN:
                    selectStatementSb.append(selectEltern).append(" and");
                    break;
                case RECHNUNGSEMPFAENGER:
                    selectStatementSb.append(selectRechnungsempfaenger).append(" and");
                    break;
                case ALLE:
                    selectStatementSb.append(selectRechnungsempfaenger).append(" or ").append(selectSchueler).append(" or ").append(selectEltern).append(" and");
                    break;
            }
        }
        if (checkNotEmpty(nachname)) {
            String selectRechnungsempfaenger = " lower(semre.rechnungsempfaenger.nachname) = :nachname";
            String selectSchueler = " lower(sch.nachname) = :nachname";
            String selectEltern = "(exists (select sch1 from Schueler sch1 where lower(sch1.mutter.nachname) = :nachname and sch1.personId = sch.personId) or exists (select sch2 from Schueler sch2 where lower(sch2.vater.nachname) = :nachname and sch2.personId = sch.personId))";
            switch (rolle) {
                case SCHUELER:
                    selectStatementSb.append(selectSchueler).append(" and");
                    break;
                case ELTERN:
                    selectStatementSb.append(selectEltern).append(" and");
                    break;
                case RECHNUNGSEMPFAENGER:
                    selectStatementSb.append(selectRechnungsempfaenger).append(" and");
                    break;
                case ALLE:
                    selectStatementSb.append(selectRechnungsempfaenger).append(" or ").append(selectSchueler).append(" or ").append(selectEltern).append(" and");
                    break;
            }
        }
        if (semesterrechnungCode != null && semesterrechnungCode != SemesterrechnungenSuchenModel.SEMESTERRECHNUNG_CODE_ALLE) {
            selectStatementSb.append(" semre.semesterrechnungCode.codeId = :semesterrechnungCodeId and");
        }
        if (stipendium != null && stipendium != Stipendium.KEINES) {
            selectStatementSb.append(" semre.stipendium = :stipendium and");
        }
        if (gratiskinder == null || !gratiskinder) {
            selectStatementSb.append(" semre.gratiskinder = 0 and");
        } else {
            selectStatementSb.append(" semre.gratiskinder = 1 and");
        }
        if (rechnungsdatumVorrechnung != null) {
            switch (praezisierungRechnungsdatumVorrechnungSelected) {
                case AM:
                    selectStatementSb.append(" semre.rechnungsdatumVorrechnung = :rechnungsdatumVorrechnung and");
                    break;
                case VOR:
                    selectStatementSb.append(" semre.rechnungsdatumVorrechnung < :rechnungsdatumVorrechnung and");
                    break;
                case NACH:
                    selectStatementSb.append(" semre.rechnungsdatumVorrechnung > :rechnungsdatumVorrechnung and");
                    break;
            }
        }
        if (ermaessigungVorrechnung != null) {
            switch (praezisierungErmaessigungVorrechnungSelected) {
                case GLEICH:
                    selectStatementSb.append(" semre.ermaessigungVorrechnung = :ermaessigungVorrechnung and");
                    break;
                case KLEINER:
                    selectStatementSb.append(" semre.ermaessigungVorrechnung < :ermaessigungVorrechnung and");
                    break;
                case GROESSER:
                    selectStatementSb.append(" semre.ermaessigungVorrechnung > :ermaessigungVorrechnung and");
                    break;
            }
        }
        if (zuschlagVorrechnung != null) {
            switch (praezisierungZuschlagVorrechnungSelected) {
                case GLEICH:
                    selectStatementSb.append(" semre.zuschlagVorrechnung = :zuschlagVorrechnung and");
                    break;
                case KLEINER:
                    selectStatementSb.append(" semre.zuschlagVorrechnung < :zuschlagVorrechnung and");
                    break;
                case GROESSER:
                    selectStatementSb.append(" semre.zuschlagVorrechnung > :zuschlagVorrechnung and");
                    break;
            }
        }
        if (wochenbetragVorrechnung != null) {
            switch (praezisierungWochenbetragVorrechnungSelected) {
                case GLEICH:
                    selectStatementSb.append(" semre.wochenbetragVorrechnung = :wochenbetragVorrechnung and");
                    break;
                case KLEINER:
                    selectStatementSb.append(" semre.wochenbetragVorrechnung < :wochenbetragVorrechnung and");
                    break;
                case GROESSER:
                    selectStatementSb.append(" semre.wochenbetragVorrechnung > :wochenbetragVorrechnung and");
                    break;
            }
        }
        if (rechnungsdatumNachrechnung != null) {
            switch (praezisierungRechnungsdatumNachrechnungSelected) {
                case AM:
                    selectStatementSb.append(" semre.rechnungsdatumNachrechnung = :rechnungsdatumNachrechnung and");
                    break;
                case VOR:
                    selectStatementSb.append(" semre.rechnungsdatumNachrechnung < :rechnungsdatumNachrechnung and");
                    break;
                case NACH:
                    selectStatementSb.append(" semre.rechnungsdatumNachrechnung > :rechnungsdatumNachrechnung and");
                    break;
            }
        }
        if (ermaessigungNachrechnung != null) {
            switch (praezisierungErmaessigungNachrechnungSelected) {
                case GLEICH:
                    selectStatementSb.append(" semre.ermaessigungNachrechnung = :ermaessigungNachrechnung and");
                    break;
                case KLEINER:
                    selectStatementSb.append(" semre.ermaessigungNachrechnung < :ermaessigungNachrechnung and");
                    break;
                case GROESSER:
                    selectStatementSb.append(" semre.ermaessigungNachrechnung > :ermaessigungNachrechnung and");
                    break;
            }
        }
        if (zuschlagNachrechnung != null) {
            switch (praezisierungZuschlagNachrechnungSelected) {
                case GLEICH:
                    selectStatementSb.append(" semre.zuschlagNachrechnung = :zuschlagNachrechnung and");
                    break;
                case KLEINER:
                    selectStatementSb.append(" semre.zuschlagNachrechnung < :zuschlagNachrechnung and");
                    break;
                case GROESSER:
                    selectStatementSb.append(" semre.zuschlagNachrechnung > :zuschlagNachrechnung and");
                    break;
            }
        }
        if (anzahlWochenNachrechnung != null) {
            switch (praezisierungAnzahlWochenNachrechnungSelected) {
                case GLEICH:
                    selectStatementSb.append(" semre.anzahlWochenNachrechnung = :anzahlWochenNachrechnung and");
                    break;
                case KLEINER:
                    selectStatementSb.append(" semre.anzahlWochenNachrechnung < :anzahlWochenNachrechnung and");
                    break;
                case GROESSER:
                    selectStatementSb.append(" semre.anzahlWochenNachrechnung > :anzahlWochenNachrechnung and");
                    break;
            }
        }
        if (wochenbetragNachrechnung != null) {
            switch (praezisierungWochenbetragNachrechnungSelected) {
                case GLEICH:
                    selectStatementSb.append(" semre.wochenbetragNachrechnung = :wochenbetragNachrechnung and");
                    break;
                case KLEINER:
                    selectStatementSb.append(" semre.wochenbetragNachrechnung < :wochenbetragNachrechnung and");
                    break;
                case GROESSER:
                    selectStatementSb.append(" semre.wochenbetragNachrechnung > :wochenbetragNachrechnung and");
                    break;
            }
        }

    }
    
    private void setSelectionParameters() {
        typedQuery.setParameter("semesterId", semester.getSemesterId());
        if (selectStatementSb.toString().contains(":vorname")) {
            typedQuery.setParameter("vorname", vorname.toLowerCase());
        }
        if (selectStatementSb.toString().contains(":nachname")) {
            typedQuery.setParameter("nachname", nachname.toLowerCase());
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

    public List<Semesterrechnung> getSemesterrechnungenFound() {
        return semesterrechnungenFound;
    }
}
