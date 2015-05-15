package ch.metzenthin.svm.model.daos;

import ch.metzenthin.svm.model.entities.Adresse;
import ch.metzenthin.svm.model.entities.Angehoeriger;

/**
 * @author Martin Schraner
 */
public class AngehoerigerDao extends GenericDao<Angehoeriger, Integer> {

    @Override
    public void remove(Angehoeriger angehoeriger) {
        Adresse adresse = angehoeriger.getAdresse();
        adresse.getPersonen().remove(angehoeriger);
        entityManager.remove(angehoeriger);

        // Remove adresse if it is not referenced any more
        if (adresse.getPersonen().size() == 0) {
            entityManager.remove(adresse);
        }
    }

}
