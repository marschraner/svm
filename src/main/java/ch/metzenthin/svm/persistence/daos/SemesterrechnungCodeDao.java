package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungCodeDao extends GenericDao<SemesterrechnungCode, Integer> {

    public List<SemesterrechnungCode> findAll() {
        TypedQuery<SemesterrechnungCode> typedQuery = db.getCurrentEntityManager().createQuery(
                "select c from SemesterrechnungCode c order by c.kuerzel", SemesterrechnungCode.class);
        return typedQuery.getResultList();
    }

}

