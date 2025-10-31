package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * @author Martin Schraner
 */
public class CheckIfAngehoerigerVerwaistAndDeleteCommand implements Command {

  private final DB db = DBFactory.getInstance();
  private final AngehoerigerDao angehoerigerDao = new AngehoerigerDao();

  // input
  private Angehoeriger angehoeriger;

  // output
  private boolean deleted;

  public CheckIfAngehoerigerVerwaistAndDeleteCommand(Angehoeriger angehoeriger) {
    this.angehoeriger = angehoeriger;
  }

  @Override
  public void execute() {

    deleted = false;

    if (angehoeriger == null) {
      return;
    }

    // Kommt Angehoeriger als Mutter, Vater oder Rechnungsempfänger vor?
    EntityManager entityManager = db.getCurrentEntityManager();
    TypedQuery<Long> typedQuery =
        entityManager.createQuery(
            "select count(s) from Schueler s where s.mutter.personId = :angehoerigerPersonId or s.vater.personId = :angehoerigerPersonId or s.rechnungsempfaenger.personId = :angehoerigerPersonId",
            Long.class);
    typedQuery.setParameter("angehoerigerPersonId", angehoeriger.getPersonId());
    if (typedQuery.getSingleResult() > 0) {
      return;
    }

    // Kommt Angehoeriger als Rechnungsempfänger einer Semesterrechnung vor?
    typedQuery =
        entityManager.createQuery(
            "select count(s) from Semesterrechnung s where s.rechnungsempfaenger.personId = :angehoerigerPersonId",
            Long.class);
    typedQuery.setParameter("angehoerigerPersonId", angehoeriger.getPersonId());
    if (typedQuery.getSingleResult() > 0) {
      return;
    }

    // Angehöriger in diesem Fall verwaist -> löschen
    angehoerigerDao.remove(angehoeriger);
    entityManager.flush();
    angehoeriger = null;
    deleted = true;
  }

  public boolean isDeleted() {
    return deleted;
  }
}
