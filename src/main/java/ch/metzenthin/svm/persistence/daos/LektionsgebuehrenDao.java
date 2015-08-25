package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenDao extends GenericDao<Lektionsgebuehren, Integer> {

    public LektionsgebuehrenDao(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Lektionsgebuehren> findAll() {
        TypedQuery<Lektionsgebuehren> typedQuery = entityManager.createQuery("select l from Lektionsgebuehren l order by l.lektionslaenge", Lektionsgebuehren.class);
        return typedQuery.getResultList();
    }

}
