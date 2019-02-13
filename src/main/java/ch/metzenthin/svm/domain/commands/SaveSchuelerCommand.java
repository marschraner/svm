package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Hans Stamm
 */
public class SaveSchuelerCommand implements Command {

    private final SchuelerDao schuelerDao = new SchuelerDao();

    private Schueler schueler;
    private Schueler savedSchueler;

    public SaveSchuelerCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    Schueler getSavedSchueler() {
        return savedSchueler;
    }

    @Override
    public void execute() {
        savedSchueler = schuelerDao.save(schueler);
    }

}
