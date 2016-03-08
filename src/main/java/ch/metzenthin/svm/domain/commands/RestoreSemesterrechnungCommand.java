package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class RestoreSemesterrechnungCommand extends GenericDaoCommand {

    // input
    private List<Semesterrechnung> semesterrechnungen;
    int indexSemesterrechnungToBeRestored;

    public RestoreSemesterrechnungCommand(List<Semesterrechnung> semesterrechnungen, int indexSemesterrechnungToBeRestored) {
        this.semesterrechnungen = semesterrechnungen;
        this.indexSemesterrechnungToBeRestored = indexSemesterrechnungToBeRestored;
    }

    @Override
    public void execute() {
        SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao(entityManager);
        Semesterrechnung semesterrechnungToBeDeletedLogically = semesterrechnungen.get(indexSemesterrechnungToBeRestored);
        semesterrechnungToBeDeletedLogically.setDeleted(false);
        semesterrechnungDao.save(semesterrechnungToBeDeletedLogically);
        semesterrechnungen.remove(indexSemesterrechnungToBeRestored);
    }
    
}
