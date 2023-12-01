package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class MitarbeiterCodeDao extends GenericDao<MitarbeiterCode, Integer> {

    public Mitarbeiter addToMitarbeiterAndSave(MitarbeiterCode mitarbeiterCode, Mitarbeiter mitarbeiter) {
        mitarbeiter.addCode(mitarbeiterCode);
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.persist(mitarbeiter);
        entityManager.flush();
        entityManager.refresh(mitarbeiterCode);
        entityManager.refresh(mitarbeiter);
        return mitarbeiter;
    }

    public Mitarbeiter removeFromMitarbeiterAndUpdate(MitarbeiterCode mitarbeiterCode, Mitarbeiter mitarbeiter) {
        mitarbeiter.deleteCode(mitarbeiterCode);
        EntityManager entityManager = db.getCurrentEntityManager();
        entityManager.persist(mitarbeiter);
        entityManager.flush();
        entityManager.refresh(mitarbeiterCode);
        entityManager.refresh(mitarbeiter);
        return mitarbeiter;
    }

    public List<MitarbeiterCode> findAll() {
        TypedQuery<MitarbeiterCode> typedQuery = db.getCurrentEntityManager().createQuery(
                "select c from MitarbeiterCode c order by c.kuerzel", MitarbeiterCode.class);
        return typedQuery.getResultList();
    }

}

