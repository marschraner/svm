package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Kurstyp;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KurstypDao extends GenericDao<Kurstyp, Integer> {

    public KurstypDao(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Kurstyp> findAll() {
        TypedQuery<Kurstyp> typedQuery = entityManager.createQuery("select k from Kurstyp k order by k.bezeichnung", Kurstyp.class);
        return typedQuery.getResultList();
    }

}
