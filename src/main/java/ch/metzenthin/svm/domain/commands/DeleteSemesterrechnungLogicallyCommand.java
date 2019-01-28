package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterrechnungLogicallyCommand extends GenericDaoCommand {

    private final SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao();

    // input
    private List<Semesterrechnung> semesterrechnungen;
    private int indexSemesterrechnungToBeDeletedLogically;

    public DeleteSemesterrechnungLogicallyCommand(List<Semesterrechnung> semesterrechnungen, int indexSemesterrechnungToBeDeletedLogically) {
        this.semesterrechnungen = semesterrechnungen;
        this.indexSemesterrechnungToBeDeletedLogically = indexSemesterrechnungToBeDeletedLogically;
    }

    @Override
    public void execute() {
        Semesterrechnung semesterrechnungToBeDeletedLogically = semesterrechnungen.get(indexSemesterrechnungToBeDeletedLogically);
        semesterrechnungToBeDeletedLogically.setDeleted(true);
        semesterrechnungDao.save(semesterrechnungToBeDeletedLogically);
        semesterrechnungen.remove(indexSemesterrechnungToBeDeletedLogically);
    }
    
}
