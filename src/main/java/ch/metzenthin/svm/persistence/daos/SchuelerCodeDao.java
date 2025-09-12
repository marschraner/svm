package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SchuelerCodeDao extends GenericDao<SchuelerCode, Integer> {

  public Schueler addToSchuelerAndSave(SchuelerCode schuelerCode, Schueler schueler) {
    schueler.addCode(schuelerCode);
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.persist(schueler);
    entityManager.flush();
    entityManager.refresh(schuelerCode);
    entityManager.refresh(schueler);
    return schueler;
  }

  public Schueler removeFromSchuelerAndUpdate(SchuelerCode schuelerCode, Schueler schueler) {
    schueler.deleteCode(schuelerCode);
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.persist(schueler);
    entityManager.flush();
    entityManager.refresh(schuelerCode);
    entityManager.refresh(schueler);
    return schueler;
  }

  public List<SchuelerCode> findAll() {
    TypedQuery<SchuelerCode> typedQuery =
        db.getCurrentEntityManager()
            .createQuery("select c from SchuelerCode c order by c.kuerzel", SchuelerCode.class);
    return typedQuery.getResultList();
  }
}
