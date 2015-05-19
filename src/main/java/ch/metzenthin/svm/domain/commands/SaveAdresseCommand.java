package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AdresseDao;
import ch.metzenthin.svm.persistence.entities.Adresse;

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
        AdresseDao adresseDao = new AdresseDao(entityManager);
        savedAdresse = adresseDao.save(adresse);
    }

}
