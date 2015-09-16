package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Schuljahre;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindNextSemesterCommand extends GenericDaoCommand {

    // input
    private Semester currentSemester;

    // output
    private Semester nextSemester;

    public FindNextSemesterCommand(Semester currentSemester) {
        this.currentSemester = currentSemester;
    }

    @Override
    public void execute() {
        String schuljahrNextSemester;
        Semesterbezeichnung semesterbezeichnungPreviousSemester;
        if (currentSemester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
            schuljahrNextSemester = currentSemester.getSchuljahr();
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ZWEITES_SEMESTER;

        } else {
            schuljahrNextSemester = Schuljahre.getNextSchuljahr(currentSemester.getSchuljahr());
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ERSTES_SEMESTER;
        }
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        findAllSemestersCommand.setEntityManager(entityManager);
        findAllSemestersCommand.execute();
        List<Semester> semestersAll = findAllSemestersCommand.getSemestersAll();
        FindSemesterForSchuljahrSemesterbezeichnungCommand findSemesterForSchuljahrSemesterbezeichnungCommand = new FindSemesterForSchuljahrSemesterbezeichnungCommand(schuljahrNextSemester, semesterbezeichnungPreviousSemester, semestersAll);
        findSemesterForSchuljahrSemesterbezeichnungCommand.execute();
        nextSemester = findSemesterForSchuljahrSemesterbezeichnungCommand.getSemesterFound();
    }

    public Semester getNextSemester() {
        return nextSemester;
    }
}