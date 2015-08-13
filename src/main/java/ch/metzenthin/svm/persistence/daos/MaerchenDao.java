package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Maerchen;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaerchenDao extends GenericDao<Maerchen, Integer> {

    public MaerchenDao(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Maerchen> findAll() {
        TypedQuery<Maerchen> typedQuery = entityManager.createQuery("select m from Maerchen m order by m.schuljahr desc", Maerchen.class);
        return typedQuery.getResultList();
    }

}

