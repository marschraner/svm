package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMitarbeiterCommand implements Command {

    private final MitarbeiterDao mitarbeiterDao = new MitarbeiterDao();
    private final MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao();

    // input
    private final Mitarbeiter mitarbeiter;
    private final Adresse adresse;
    private final Set<MitarbeiterCode> mitarbeiterCodes;
    private final Mitarbeiter mitarbeiterOrigin;
    private final List<Mitarbeiter> bereitsErfassteMitarbeiter;

    // output
    private Mitarbeiter mitarbeiterSaved;

    public SaveOrUpdateMitarbeiterCommand(Mitarbeiter mitarbeiter, Adresse adresse, Set<MitarbeiterCode> mitarbeiterCodes, Mitarbeiter mitarbeiterOrigin, List<Mitarbeiter> bereitsErfassteMitarbeiter) {
        this.mitarbeiter = mitarbeiter;
        this.adresse = adresse;
        this.mitarbeiterCodes = mitarbeiterCodes;
        this.mitarbeiterOrigin = mitarbeiterOrigin;
        this.bereitsErfassteMitarbeiter = bereitsErfassteMitarbeiter;
    }

    @Override
    public void execute() {
        if (isSetAdresse()) {
            mitarbeiter.setAdresse(adresse);
        }
        if (mitarbeiterOrigin != null) {
            // Update von codeOrigin mit Werten von code
            mitarbeiterOrigin.copyAttributesFrom(mitarbeiter);
            if (isSetAdresse()) {
                if (mitarbeiterOrigin.getAdresse() != null) {
                    mitarbeiterOrigin.getAdresse().copyAttributesFrom(mitarbeiter.getAdresse());
                } else {
                    mitarbeiterOrigin.setAdresse(adresse);
                }
            } else {
                // Adresse entfernt
                if (mitarbeiterOrigin.getAdresse() != null) {
                    mitarbeiterOrigin.setAdresse(null);
                }
            }
            mitarbeiterSaved = mitarbeiterDao.save(mitarbeiterOrigin);
            addMitarbeiterCodesMitarbeiterBearbeiten();
            deleteMitarbeiterCodesMitarbeiterBearbeiten();
        } else {
            // Neuer Mitarbeiter
            mitarbeiterSaved = mitarbeiterDao.save(mitarbeiter);
            bereitsErfassteMitarbeiter.add(mitarbeiterSaved);
            addMitarbeiterCodesNeuerMitarbeiter();
        }
        Collections.sort(bereitsErfassteMitarbeiter);
    }

    private void addMitarbeiterCodesNeuerMitarbeiter() {
        if (mitarbeiterCodes == null) {
            return;
        }
        for (MitarbeiterCode mitarbeiterCode : mitarbeiterCodes) {
            mitarbeiterSaved = mitarbeiterCodeDao.addToMitarbeiterAndSave(mitarbeiterCode, mitarbeiterSaved);
        }
    }

    private void addMitarbeiterCodesMitarbeiterBearbeiten() {
        if (mitarbeiterCodes == null) {
            return;
        }
        Set<MitarbeiterCode> mitarbeiterCodesOrigin = mitarbeiterOrigin.getMitarbeiterCodes();
        for (MitarbeiterCode mitarbeiterCode : mitarbeiterCodes) {
            boolean found = false;
            for (MitarbeiterCode mitarbeiterCodeOrigin : mitarbeiterCodesOrigin) {
                if (mitarbeiterCode.isIdenticalWith(mitarbeiterCodeOrigin)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                mitarbeiterSaved = mitarbeiterCodeDao.addToMitarbeiterAndSave(mitarbeiterCode, mitarbeiterSaved);
            }
        }
    }

    private void deleteMitarbeiterCodesMitarbeiterBearbeiten() {
        if (mitarbeiterCodes == null) {
            return;
        }
        Set<MitarbeiterCode> mitarbeiterCodesOrigin = mitarbeiterOrigin.getMitarbeiterCodes();
        Iterator<MitarbeiterCode> it = mitarbeiterCodesOrigin.iterator();
        while (it.hasNext()) {
            boolean found = false;
            MitarbeiterCode mitarbeiterCodeOriginIt = it.next();
            for (MitarbeiterCode mitarbeiterCode : mitarbeiterCodes) {
                if (mitarbeiterCode.isIdenticalWith(mitarbeiterCodeOriginIt)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                it.remove();
                mitarbeiterSaved = mitarbeiterCodeDao.removeFromMitarbeiterAndUpdate(mitarbeiterCodeOriginIt, mitarbeiterSaved);
            }
        }
    }

    private boolean isSetAdresse() {
        return checkNotEmpty(adresse.getPlz())
                && checkNotEmpty(adresse.getOrt());
    }
}
