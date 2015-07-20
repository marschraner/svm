package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Kursort;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursortDao extends GenericDao<Kursort, Integer> {

    public KursortDao(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Kursort> findAll() {
        TypedQuery<Kursort> typedQuery = entityManager.createQuery("select k from Kursort k order by k.bezeichnung", Kursort.class);
        return typedQuery.getResultList();
    }

}
