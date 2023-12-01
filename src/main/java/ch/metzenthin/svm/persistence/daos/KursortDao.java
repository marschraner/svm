package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Kursort;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursortDao extends GenericDao<Kursort, Integer> {

    public List<Kursort> findAll() {
        TypedQuery<Kursort> typedQuery = db.getCurrentEntityManager().createQuery(
                "select k from Kursort k order by k.bezeichnung", Kursort.class);
        return typedQuery.getResultList();
    }

}
