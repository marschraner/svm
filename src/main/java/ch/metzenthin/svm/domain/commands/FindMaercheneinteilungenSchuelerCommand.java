package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaercheneinteilungDao;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindMaercheneinteilungenSchuelerCommand extends GenericDaoCommand {

    // input
    private Schueler schueler;

    // output
    private List<Maercheneinteilung> maercheneinteilungenFound;

    public FindMaercheneinteilungenSchuelerCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        MaercheneinteilungDao maercheneinteilungDao = new MaercheneinteilungDao(entityManager);
        maercheneinteilungenFound = maercheneinteilungDao.findMaercheneinteilungenSchueler(schueler);
    }

    public List<Maercheneinteilung> getMaercheneinteilungenFound() {
        return maercheneinteilungenFound;
    }
}
