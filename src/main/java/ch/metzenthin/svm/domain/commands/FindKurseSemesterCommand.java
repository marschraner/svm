package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindKurseSemesterCommand extends GenericDaoCommand {

    // input
    private Semester semester;

    // output
    private List<Kurs> kurseFound;

    public FindKurseSemesterCommand(Semester semester) {
        this.semester = semester;
    }

    @Override
    public void execute() {
        KursDao kursDao = new KursDao(entityManager);
        kurseFound = kursDao.findKurseSemester(semester);
    }

    public List<Kurs> getKurseFound() {
        return kurseFound;
    }
}
