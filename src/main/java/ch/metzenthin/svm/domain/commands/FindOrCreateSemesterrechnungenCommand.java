package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindOrCreateSemesterrechnungenCommand extends GenericDaoCommand {

    // input
    private SemesterrechnungenSuchenModel semesterrechnungenSuchenModel;

    // output
    private List<Semesterrechnung> foundOrCreatedSemesterrechnungen = new ArrayList<>();

    private List<Semesterrechnung> semesterrechnungenCreated;

    public FindOrCreateSemesterrechnungenCommand(SemesterrechnungenSuchenModel semesterrechnungenSuchenModel) {
        this.semesterrechnungenSuchenModel = semesterrechnungenSuchenModel;
    }

    @Override
    public void execute() {

        // 1. Bereits erfasste Semesterrechnungen
        FindErfassteSemesterrechnungenCommand findErfassteSemesterrechnungenCommand = new FindErfassteSemesterrechnungenCommand(semesterrechnungenSuchenModel);
        findErfassteSemesterrechnungenCommand.setEntityManager(entityManager);
        findErfassteSemesterrechnungenCommand.execute();
        List<Semesterrechnung> erfassteSemesterechnungen = findErfassteSemesterrechnungenCommand.getSemesterrechnungenFound();

        // 2. Rechnungsemfänger ohne Semesterrechnung
        FindRechnungsempfaengerOhneSemesterrechnungenCommand findRechnungsempfaengerOhneSemesterrechnungenCommand = new FindRechnungsempfaengerOhneSemesterrechnungenCommand(semesterrechnungenSuchenModel);
        findRechnungsempfaengerOhneSemesterrechnungenCommand.setEntityManager(entityManager);
        findRechnungsempfaengerOhneSemesterrechnungenCommand.execute();
        List<Angehoeriger> rechnungsempfaengersOhneSemesterrechungen = findRechnungsempfaengerOhneSemesterrechnungenCommand.getRechnungsempfaengersFound();

        // 3. Erzeuge fehlende Semesterrechnungen
        CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand createSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand =
                new CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand(rechnungsempfaengersOhneSemesterrechungen, semesterrechnungenSuchenModel.getSemester());
        createSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand.setEntityManager(entityManager);
        createSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand.execute();
        semesterrechnungenCreated = createSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand.getSemesterrechnungenCreated();

        // 4. Suchfilter auf erzeugte Semesterrechnungen anwenden
        filterSemesterrechnungenCreated();

        foundOrCreatedSemesterrechnungen = erfassteSemesterechnungen;
        foundOrCreatedSemesterrechnungen.addAll(semesterrechnungenCreated);
        Collections.sort(foundOrCreatedSemesterrechnungen);
    }

    private void filterSemesterrechnungenCreated() {
        // SemesterrechnungCode
        if (semesterrechnungenSuchenModel.getSemesterrechnungCode() != null && semesterrechnungenSuchenModel.getSemesterrechnungCode() != SemesterrechnungenSuchenModel.SEMESTERRECHNUNG_CODE_ALLE) {
            Iterator<Semesterrechnung> it = semesterrechnungenCreated.iterator();
            while (it.hasNext()) {
                Semesterrechnung semesterrechnungIt = it.next();
                if (semesterrechnungIt.getSemesterrechnungCode() == null || !semesterrechnungIt.getSemesterrechnungCode().isIdenticalWith(semesterrechnungenSuchenModel.getSemesterrechnungCode())) {
                    it.remove();
                }
            }
        }
        // Stipendium
        if (semesterrechnungenSuchenModel.getStipendium() != null && semesterrechnungenSuchenModel.getStipendium() != Stipendium.KEINES) {
            Iterator<Semesterrechnung> it = semesterrechnungenCreated.iterator();
            while (it.hasNext()) {
                Semesterrechnung semesterrechnungIt = it.next();
                if (semesterrechnungIt.getStipendium() == null || semesterrechnungIt.getStipendium() != semesterrechnungenSuchenModel.getStipendium()) {
                    it.remove();
                }
            }
        }
        // Gratiskinder
        Iterator<Semesterrechnung> it = semesterrechnungenCreated.iterator();
        while (it.hasNext()) {
            Semesterrechnung semesterrechnungIt = it.next();
            if (semesterrechnungIt.getGratiskinder() != semesterrechnungenSuchenModel.isGratiskinder()) {
                it.remove();
            }
        }
    }

    public List<Semesterrechnung> getFoundOrCreatedSemesterrechnungen() {
        return foundOrCreatedSemesterrechnungen;
    }
}
