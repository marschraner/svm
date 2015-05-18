package ch.metzenthin.svm.model.daos;

import ch.metzenthin.svm.model.entities.Dispensation;
import ch.metzenthin.svm.model.entities.Schueler;

import javax.persistence.EntityManager;

/**
 * @author Martin Schraner
 */
public class DispensationDao extends GenericDao<Dispensation, Integer> {

    public DispensationDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Dispensation save(Dispensation dispensation) {
        throw new NullPointerException("Operation not supported");
    }

    public Dispensation save(Dispensation dispensation, Schueler schueler) {
        schueler.addDispensation(dispensation);
        entityManager.persist(schueler);
        return dispensation;
    }


    @Override
    public void remove(Dispensation dispensation) {
        throw new NullPointerException("Operation not supported");
    }



}

