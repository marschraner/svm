package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.entities.*;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
    private SemesterrechnungenSuchenModel.RechnungsdatumSelected rechnungsdatumSelected;
    private Calendar rechnungsdatum;
    private SemesterrechnungenSuchenModel.RechnungsstatusSelected rechnungsstatus;
    private BigDecimal wochenbetrag;
    private BigDecimal schulgeld;
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
        this.rechnungsdatumSelected = semesterrechnungenSuchenModel.getRechnungsdatumSelected();
        this.rechnungsdatum = semesterrechnungenSuchenModel.getRechnungsdatum();
        this.rechnungsstatus = semesterrechnungenSuchenModel.getRechnungsstatus();
        this.wochenbetrag = semesterrechnungenSuchenModel.getWochenbetrag();
        this.schulgeld = semesterrechnungenSuchenModel.getSchulgeld();
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

        List<Semesterrechnung> resultList1 = typedQuery.getResultList();
        if (schulgeld == null && rechnungsstatus == SemesterrechnungenSuchenModel.RechnungsstatusSelected.ALLE) {
            semesterrechnungenFound = resultList1;
            return;
        }

        // Schulgeld
        List<Semesterrechnung> resultList2;
        if (schulgeld != null) {
            resultList2 = new ArrayList<>();
            for (Semesterrechnung semesterrechnung : resultList1) {
                if (semesterrechnung.getSchulgeld() != null && semesterrechnung.getSchulgeld().compareTo(schulgeld) == 0) {
                    resultList2.add(semesterrechnung);
                }
            }
        } else {
            resultList2 = resultList1;
        }

        // Offene / bezahlte Rechnungen
        List<Semesterrechnung> resultList3 = new ArrayList<>();
        if (rechnungsstatus == SemesterrechnungenSuchenModel.RechnungsstatusSelected.OFFEN) {
            for (Semesterrechnung semesterrechnung : resultList1) {
                if (semesterrechnung.getRestbetrag() != null && semesterrechnung.getRestbetrag().compareTo(BigDecimal.ZERO) > 0) {
                    resultList3.add(semesterrechnung);
                }
            }
        } else if (rechnungsstatus == SemesterrechnungenSuchenModel.RechnungsstatusSelected.BEZAHLT) {
            for (Semesterrechnung semesterrechnung : resultList1) {
                if (semesterrechnung.getRestbetrag() != null && semesterrechnung.getRestbetrag().compareTo(BigDecimal.ZERO) == 0) {
                    resultList3.add(semesterrechnung);
                }
            }
        } else {
            resultList3 = resultList2;
        }
        semesterrechnungenFound = resultList3;
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
        if (rechnungsdatum != null) {
            switch (rechnungsdatumSelected) {
                case AM:
                    selectStatementSb.append(" semre.rechnungsdatum = :rechnungsdatum and");
                    break;
                case VOR:
                    selectStatementSb.append(" semre.rechnungsdatum < :rechnungsdatum and");
                    break;
                case NACH:
                    selectStatementSb.append(" semre.rechnungsdatum > :rechnungsdatum and");
                    break;
            }
        }
        if (wochenbetrag != null) {
            selectStatementSb.append(" semre.wochenbetrag = :wochenbetrag and");
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
        if (selectStatementSb.toString().contains(":rechnungsdatum")) {
            typedQuery.setParameter("rechnungsdatum", rechnungsdatum);
        }
        if (selectStatementSb.toString().contains(":wochenbetrag")) {
            typedQuery.setParameter("wochenbetrag", wochenbetrag);
        }
    }

    public List<Semesterrechnung> getSemesterrechnungenFound() {
        return semesterrechnungenFound;
    }
}
