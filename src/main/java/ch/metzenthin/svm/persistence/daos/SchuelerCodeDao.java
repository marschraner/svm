package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SchuelerCodeDao extends GenericDao<SchuelerCode, Integer> {

    public SchuelerCodeDao(EntityManager entityManager) {
        super(entityManager);
    }

    public Schueler addToSchuelerAndSave(SchuelerCode schuelerCode, Schueler schueler) {
        schueler.addCode(schuelerCode);
        entityManager.persist(schueler);
        entityManager.flush();
        entityManager.refresh(schuelerCode);
        entityManager.refresh(schueler);
        return schueler;
    }

    public Schueler removeFromSchuelerAndUpdate(SchuelerCode schuelerCode, Schueler schueler) {
        schueler.deleteCode(schuelerCode);
        entityManager.persist(schueler);
        entityManager.flush();
        entityManager.refresh(schuelerCode);
        entityManager.refresh(schueler);
        return schueler;
    }

    public List<SchuelerCode> findAll() {
        TypedQuery<SchuelerCode> typedQuery = entityManager.createQuery("select c from SchuelerCode c order by c.kuerzel", SchuelerCode.class);
        return typedQuery.getResultList();
    }

}

