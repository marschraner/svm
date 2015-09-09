package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.Collection;

/**
 * @author Martin Schraner
 */
public class UpdateAnzWochenSemesterrechnungenCommand extends GenericDaoCommand {

    // input
    private Collection<Semesterrechnung> semesterrechnungen;
    private int anzahlSchulwochen;


    public UpdateAnzWochenSemesterrechnungenCommand(Collection<Semesterrechnung> semesterrechnungen, int anzahlSchulwochen) {
        this.semesterrechnungen = semesterrechnungen;
        this.anzahlSchulwochen = anzahlSchulwochen;
    }

    @Override
    public void execute() {

        SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao(entityManager);

        for (Semesterrechnung semesterrechnung : semesterrechnungen) {

            boolean dataChanged = false;
            if (semesterrechnung.getRechnungsdatumVorrechnung() == null) {
                semesterrechnung.setAnzahlWochenVorrechnung(anzahlSchulwochen);
                dataChanged = true;
            }
            if (semesterrechnung.getRechnungsdatumNachrechnung() == null) {
                semesterrechnung.setAnzahlWochenNachrechnung(anzahlSchulwochen);
                dataChanged = true;
            }
            if (dataChanged) {
                semesterrechnungDao.save(semesterrechnung);
            }
        }
    }

}
