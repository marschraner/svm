package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public class ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand implements Command {

    private final DB db = DBFactory.getInstance();

    private final SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao();

    // input
    private List<Semesterrechnung> semesterrechnungen;
    private Rechnungstyp rechnungstyp;
    private Calendar rechnungsdatum;

    // output
    private Set<Semesterrechnung> updatedSemesterrechnungen = new HashSet<>();

    public ReplaceRechnungsdatumAndUpdateSemesterrechnungenCommand(List<Semesterrechnung> semesterrechnungen, Rechnungstyp rechnungstyp, Calendar rechnungsdatum) {
        this.semesterrechnungen = semesterrechnungen;
        this.rechnungstyp = rechnungstyp;
        this.rechnungsdatum = rechnungsdatum;
    }

    @Override
    public void execute() {

        for (Semesterrechnung semesterrechnung : semesterrechnungen) {

            // 5. Semesterrechnung mit neuem Rechnungsdatum aktualisieren
            switch (rechnungstyp) {

                case VORRECHNUNG:
                    semesterrechnung.setRechnungsdatumVorrechnung(rechnungsdatum);
                    break;

                case NACHRECHNUNG:
                    semesterrechnung.setRechnungsdatumNachrechnung(rechnungsdatum);

                    // Beträge der Vorrechnung kopieren (analog
                    // SemesterRechnungBearbeitenModel.copyZahlungenVorrechnungToZahlungenNachrechnung()).
                    // Das Kopieren der Zahlungen geschieht nur, wenn die entsprechende Zahlung in der Nachrechnung noch
                    // nicht gesetzt ist.
                    if (semesterrechnung.getBetragZahlung1Nachrechnung() == null
                            && semesterrechnung.getBetragZahlung1Vorrechnung() != null) {
                        semesterrechnung.setBetragZahlung1Nachrechnung(semesterrechnung.getBetragZahlung1Vorrechnung());
                    }
                    if (semesterrechnung.getDatumZahlung1Nachrechnung() == null
                            && semesterrechnung.getDatumZahlung1Vorrechnung() != null) {
                        semesterrechnung.setDatumZahlung1Nachrechnung(semesterrechnung.getDatumZahlung1Vorrechnung());
                    }
                    if (semesterrechnung.getBetragZahlung2Nachrechnung() == null
                            && semesterrechnung.getBetragZahlung2Vorrechnung() != null) {
                        semesterrechnung.setBetragZahlung2Nachrechnung(semesterrechnung.getBetragZahlung2Vorrechnung());
                    }
                    if (semesterrechnung.getDatumZahlung2Nachrechnung() == null
                            && semesterrechnung.getDatumZahlung2Vorrechnung() != null) {
                        semesterrechnung.setDatumZahlung2Nachrechnung(semesterrechnung.getDatumZahlung2Vorrechnung());
                    }
                    if (semesterrechnung.getBetragZahlung3Nachrechnung() == null
                            && semesterrechnung.getBetragZahlung3Vorrechnung() != null) {
                        semesterrechnung.setBetragZahlung3Nachrechnung(semesterrechnung.getBetragZahlung3Vorrechnung());
                    }
                    if (semesterrechnung.getDatumZahlung3Nachrechnung() == null
                            && semesterrechnung.getDatumZahlung3Vorrechnung() != null) {
                        semesterrechnung.setDatumZahlung3Nachrechnung(semesterrechnung.getDatumZahlung3Vorrechnung());
                    }
                    break;
            }

            // Ohne merge() tritt ab der zweiten Iteration beim Aufruf von semesterrechnungDao.save()
            // folgende Exception auf (Stand 13.04.2019; Grund unklar, evtl. Hibernate-Bug?):
            // javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: detached entity passed to persist: ch.metzenthin.svm.persistence.entities.Angehoeriger
            semesterrechnung = db.getCurrentEntityManager().merge(semesterrechnung);

            Semesterrechnung updatedSemesterrechnung = semesterrechnungDao.save(semesterrechnung);
            updatedSemesterrechnungen.add(updatedSemesterrechnung);
        }
    }

    public Set<Semesterrechnung> getUpdatedSemesterrechnungen() {
        return updatedSemesterrechnungen;
    }
}
