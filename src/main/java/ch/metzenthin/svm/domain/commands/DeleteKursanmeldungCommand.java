package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteKursanmeldungCommand extends GenericDaoCommand {

    // input
    private List<Kursanmeldung> kursanmeldungen;
    int indexKursanmeldungToBeDeleted;

    public DeleteKursanmeldungCommand(List<Kursanmeldung> kursanmeldungen, int indexKursanmeldungToBeDeleted) {
        this.kursanmeldungen = kursanmeldungen;
        this.indexKursanmeldungToBeDeleted = indexKursanmeldungToBeDeleted;
    }

    @Override
    public void execute() {
        KursanmeldungDao kursanmeldungDao = new KursanmeldungDao(entityManager);
        Kursanmeldung kursanmeldungToBeDeleted = kursanmeldungen.get(indexKursanmeldungToBeDeleted);
        kursanmeldungDao.remove(kursanmeldungToBeDeleted);
        kursanmeldungen.remove(indexKursanmeldungToBeDeleted);
    }
    
}
