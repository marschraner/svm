package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.SemesterrechnungenSuchenModel;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungSortByAktiveSchuelerComparator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateAndFindSemesterrechnungenCommand implements Command {

  // input
  private final SemesterrechnungenSuchenModel semesterrechnungenSuchenModel;

  // output
  private List<Semesterrechnung> semesterrechnungenFound = new ArrayList<>();

  public CreateAndFindSemesterrechnungenCommand(
      SemesterrechnungenSuchenModel semesterrechnungenSuchenModel) {
    this.semesterrechnungenSuchenModel = semesterrechnungenSuchenModel;
  }

  @SuppressWarnings("ExtractMethodRecommender")
  @Override
  public void execute() {

    // 1. Ist gewähltes Semester später als nächstes Semester?
    //    -> Keine Semesterrechnungen erstellen und leere Liste zurückgeben
    FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
    findAllSemestersCommand.execute();
    List<Semester> semestersAll = findAllSemestersCommand.getSemestersAll();
    FindSemesterForCalendarCommand findSemesterForCalendarCommand =
        new FindSemesterForCalendarCommand(semestersAll);
    findSemesterForCalendarCommand.execute();
    Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
    if (nextSemester != null && semesterrechnungenSuchenModel.getSemester().isAfter(nextSemester)) {
      semesterrechnungenFound = new ArrayList<>();
      return;
    }

    // 2. Suche noch nicht erfasste Semesterrechnungen, d.h. Rechnungsempfänger ohne
    // Semesterrechnung mit aktuellen Kursen (-> Nachrechnung) oder Kursen vom Vorsemester ohne
    // Abmeldung (-> Vorrechnung)
    FindRechnungsempfaengerOhneSemesterrechnungenCommand
        findRechnungsempfaengerOhneSemesterrechnungenCommand =
            new FindRechnungsempfaengerOhneSemesterrechnungenCommand(
                semesterrechnungenSuchenModel.getSemester());
    findRechnungsempfaengerOhneSemesterrechnungenCommand.execute();
    List<Angehoeriger> rechnungsempfaengersOhneSemesterrechnungen =
        findRechnungsempfaengerOhneSemesterrechnungenCommand.getRechnungsempfaengersFound();

    // 3. Erzeuge und speichere fehlende Semesterrechnungen
    CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand
        createSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand =
            new CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand(
                rechnungsempfaengersOhneSemesterrechnungen,
                semesterrechnungenSuchenModel.getSemester());
    createSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand.execute();

    // 4. Suche Semesterrechnungen
    FindSemesterrechnungenCommand findSemesterrechnungenCommand =
        new FindSemesterrechnungenCommand(semesterrechnungenSuchenModel);
    findSemesterrechnungenCommand.execute();
    semesterrechnungenFound = findSemesterrechnungenCommand.getSemesterrechnungenFound();

    // 5. Lösche Semesterrechnungen mit verwaisten Rechnungsempfängern (Rechnungsempfänger ohne
    // Schüler)
    DeleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand
        deleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand =
            new DeleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand(
                semesterrechnungenFound);
    deleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand.execute();

    // 6. Sortierung nach aktiven Schülern (analog zur Schueler-Spalte in
    // SemesterrechnungenTableData)
    Semester currentSemester = semesterrechnungenSuchenModel.getSemester();
    FindPreviousSemesterCommand findPreviousSemesterCommand =
        new FindPreviousSemesterCommand(currentSemester);
    findPreviousSemesterCommand.execute();
    Semester previousSemester = findPreviousSemesterCommand.getPreviousSemester();
    Comparator<Semesterrechnung> semesterrechnungSortByAktiveSchuelerComparator =
        new SemesterrechnungSortByAktiveSchuelerComparator(previousSemester);
    semesterrechnungenFound.sort(semesterrechnungSortByAktiveSchuelerComparator);
  }

  public List<Semesterrechnung> getSemesterrechnungenFound() {
    return semesterrechnungenFound;
  }
}
