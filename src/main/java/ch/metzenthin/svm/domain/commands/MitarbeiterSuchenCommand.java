package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import ch.metzenthin.svm.domain.model.MitarbeiterSuchenModel;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class MitarbeiterSuchenCommand implements Command {

  private static final Logger LOGGER = LogManager.getLogger(MitarbeiterSuchenCommand.class);

  private final DB db = DBFactory.getInstance();

  // input
  private final String nachname;
  private final String vorname;
  private final MitarbeiterCode mitarbeiterCode;
  private final MitarbeiterSuchenModel.LehrkraftJaNeinSelected lehrkraftJaNeinSelected;
  private final MitarbeiterSuchenModel.StatusSelected statusSelected;
  private StringBuilder selectStatementSb;
  private TypedQuery<Mitarbeiter> typedQuery;

  // output
  private List<Mitarbeiter> mitarbeitersFound;

  public MitarbeiterSuchenCommand(MitarbeiterSuchenModel mitarbeiterSuchenModel) {
    this.nachname = mitarbeiterSuchenModel.getNachname();
    this.vorname = mitarbeiterSuchenModel.getVorname();
    this.mitarbeiterCode = mitarbeiterSuchenModel.getMitarbeiterCode();
    this.lehrkraftJaNeinSelected = mitarbeiterSuchenModel.getLehrkraftJaNeinSelected();
    this.statusSelected = mitarbeiterSuchenModel.getStatusSelected();
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

    LOGGER.trace("JPQL Select-Statement: {}", selectStatementSb);

    typedQuery =
        db.getCurrentEntityManager().createQuery(selectStatementSb.toString(), Mitarbeiter.class);

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
        selectStatementSb.append(" m.lehrkraft = true and");
        break;
      case NEIN:
        selectStatementSb.append(" m.lehrkraft = false and");
        break;
      case ALLE:
        break;
    }
    switch (statusSelected) {
      case AKTIV:
        selectStatementSb.append(" m.aktiv = true and");
        break;
      case NICHT_AKTIV:
        selectStatementSb.append(" m.aktiv = false and");
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
