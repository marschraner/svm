package ch.metzenthin.svm.persistence.daos;

import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.persistence.EntityManager;

/**
 * @author Martin Schraner
 */
public class SchuelerDao extends GenericDao<Schueler, Integer> {

    public SchuelerDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void remove(Schueler schueler) {

        // Remove schueler from adresse, vater, mutter and rechnungsempaenger
        Adresse adresse = schueler.getAdresse();
        adresse.getPersonen().remove(schueler);

        Angehoeriger vater = schueler.getVater();
        if (vater != null) {
            vater.getKinderVater().remove(schueler);
        }

        Angehoeriger mutter = schueler.getMutter();
        if (mutter != null) {
            mutter.getKinderMutter().remove(schueler);
        }

        Angehoeriger rechnungsempfaenger = schueler.getRechnungsempfaenger();
        rechnungsempfaenger.getSchuelerRechnungsempfaenger().remove(schueler);

        // Remove schueler from db
        entityManager.remove(schueler);

        // Remove adresse, vater, mutter and rechnungsempfaenger if they are not referenced any more
        if (adresse.getPersonen().size() == 0) {
            AdresseDao adresseDao = new AdresseDao(entityManager);
            adresseDao.remove(adresse);
        }

        AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);
        if (vater != null && entityManager.contains(vater) && vater.getKinderVater().size() == 0 && vater.getSchuelerRechnungsempfaenger().size() == 0) {
            angehoerigerDao.remove(vater);
        }

        if (mutter != null && entityManager.contains(mutter) && mutter.getKinderMutter().size() == 0 && mutter.getSchuelerRechnungsempfaenger().size() == 0) {
            angehoerigerDao.remove(mutter);
        }

        if (entityManager.contains(rechnungsempfaenger) && rechnungsempfaenger.getKinderVater().size() == 0 && rechnungsempfaenger.getKinderMutter().size() == 0 && rechnungsempfaenger.getSchuelerRechnungsempfaenger().size() == 0) {
            angehoerigerDao.remove(rechnungsempfaenger);
        }
    }

}
