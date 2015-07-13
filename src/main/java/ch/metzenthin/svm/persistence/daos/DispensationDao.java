package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;

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

    @Override
    public void remove(Dispensation dispensation) {
        throw new NullPointerException("Operation not supported");
    }

    public Schueler addToSchuelerAndSave(Dispensation dispensation, Schueler schueler) {
        schueler.addDispensation(dispensation);
        entityManager.persist(schueler);
        return schueler;
    }

    public Schueler removeFromSchuelerAndUpdate(Dispensation dispensation, Schueler schueler) {
        schueler.deleteDispensation(dispensation);
        entityManager.persist(schueler);
        return schueler;
    }

}

