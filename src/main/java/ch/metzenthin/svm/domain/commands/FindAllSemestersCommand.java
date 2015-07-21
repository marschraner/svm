package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllSemestersCommand extends GenericDaoCommand {

    // output
    private List<Semester> semesterAll;

    @Override
    public void execute() {

        SemesterDao semesterDao = new SemesterDao(entityManager);
        semesterAll = semesterDao.findAll();
    }

    public List<Semester> getSemesterAll() {
        return semesterAll;
    }

}
