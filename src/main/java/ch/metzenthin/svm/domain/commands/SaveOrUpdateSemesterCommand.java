package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSemesterCommand extends GenericDaoCommand {

    // input
    private Semester semester;
    private Semester semesterOrigin;
    private List<Semester> bereitsErfassteSemester;


    public SaveOrUpdateSemesterCommand(Semester semester, Semester semesterOrigin, List<Semester> bereitsErfassteSemester) {
        this.semester = semester;
        this.semesterOrigin = semesterOrigin;
        this.bereitsErfassteSemester = bereitsErfassteSemester;
    }

    @Override
    public void execute() {
        SemesterDao semesterDao = new SemesterDao(entityManager);
        if (semesterOrigin != null) {
            // Update von semesterOrigin mit Werten von semester
            semesterOrigin.copyAttributesFrom(semester);
            semesterDao.save(semesterOrigin);
        } else {
            Semester semesterSaved = semesterDao.save(semester);
            bereitsErfassteSemester.add(semesterSaved);
        }
        Collections.sort(bereitsErfassteSemester);
    }

}
