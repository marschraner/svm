package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.Collection;

/**
 * @author Martin Schraner
 */
public class UpdateAnzWochenSemesterrechnungenCommand extends GenericDaoCommand {

    // input
    private Collection<Semesterrechnung> semesterrechnungen;
    private Semester semester;


    public UpdateAnzWochenSemesterrechnungenCommand(Collection<Semesterrechnung> semesterrechnungen, Semester semester) {
        this.semesterrechnungen = semesterrechnungen;
        this.semester = semester;
    }

    @Override
    public void execute() {

        SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao(entityManager);

        for (Semesterrechnung semesterrechnung : semesterrechnungen) {

            if (semesterrechnung.getRechnungsdatumVorrechnung() == null || semesterrechnung.getRechnungsdatumNachrechnung() == null) {

                // Berechnung der Anzahl Wochen
                CalculateAnzWochenCommand calculateAnzWochenCommand = new CalculateAnzWochenCommand(semesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaenger(), semester);
                calculateAnzWochenCommand.execute();

                if (semesterrechnung.getRechnungsdatumVorrechnung() == null) {
                    semesterrechnung.setAnzahlWochenVorrechnung(calculateAnzWochenCommand.getAnzahlWochen());
                }
                if (semesterrechnung.getRechnungsdatumNachrechnung() == null) {
                    semesterrechnung.setAnzahlWochenNachrechnung(calculateAnzWochenCommand.getAnzahlWochen());
                }

                semesterrechnungDao.save(semesterrechnung);
            }
        }
    }

}
