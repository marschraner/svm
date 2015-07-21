package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Semester;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemesterDao extends GenericDao<Semester, Integer> {

    public SemesterDao(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Semester> findAll() {
        TypedQuery<Semester> typedQuery = entityManager.createQuery("select s from Semester s order by s.semesterbeginn, s.semesterende desc", Semester.class);
        return typedQuery.getResultList();
    }

}

