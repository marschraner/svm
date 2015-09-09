package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterrechnungCommand extends GenericDaoCommand {

    // input
    private List<Semesterrechnung> semesterrechnungen;
    int indexSemesterrechnungToBeDeleted;

    public DeleteSemesterrechnungCommand(List<Semesterrechnung> semesterrechnungen, int indexSemesterrechnungToBeDeleted) {
        this.semesterrechnungen = semesterrechnungen;
        this.indexSemesterrechnungToBeDeleted = indexSemesterrechnungToBeDeleted;
    }

    @Override
    public void execute() {
        SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao(entityManager);
        Semesterrechnung semesterrechnungToBeDeleted = semesterrechnungen.get(indexSemesterrechnungToBeDeleted);
        semesterrechnungDao.remove(semesterrechnungToBeDeleted);
        semesterrechnungen.remove(indexSemesterrechnungToBeDeleted);
    }
    
}
