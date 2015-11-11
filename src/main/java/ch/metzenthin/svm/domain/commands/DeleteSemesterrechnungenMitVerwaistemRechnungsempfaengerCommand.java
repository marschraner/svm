package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand extends GenericDaoCommand {

    // input
    private List<Semesterrechnung> semesterrechnungen;

    public DeleteSemesterrechnungenMitVerwaistemRechnungsempfaengerCommand(List<Semesterrechnung> semesterrechnungen) {
        this.semesterrechnungen = semesterrechnungen;
    }

    @Override
    public void execute() {

        SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao(entityManager);
        Iterator<Semesterrechnung> it = semesterrechnungen.iterator();
        while (it.hasNext()) {
            Semesterrechnung semesterrechnungToBeChecked = it.next();
            Angehoeriger rechnungsempfaenger = semesterrechnungToBeChecked.getRechnungsempfaenger();
            if ((rechnungsempfaenger.getSchuelerRechnungsempfaenger() == null || rechnungsempfaenger.getSchuelerRechnungsempfaenger().isEmpty())
                    // Rechnungsempfänger von früheren (bezahlten) Rechnungen sind inzwischen möglicherweise verwaist (falls Rechnungsempfänger gewechselt hatte)
                    && semesterrechnungToBeChecked.getRechnungsdatumVorrechnung() == null
                    && semesterrechnungToBeChecked.getRechnungsdatumNachrechnung() == null) {
                it.remove();
                semesterrechnungDao.remove(semesterrechnungToBeChecked);
                entityManager.flush();

                // Möglicherweise ist Rechnungsempfänger verwaist und kann gelöscht werden
                CheckIfAngehoerigerVerwaistAndDeleteCommand checkIfAngehoerigerVerwaistAndDeleteCommand = new CheckIfAngehoerigerVerwaistAndDeleteCommand(rechnungsempfaenger);
                checkIfAngehoerigerVerwaistAndDeleteCommand.setEntityManager(entityManager);
                checkIfAngehoerigerVerwaistAndDeleteCommand.execute();
            }
        }

    }
}
