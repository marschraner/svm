package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.daos.MaercheneinteilungDao;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.MaercheneinteilungId;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMaercheneinteilungCommand extends GenericDaoCommand {

    // input
    private Maercheneinteilung maercheneinteilung;
    private final ElternmithilfeCode elternmithilfeCode;
    private Maercheneinteilung maercheneinteilungOrigin;
    private List<Maercheneinteilung> bereitsErfassteMaercheneinteilungen;


    public SaveOrUpdateMaercheneinteilungCommand(Maercheneinteilung maercheneinteilung, ElternmithilfeCode elternmithilfeCode, Maercheneinteilung maercheneinteilungOrigin, List<Maercheneinteilung> bereitsErfassteMaercheneinteilungen) {
        this.maercheneinteilung = maercheneinteilung;
        this.elternmithilfeCode = elternmithilfeCode;
        this.maercheneinteilungOrigin = maercheneinteilungOrigin;
        this.bereitsErfassteMaercheneinteilungen = bereitsErfassteMaercheneinteilungen;
    }

    @Override
    public void execute() {
        MaercheneinteilungDao maercheneinteilungDao = new MaercheneinteilungDao(entityManager);
        ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao(entityManager);
        // Reload zur Verhinderung von Lazy Loading-Problem
        ElternmithilfeCode elternmithilfeCodeReloaded = null;
        if (elternmithilfeCode != null) {
            elternmithilfeCodeReloaded = elternmithilfeCodeDao.findById(elternmithilfeCode.getCodeId());
        }
        if (maercheneinteilungOrigin != null) {
            // Update von maercheneinteilungOrigin mit Werten von maercheneinteilung
            maercheneinteilungOrigin.copyAttributesFrom(maercheneinteilung);
            maercheneinteilungOrigin.setElternmithilfeCode(elternmithilfeCodeReloaded);
            // Reload zur Verhinderung von Lazy Loading-Problem
            Maercheneinteilung maercheneinteilungOriginReloaded = maercheneinteilungDao.findById(new MaercheneinteilungId(maercheneinteilungOrigin.getSchueler().getPersonId(), maercheneinteilungOrigin.getMaerchen().getMaerchenId()));
            maercheneinteilungDao.save(maercheneinteilungOriginReloaded);
        } else {
            maercheneinteilung.setElternmithilfeCode(elternmithilfeCodeReloaded);
            Maercheneinteilung maercheneinteilungSaved = maercheneinteilungDao.save(maercheneinteilung);
            bereitsErfassteMaercheneinteilungen.add(maercheneinteilungSaved);
        }
        Collections.sort(bereitsErfassteMaercheneinteilungen);
    }

}
