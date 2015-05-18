package ch.metzenthin.svm.commands;

import ch.metzenthin.svm.model.daos.AngehoerigerDao;
import ch.metzenthin.svm.model.entities.Angehoeriger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveAngehoerigeCommand extends GenericDaoCommand {

    private List<Angehoeriger> angehoerige;
    private List<Angehoeriger> savedAngehoerige = new ArrayList<>();

    public SaveAngehoerigeCommand(List<Angehoeriger> angehoerige) {
        this.angehoerige = angehoerige;
    }

    @Override
    public void execute() {
        AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);

        // Müssen alle in derselben Session bzw. vom selben Entity Manager ausgeführt werden!!!
        for (Angehoeriger angehoeriger : angehoerige) {
            Angehoeriger savedAngehoeriger = angehoerigerDao.save(angehoeriger);
            savedAngehoerige.add(savedAngehoeriger);
        }
    }

    public List<Angehoeriger> getSavedAngehoeriger() {
        return savedAngehoerige;
    }

}
