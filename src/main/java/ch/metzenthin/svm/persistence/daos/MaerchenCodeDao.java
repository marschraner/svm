package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.MaerchenCode;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaerchenCodeDao extends GenericDao<MaerchenCode, Integer> {

    public MaerchenCodeDao(EntityManager entityManager) {
        super(entityManager);
    }

    public List<MaerchenCode> findAll() {
        TypedQuery<MaerchenCode> typedQuery = entityManager.createQuery("select c from MaerchenCode c order by c.kuerzel", MaerchenCode.class);
        return typedQuery.getResultList();
    }

}

