package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Semester;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemesterDao extends GenericDao<Semester, Integer> {

  public List<Semester> findAll() {
    TypedQuery<Semester> typedQuery =
        db.getCurrentEntityManager()
            .createQuery(
                "select s from Semester s order by s.semesterbeginn desc, s.semesterende desc",
                Semester.class);
    return typedQuery.getResultList();
  }
}
