package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class ElternmithilfeCodeDao extends GenericDao<ElternmithilfeCode, Integer> {

    public List<ElternmithilfeCode> findAll() {
        TypedQuery<ElternmithilfeCode> typedQuery = db.getCurrentEntityManager().createQuery(
                "select c from ElternmithilfeCode c order by c.kuerzel", ElternmithilfeCode.class);
        return typedQuery.getResultList();
    }

}

