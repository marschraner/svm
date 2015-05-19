package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Adresse;

import javax.persistence.EntityManager;

/**
 * @author Hans Stamm
 */
public class AdresseDao extends GenericDao<Adresse, Integer> {

    public AdresseDao(EntityManager entityManager) {
        super(entityManager);
    }

}
