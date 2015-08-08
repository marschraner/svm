package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllSemestersCommand extends GenericDaoCommand {

    // output
    private List<Semester> semestersAll;

    @Override
    public void execute() {

        SemesterDao semesterDao = new SemesterDao(entityManager);
        semestersAll = semesterDao.findAll();
    }

    public List<Semester> getSemestersAll() {
        return semestersAll;
    }

}
