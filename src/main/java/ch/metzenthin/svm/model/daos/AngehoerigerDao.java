package ch.metzenthin.svm.model.daos;

import ch.metzenthin.svm.model.entities.Adresse;
import ch.metzenthin.svm.model.entities.Angehoeriger;

import javax.persistence.EntityManager;

/**
 * @author Martin Schraner
 */
public class AngehoerigerDao extends GenericDao<Angehoeriger, Integer> {

    public AngehoerigerDao(EntityManager entityManager) {
        super(entityManager);
    }

    public void remove(Angehoeriger angehoeriger) {

        // Remove angehoeriger from adresse
        Adresse adresse = angehoeriger.getAdresse();
        adresse.getPersonen().remove(angehoeriger);

        // Remove angehoeriger from db
        entityManager.remove(angehoeriger);

        // Remove adresse from db if it is not referenced any more
        if (adresse.getPersonen().size() == 0) {
            AdresseDao adresseDao = new AdresseDao(entityManager);
            adresseDao.remove(adresse);
        }
    }

}
