package ch.metzenthin.svm.commands;

import ch.metzenthin.svm.model.daos.AdresseDaoJpa;
import ch.metzenthin.svm.model.entities.Adresse;

/**
 * @author Hans Stamm
 */
public class AdresseInsertCommand extends GenericDaoCommand {

    private Adresse adresse;
    private Adresse newAdresse;

    public AdresseInsertCommand(Adresse adresse) {
        this.adresse = adresse;
    }

    public Adresse getNewAdresse() {
        return newAdresse;
    }

    @Override
    public void execute() {
        AdresseDaoJpa adresseDao = new AdresseDaoJpa();
        adresseDao.setEntityManager(entityManager);
        newAdresse = adresseDao.save(adresse);
    }

}
