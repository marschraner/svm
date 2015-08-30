package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindSemesterrechnungenSemesterCommand extends GenericDaoCommand {

    // input
    private Semester semester;

    // output
    private List<Semesterrechnung> semesterrechnungenFound;

    public FindSemesterrechnungenSemesterCommand(Semester semester) {
        this.semester = semester;
    }

    @Override
    public void execute() {
        SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao(entityManager);
        semesterrechnungenFound = semesterrechnungDao.findSemesterrechnungenSemester(semester);
    }

    public List<Semesterrechnung> getSemesterrechnungenFound() {
        return semesterrechnungenFound;
    }
}
