package ch.metzenthin.svm.model.daos;

import ch.metzenthin.svm.model.entities.Adresse;

import javax.persistence.EntityManager;

/**
 * @author Hans Stamm
 */
public class AdresseDao extends GenericDao<Adresse, Integer> {

    public AdresseDao(EntityManager entityManager) {
        super(entityManager);
    }

}
