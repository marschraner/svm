package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand extends GenericDaoCommand {

    // input
    private List<Semesterrechnung> semesterrechnungen;
    private Rechnungstyp rechnungstyp;
    private Calendar rechnungsdatum;

    public ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand(List<Semesterrechnung> semesterrechnungen, Rechnungstyp rechnungstyp, Calendar rechnungsdatum) {
        this.semesterrechnungen = semesterrechnungen;
        this.rechnungstyp = rechnungstyp;
        this.rechnungsdatum = rechnungsdatum;
    }

    @Override
    public void execute() {
        
        SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao(entityManager);

        for (Semesterrechnung semesterrechnung : semesterrechnungen) {

            // 5. Semesterrechnung mit neuem Rechnungsdatum aktualisieren
            switch (rechnungstyp) {
                case VORRECHNUNG:
                    semesterrechnung.setRechnungsdatumVorrechnung(rechnungsdatum);
                    break;
                case NACHRECHNUNG:
                    semesterrechnung.setRechnungsdatumNachrechnung(rechnungsdatum);
                    break;
            }
            semesterrechnungDao.save(semesterrechnung);

        }
    }
    
}
