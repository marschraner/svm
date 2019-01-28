package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveAngehoerigeCommand extends GenericDaoCommand {

    private final AngehoerigerDao angehoerigerDao = new AngehoerigerDao();

    private List<Angehoeriger> angehoerige;
    private List<Angehoeriger> savedAngehoerige = new ArrayList<>();

    SaveAngehoerigeCommand(List<Angehoeriger> angehoerige) {
        this.angehoerige = angehoerige;
    }

    @Override
    public void execute() {
        // Müssen alle in derselben Session bzw. vom selben Entity Manager ausgeführt werden!!!
        for (Angehoeriger angehoeriger : angehoerige) {
            Angehoeriger savedAngehoeriger = angehoerigerDao.save(angehoeriger);
            savedAngehoerige.add(savedAngehoeriger);
        }
    }

    List<Angehoeriger> getSavedAngehoerige() {
        return savedAngehoerige;
    }

}
