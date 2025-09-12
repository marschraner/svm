package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Maerchen;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaerchenDao extends GenericDao<Maerchen, Integer> {

  public List<Maerchen> findAll() {
    TypedQuery<Maerchen> typedQuery =
        db.getCurrentEntityManager()
            .createQuery("select m from Maerchen m order by m.schuljahr desc", Maerchen.class);
    return typedQuery.getResultList();
  }
}
