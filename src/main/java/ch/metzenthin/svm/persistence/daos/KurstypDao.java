package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Kurstyp;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class KurstypDao extends GenericDao<Kurstyp, Integer> {

    public List<Kurstyp> findAll() {
        TypedQuery<Kurstyp> typedQuery = db.getCurrentEntityManager().createQuery(
                "select k from Kurstyp k order by k.bezeichnung", Kurstyp.class);
        return typedQuery.getResultList();
    }

}
