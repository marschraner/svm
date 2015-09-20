package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MitarbeiterDao extends GenericDao<Mitarbeiter, Integer> {

    public MitarbeiterDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Mitarbeiter save(Mitarbeiter mitarbeiter) {
        super.save(mitarbeiter);
        if (mitarbeiter.getAdresse() != null) {
            entityManager.refresh(mitarbeiter.getAdresse());
        }
        return mitarbeiter;
    }

    @Override
    public void remove(Mitarbeiter mitarbeiter) {
        // Lösche zugewiesene Codes
        for (MitarbeiterCode mitarbeiterCode : new HashSet<>(mitarbeiter.getMitarbeiterCodes())) {
            mitarbeiter.deleteCode(mitarbeiterCode);
            entityManager.refresh(mitarbeiterCode);
        }

        // Lösche Mitarbeiter aus DB
        entityManager.remove(mitarbeiter);
    }

    public List<Mitarbeiter> findAll() {
        TypedQuery<Mitarbeiter> typedQuery = entityManager.createQuery("select l from Mitarbeiter l order by l.nachname, l.vorname, l.geburtsdatum", Mitarbeiter.class);
        return typedQuery.getResultList();
    }

}
