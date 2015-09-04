package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class FindRechnungsempfaengerOhneSemesterrechnungenCommand extends GenericDaoCommand {

    private static final Logger LOGGER = Logger.getLogger(FindRechnungsempfaengerOhneSemesterrechnungenCommand.class);

    // input
    private Semester semester;
    private String nachname;
    private String vorname;
    private SemesterrechnungenSuchenModel.RolleSelected rolle;
    private StringBuilder selectStatementSb;
    TypedQuery<Angehoeriger> typedQuery;

    // output
    private List<Angehoeriger> rechnungsempfaengersFound = new ArrayList<>();

    public FindRechnungsempfaengerOhneSemesterrechnungenCommand(SemesterrechnungenSuchenModel semesterrechnungenSuchenModel) {
        this.semester = semesterrechnungenSuchenModel.getSemester();
        this.nachname = semesterrechnungenSuchenModel.getNachname();
        this.vorname = semesterrechnungenSuchenModel.getVorname();
        this.rolle = semesterrechnungenSuchenModel.getRolle();
    }

    @Override
    public void execute() {

        selectStatementSb = new StringBuilder("select distinct rech from Angehoeriger rech" +
                " join rech.schuelerRechnungsempfaenger sch" +
                " join sch.kursanmeldungen kursanm");

        // Selection-Statements
        selectStatementSb.append(" where kursanm.kurs.semester.semesterId = :semesterId and" +
                " not exists (select semre.rechnungsempfaenger from Semesterrechnung semre where semre.rechnungsempfaenger.personId = rech.personId and semre.semester.semesterId = :semesterId) and");
        createWhereSelections();

        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }

        LOGGER.trace("JPQL Select-Statement: " + selectStatementSb.toString());

        typedQuery = entityManager.createQuery(selectStatementSb.toString(), Angehoeriger.class);

        // Suchparameter setzen
        setSelectionParameters();

        rechnungsempfaengersFound = typedQuery.getResultList();
    }

    private void createWhereSelections() {
        if (checkNotEmpty(vorname)) {
            String selectRechnungsempfaenger = " lower(rech.vorname) = :vorname";
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
            String selectRechnungsempfaenger = " lower(rech.nachname) = :nachname";
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
    }
    
    private void setSelectionParameters() {
        typedQuery.setParameter("semesterId", semester.getSemesterId());
        if (selectStatementSb.toString().contains(":vorname")) {
            typedQuery.setParameter("vorname", vorname.toLowerCase());
        }
        if (selectStatementSb.toString().contains(":nachname")) {
            typedQuery.setParameter("nachname", nachname.toLowerCase());
        }
    }

    public List<Angehoeriger> getRechnungsempfaengersFound() {
        return rechnungsempfaengersFound;
    }
}
