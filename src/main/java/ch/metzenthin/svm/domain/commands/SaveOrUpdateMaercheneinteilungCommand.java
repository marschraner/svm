package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.daos.MaercheneinteilungDao;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMaercheneinteilungCommand implements Command {

    private final MaercheneinteilungDao maercheneinteilungDao = new MaercheneinteilungDao();
    private final ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao();

    // input
    private final Maercheneinteilung maercheneinteilung;
    private final ElternmithilfeCode elternmithilfeCode;
    private final ElternmithilfeDrittperson elternmithilfeDrittperson;
    private final Adresse elternmithilfeDrittpersonAdresse;
    private final Maercheneinteilung maercheneinteilungOrigin;
    private final List<Maercheneinteilung> bereitsErfassteMaercheneinteilungen;

    public SaveOrUpdateMaercheneinteilungCommand(Maercheneinteilung maercheneinteilung, ElternmithilfeCode elternmithilfeCode, ElternmithilfeDrittperson elternmithilfeDrittperson, Adresse elternmithilfeDrittpersonAdresse, Maercheneinteilung maercheneinteilungOrigin, List<Maercheneinteilung> bereitsErfassteMaercheneinteilungen) {
        this.maercheneinteilung = maercheneinteilung;
        this.elternmithilfeCode = elternmithilfeCode;
        this.elternmithilfeDrittperson = elternmithilfeDrittperson;
        this.elternmithilfeDrittpersonAdresse = elternmithilfeDrittpersonAdresse;
        this.maercheneinteilungOrigin = maercheneinteilungOrigin;
        this.bereitsErfassteMaercheneinteilungen = bereitsErfassteMaercheneinteilungen;
    }

    @Override
    public void execute() {
        // Reload zur Verhinderung von Lazy Loading-Problem
        ElternmithilfeCode elternmithilfeCodeReloaded = null;
        if (elternmithilfeCode != null) {
            elternmithilfeCodeReloaded = elternmithilfeCodeDao.findById(elternmithilfeCode.getCodeId());
        }
        if (elternmithilfeDrittperson != null) {
            elternmithilfeDrittperson.setAdresse(elternmithilfeDrittpersonAdresse);
        }
        if (maercheneinteilungOrigin != null) {
            // Update von maercheneinteilungOrigin mit Werten von maercheneinteilung
            maercheneinteilungOrigin.copyAttributesFrom(maercheneinteilung);
            maercheneinteilungOrigin.setElternmithilfeCode(elternmithilfeCodeReloaded);
            // Elternmithilfe-Drittperson
            if (maercheneinteilungOrigin.getElternmithilfeDrittperson() != null && elternmithilfeDrittperson != null) {
                maercheneinteilungOrigin.getElternmithilfeDrittperson().copyAttributesFrom(elternmithilfeDrittperson);
                maercheneinteilungOrigin.getElternmithilfeDrittperson().getAdresse().copyAttributesFrom(elternmithilfeDrittperson.getAdresse());
            } else {
                maercheneinteilungOrigin.setElternmithilfeDrittperson(elternmithilfeDrittperson);
            }
            // Reload zur Verhinderung von Lazy Loading-Problem
            Maercheneinteilung maercheneinteilungOriginReloaded = maercheneinteilungDao.findById(new MaercheneinteilungId(maercheneinteilungOrigin.getSchueler().getPersonId(), maercheneinteilungOrigin.getMaerchen().getMaerchenId()));
            maercheneinteilungDao.save(maercheneinteilungOriginReloaded);
        } else {
            maercheneinteilung.setElternmithilfeCode(elternmithilfeCodeReloaded);
            maercheneinteilung.setElternmithilfeDrittperson(elternmithilfeDrittperson);
            Maercheneinteilung maercheneinteilungSaved = maercheneinteilungDao.save(maercheneinteilung);
            bereitsErfassteMaercheneinteilungen.add(maercheneinteilungSaved);
        }
        Collections.sort(bereitsErfassteMaercheneinteilungen);
    }

}
