package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungDao extends GenericDao<Semesterrechnung, SemesterrechnungId> {

  @Override
  public Semesterrechnung save(Semesterrechnung semesterrechnung) {
    if (!semesterrechnung.getSemester().getSemesterrechnungen().contains(semesterrechnung)) {
      semesterrechnung.getSemester().getSemesterrechnungen().add(semesterrechnung);
    }
    if (!semesterrechnung
        .getRechnungsempfaenger()
        .getSemesterrechnungen()
        .contains(semesterrechnung)) {
      semesterrechnung.getRechnungsempfaenger().getSemesterrechnungen().add(semesterrechnung);
    }
    EntityManager entityManager = db.getCurrentEntityManager();
    entityManager.persist(semesterrechnung);
    entityManager.flush();
    entityManager.refresh(semesterrechnung);
    return semesterrechnung;
  }

  @Override
  public void remove(Semesterrechnung semesterrechnung) {
    semesterrechnung.getSemester().getSemesterrechnungen().remove(semesterrechnung);
    semesterrechnung.getRechnungsempfaenger().getSemesterrechnungen().remove(semesterrechnung);
    db.getCurrentEntityManager().remove(semesterrechnung);
  }

  public List<Semesterrechnung> findSemesterrechnungenSemester(Semester semester) {
    TypedQuery<Semesterrechnung> typedQuery =
        db.getCurrentEntityManager()
            .createQuery(
                "select semre from Semesterrechnung semre where semre.semester.semesterId = :semesterId",
                Semesterrechnung.class);
    typedQuery.setParameter("semesterId", semester.getSemesterId());
    List<Semesterrechnung> semesterrechnungenFound = typedQuery.getResultList();
    // Sortieren gemäss compareTo in Semesterrechnungen
    Collections.sort(semesterrechnungenFound);
    return semesterrechnungenFound;
  }
}
