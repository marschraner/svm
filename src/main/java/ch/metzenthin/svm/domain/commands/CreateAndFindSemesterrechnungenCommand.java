package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungAktiveSchuelerComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        // 1. Suche noch nicht erfasste Semesterrechnungen, d.h. Rechnungsempfänger ohne Semesterrechnung mit aktuellen Kursen (-> Nachrechnung) oder Kursen vom Vorsemester ohne Abmeldung (-> Vorrechnung)
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

        // 4. Lösche Semesterrechnungen mit verwaisten Rechnungsempfängern (Rechnungsempfänger ohne Schüler)
        DeleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand deleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand = new DeleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand(semesterrechnungenFound);
        deleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand.setEntityManager(entityManager);
        deleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand.execute();

        // 5. Sortierung nach aktiven Schülern (analog zur Schueler-Spalte in SemesterrechnungenTableData)
        Semester currentSemester = semesterrechnungenSuchenModel.getSemester();
        FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(currentSemester);
        findPreviousSemesterCommand.setEntityManager(entityManager);
        findPreviousSemesterCommand.execute();
        Semester previousSemester = findPreviousSemesterCommand.getPreviousSemester();
        Comparator<Semesterrechnung> semesterrechnungAktiveSchuelerComparator = new SemesterrechnungAktiveSchuelerComparator(previousSemester);
        Collections.sort(semesterrechnungenFound, semesterrechnungAktiveSchuelerComparator);
    }

    public List<Semesterrechnung> getSemesterrechnungenFound() {
        return semesterrechnungenFound;
    }
}
