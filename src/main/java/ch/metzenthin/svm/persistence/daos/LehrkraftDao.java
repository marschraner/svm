package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class LehrkraftDao extends GenericDao<Lehrkraft, Integer> {

    public LehrkraftDao(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Lehrkraft> findAll() {
        TypedQuery<Lehrkraft> typedQuery = entityManager.createQuery("select l from Lehrkraft l order by l.nachname, l.vorname, l.geburtsdatum", Lehrkraft.class);
        return typedQuery.getResultList();
    }

}
