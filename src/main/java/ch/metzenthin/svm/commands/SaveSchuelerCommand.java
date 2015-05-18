package ch.metzenthin.svm.commands;

import ch.metzenthin.svm.model.daos.SchuelerDao;
import ch.metzenthin.svm.model.entities.Schueler;

/**
 * @author Hans Stamm
 */
public class SaveSchuelerCommand extends GenericDaoCommand {

    private Schueler schueler;
    private Schueler savedSchueler;

    public SaveSchuelerCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    public Schueler getSavedSchueler() {
        return savedSchueler;
    }

    @Override
    public void execute() {
        SchuelerDao schuelerDao = new SchuelerDao(entityManager);
        savedSchueler = schuelerDao.save(schueler);
    }

}
