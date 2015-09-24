package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.MitarbeiterSuchenModel;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class MitarbeiterSuchenCommand extends GenericDaoCommand {

    private static final Logger LOGGER = Logger.getLogger(MitarbeiterSuchenCommand.class);

    // input
    private String nachname;
    private String vorname;
    private MitarbeiterCode mitarbeiterCode;
    private MitarbeiterSuchenModel.LehrkraftJaNeinSelected lehrkraftJaNeinSelected;
    private MitarbeiterSuchenModel.AktivJaNeinSelected aktivJaNeinSelected;
    private StringBuilder selectStatementSb;
    private TypedQuery<Mitarbeiter> typedQuery;

    // output
    private List<Mitarbeiter> mitarbeitersFound;

    public MitarbeiterSuchenCommand(MitarbeiterSuchenModel mitarbeiterSuchenModel) {
        this.nachname = mitarbeiterSuchenModel.getNachname();
        this.vorname = mitarbeiterSuchenModel.getVorname();
        this.mitarbeiterCode = mitarbeiterSuchenModel.getMitarbeiterCode();
        this.lehrkraftJaNeinSelected = mitarbeiterSuchenModel.getLehrkraftJaNeinSelected();
        this.aktivJaNeinSelected = mitarbeiterSuchenModel.getAktivJaNeinSelected();
    }

    @Override
    public void execute() {
        
        selectStatementSb = new StringBuilder("select distinct m from Mitarbeiter m");

        // Inner-Joins erzeugen
        createJoinMitarbeiterCodes();

        // Selection-Statements
        selectStatementSb.append(" where");
        createWhereSelections();

        // Letztes " and" löschen
        if (selectStatementSb.substring(selectStatementSb.length() - 4).equals(" and")) {
            selectStatementSb.setLength(selectStatementSb.length() - 4);
        }

        // "where" löschen, falls dieses am Schluss steht
        if (selectStatementSb.substring(selectStatementSb.length() - 5).equals("where")) {
            selectStatementSb.setLength(selectStatementSb.length() - 5);
        }

        // Sortierung
        selectStatementSb.append(" order by m.nachname, m.vorname, m.geburtsdatum");

        LOGGER.trace("JPQL Select-Statement: " + selectStatementSb.toString());

        typedQuery = entityManager.createQuery(selectStatementSb.toString(), Mitarbeiter.class);

        // Suchparameter setzen
        setSelectionParameters();

        mitarbeitersFound = typedQuery.getResultList();
    }

    private void createJoinMitarbeiterCodes() {
        if (mitarbeiterCode != null) {
            selectStatementSb.append(" join m.mitarbeiterCodes c");
        }
    }

    private void createWhereSelections() {
        if (checkNotEmpty(nachname)) {
            selectStatementSb.append(" lower(m.nachname) like :nachname and");
        }
        if (checkNotEmpty(vorname)) {
            selectStatementSb.append(" lower(m.vorname) like :vorname and");
        }
        if (mitarbeiterCode != null) {
                selectStatementSb.append(" c.codeId = :codeId and");
        }
        switch (lehrkraftJaNeinSelected) {
            case JA:
                selectStatementSb.append(" m.lehrkraft = 1 and");
                break;
            case NEIN:
                selectStatementSb.append(" m.lehrkraft = 0 and");
                break;
            case ALLE:
                break;
        }
        switch (aktivJaNeinSelected) {
            case JA:
                selectStatementSb.append(" m.aktiv = 1 and");
                break;
            case NEIN:
                selectStatementSb.append(" m.aktiv = 0 and");
                break;
            case ALLE:
                break;
        }
    }

    private void setSelectionParameters() {
        if (selectStatementSb.toString().contains(":vorname")) {
            typedQuery.setParameter("vorname", vorname.toLowerCase() + "%");
        }
        if (selectStatementSb.toString().contains(":nachname")) {
            typedQuery.setParameter("nachname", nachname.toLowerCase() + "%");
        }
        if (selectStatementSb.toString().contains(":codeId")) {
            typedQuery.setParameter("codeId", mitarbeiterCode.getCodeId());
        }
    }

    public List<Mitarbeiter> getMitarbeitersFound() {
        return mitarbeitersFound;
    }

}
