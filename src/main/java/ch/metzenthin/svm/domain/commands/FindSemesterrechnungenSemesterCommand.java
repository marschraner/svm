package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindSemesterrechnungenSemesterCommand implements Command {

    private final SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao();

    // input
    private Semester semester;

    // output
    private List<Semesterrechnung> semesterrechnungenFound;

    FindSemesterrechnungenSemesterCommand(Semester semester) {
        this.semester = semester;
    }

    @Override
    public void execute() {
        semesterrechnungenFound = semesterrechnungDao.findSemesterrechnungenSemester(semester);
    }

    List<Semesterrechnung> getSemesterrechnungenFound() {
        return semesterrechnungenFound;
    }
}
