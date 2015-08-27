package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungId;

import javax.persistence.EntityManager;

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

}

