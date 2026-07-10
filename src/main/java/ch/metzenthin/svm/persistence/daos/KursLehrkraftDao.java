package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.*;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursLehrkraftDao extends GenericDao<KursLehrkraft, Integer> {

  public List<KursLehrkraft> findKursLehrkraefteByKursId(int kursId) {
    TypedQuery<KursLehrkraft> typedQuery =
        db.getCurrentEntityManager()
            .createQuery(
                "SELECT kl FROM KursLehrkraft kl "
                    + "WHERE kl.kurs.kursId = :kursId "
                    + "ORDER BY kl.kurs.kursId, kl.lehrkraefteOrder",
                KursLehrkraft.class);
    typedQuery.setParameter("kursId", kursId);
    return typedQuery.getResultList();
  }
}
