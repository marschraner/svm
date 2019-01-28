package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindKurseSemesterCommand extends GenericDaoCommand {

    private final KursDao kursDao = new KursDao();

    // input
    private Semester semester;

    // output
    private List<Kurs> kurseFound;

    public FindKurseSemesterCommand(Semester semester) {
        this.semester = semester;
    }

    @Override
    public void execute() {
        kurseFound = kursDao.findKurseSemester(semester);
    }

    public List<Kurs> getKurseFound() {
        return kurseFound;
    }
}
