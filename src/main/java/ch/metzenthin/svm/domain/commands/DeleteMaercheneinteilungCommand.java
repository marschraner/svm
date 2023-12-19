package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaercheneinteilungDao;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteMaercheneinteilungCommand implements Command {

    private final MaercheneinteilungDao maercheneinteilungDao = new MaercheneinteilungDao();

    // input
    private final List<Maercheneinteilung> maercheneinteilungen;
    private final int indexMaercheneinteilungToBeDeleted;

    public DeleteMaercheneinteilungCommand(List<Maercheneinteilung> maercheneinteilungen, int indexMaercheneinteilungToBeDeleted) {
        this.maercheneinteilungen = maercheneinteilungen;
        this.indexMaercheneinteilungToBeDeleted = indexMaercheneinteilungToBeDeleted;
    }

    @Override
    public void execute() {
        Maercheneinteilung maercheneinteilungToBeDeleted = maercheneinteilungen.get(indexMaercheneinteilungToBeDeleted);
        maercheneinteilungDao.remove(maercheneinteilungToBeDeleted);
        maercheneinteilungen.remove(indexMaercheneinteilungToBeDeleted);
    }

}
