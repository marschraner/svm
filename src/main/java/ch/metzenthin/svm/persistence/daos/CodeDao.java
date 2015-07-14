package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CodeDao extends GenericDao<Code, Integer> {

    public CodeDao(EntityManager entityManager) {
        super(entityManager);
    }

    public Schueler addToSchuelerAndSave(Code code, Schueler schueler) {
        schueler.addCode(code);
        entityManager.persist(schueler);
        return schueler;
    }

    public Schueler removeFromSchuelerAndUpdate(Code code, Schueler schueler) {
        schueler.deleteCode(code);
        entityManager.persist(schueler);
        return schueler;
    }

    public List<Code> findAll() {
        TypedQuery<Code> typedQuery = entityManager.createQuery("select c from Code c order by c.kuerzel", Code.class);
        return typedQuery.getResultList();
    }

}

