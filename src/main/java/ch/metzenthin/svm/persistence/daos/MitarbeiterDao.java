package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MitarbeiterDao extends GenericDao<Mitarbeiter, Integer> {

    public MitarbeiterDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Mitarbeiter save(Mitarbeiter entity) {
        super.save(entity);
        entityManager.refresh(entity.getAdresse());
        return entity;
    }

    public List<Mitarbeiter> findAll() {
        TypedQuery<Mitarbeiter> typedQuery = entityManager.createQuery("select l from Mitarbeiter l order by l.nachname, l.vorname, l.geburtsdatum", Mitarbeiter.class);
        return typedQuery.getResultList();
    }

}
