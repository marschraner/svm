package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class RestoreSemesterrechnungCommand extends GenericDaoCommand {

    private final SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao();

    // input
    private List<Semesterrechnung> semesterrechnungen;
    private int indexSemesterrechnungToBeRestored;

    public RestoreSemesterrechnungCommand(List<Semesterrechnung> semesterrechnungen, int indexSemesterrechnungToBeRestored) {
        this.semesterrechnungen = semesterrechnungen;
        this.indexSemesterrechnungToBeRestored = indexSemesterrechnungToBeRestored;
    }

    @Override
    public void execute() {
        Semesterrechnung semesterrechnungToBeDeletedLogically = semesterrechnungen.get(indexSemesterrechnungToBeRestored);
        semesterrechnungToBeDeletedLogically.setDeleted(false);
        semesterrechnungDao.save(semesterrechnungToBeDeletedLogically);
        semesterrechnungen.remove(indexSemesterrechnungToBeRestored);
    }
    
}
