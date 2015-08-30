package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungId;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungDao extends GenericDao<Semesterrechnung, SemesterrechnungId> {

    public SemesterrechnungDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Semesterrechnung save(Semesterrechnung semesterrechnung) {
        semesterrechnung.getSemester().getSemesterrechnungen().add(semesterrechnung);
        semesterrechnung.getRechnungsempfaenger().getSemesterrechnungen().add(semesterrechnung);
        entityManager.persist(semesterrechnung);
        entityManager.flush();
        entityManager.refresh(semesterrechnung);
        return semesterrechnung;
    }

    @Override
    public void remove(Semesterrechnung semesterrechnung) {
        semesterrechnung.getSemester().getSemesterrechnungen().remove(semesterrechnung);
        semesterrechnung.getRechnungsempfaenger().getSemesterrechnungen().remove(semesterrechnung);
        entityManager.remove(semesterrechnung);
    }

    public List<Semesterrechnung> findSemesterrechnungenSemester(Semester semester) {
        TypedQuery<Semesterrechnung> typedQuery = entityManager.createQuery("select semre from Semesterrechnung semre where semre.semester.semesterId = :semesterId", Semesterrechnung.class);
        typedQuery.setParameter("semesterId", semester.getSemesterId());
        List<Semesterrechnung> semesterrechnungenFound = typedQuery.getResultList();
        // Sortieren gemäss compareTo in Semesterrechnungen
        Collections.sort(semesterrechnungenFound);
        return semesterrechnungenFound;
    }

}

