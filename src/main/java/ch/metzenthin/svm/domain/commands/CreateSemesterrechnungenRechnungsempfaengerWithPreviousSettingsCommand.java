package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand extends GenericDaoCommand {

    // input
    private List<Angehoeriger> rechnungsempfaengers;
    private Semester semester;

    // output
    private List<Semesterrechnung> semesterrechnungenCreated = new ArrayList<>();

    public CreateSemesterrechnungenRechnungsempfaengerWithPreviousSettingsCommand(List<Angehoeriger> rechnungsempfaengers, Semester semester) {
        this.rechnungsempfaengers = rechnungsempfaengers;
        this.semester = semester;
    }

    @Override
    public void execute() {

        for (Angehoeriger rechnungsempfaenger : rechnungsempfaengers) {
            Semesterrechnung semesterrechnung = new Semesterrechnung();
            semesterrechnung.setSemester(semester);
            semesterrechnung.setRechnungsempfaenger(rechnungsempfaenger);
            semesterrechnung.setAnzahlWochen(semester.getAnzahlSchulwochen());
            semesterrechnung.setGratiskinder(false);

            if (!rechnungsempfaenger.getSemesterrechnungen().isEmpty()) {
                Semesterrechnung previousSemesterrechnung = rechnungsempfaenger.getSemesterrechnungenAsList().get(0);
                semesterrechnung.setSemesterrechnungCode(previousSemesterrechnung.getSemesterrechnungCode());
                semesterrechnung.setStipendium(previousSemesterrechnung.getStipendium());
                semesterrechnung.setGratiskinder(previousSemesterrechnung.getGratiskinder());
            }

            SaveOrUpdateSemesterrechnungCommand saveOrUpdateSemesterrechnungCommand = new SaveOrUpdateSemesterrechnungCommand(semesterrechnung, null, null, semesterrechnungenCreated);
            saveOrUpdateSemesterrechnungCommand.setEntityManager(entityManager);
            saveOrUpdateSemesterrechnungCommand.execute();
        }
    }

    public List<Semesterrechnung> getSemesterrechnungenCreated() {
        return semesterrechnungenCreated;
    }
}
