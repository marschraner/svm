package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateAndFindSemesterrechnungenCommand extends GenericDaoCommand {

    // input
    private SemesterrechnungenSuchenModel semesterrechnungenSuchenModel;

    // output
    private List<Semesterrechnung> semesterrechnungenFound = new ArrayList<>();

    public CreateAndFindSemesterrechnungenCommand(SemesterrechnungenSuchenModel semesterrechnungenSuchenModel) {
        this.semesterrechnungenSuchenModel = semesterrechnungenSuchenModel;
    }

    @Override
    public void execute() {

        // 1. Suche noch nicht erfasste Semesterrechnungen, d.h. Rechnungsempfänger mit aktuellen Kursen ohne Semesterrechnung
        FindRechnungsempfaengerOhneSemesterrechnungenCommand findRechnungsempfaengerOhneSemesterrechnungenCommand = new FindRechnungsempfaengerOhneSemesterrechnungenCommand(semesterrechnungenSuchenModel.getSemester());
        findRechnungsempfaengerOhneSemesterrechnungenCommand.setEntityManager(entityManager);
        findRechnungsempfaengerOhneSemesterrechnungenCommand.execute();
        List<Angehoeriger> rechnungsempfaengersOhneSemesterrechungen = findRechnungsempfaengerOhneSemesterrechnungenCommand.getRechnungsempfaengersFound();

        // 2. Erzeuge und speichere fehlende Semesterrechnungen
        CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand createSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand =
                new CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand(rechnungsempfaengersOhneSemesterrechungen, semesterrechnungenSuchenModel.getSemester());
        createSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand.setEntityManager(entityManager);
        createSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand.execute();

        // 3. Suche Semesterrechnungen
        FindSemesterrechnungenCommand findSemesterrechnungenCommand = new FindSemesterrechnungenCommand(semesterrechnungenSuchenModel);
        findSemesterrechnungenCommand.setEntityManager(entityManager);
        findSemesterrechnungenCommand.execute();
        semesterrechnungenFound = findSemesterrechnungenCommand.getSemesterrechnungenFound();
        Collections.sort(semesterrechnungenFound);
    }

    public List<Semesterrechnung> getSemesterrechnungenFound() {
        return semesterrechnungenFound;
    }
}
