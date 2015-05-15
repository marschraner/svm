package ch.metzenthin.svm.commands;

import ch.metzenthin.svm.model.daos.AdresseDao;
import ch.metzenthin.svm.model.entities.Adresse;

/**
 * @author Hans Stamm
 */
public class SaveAdresseCommand extends GenericDaoCommand {

    private Adresse adresse;
    private Adresse savedAdresse;

    public SaveAdresseCommand(Adresse adresse) {
        this.adresse = adresse;
    }

    public Adresse getSavedAdresse() {
        return savedAdresse;
    }

    @Override
    public void execute() {
        AdresseDao adresseDao = new AdresseDao();
        adresseDao.setEntityManager(entityManager);
        savedAdresse = adresseDao.save(adresse);
    }

}
