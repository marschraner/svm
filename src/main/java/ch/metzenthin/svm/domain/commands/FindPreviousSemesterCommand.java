package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Schuljahre;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindPreviousSemesterCommand extends GenericDaoCommand {

    // input
    private Semester currentSemester;

    // output
    private Semester previousSemester;

    public FindPreviousSemesterCommand(Semester currentSemester) {
        this.currentSemester = currentSemester;
    }

    @Override
    public void execute() {
        String schuljahrPreviousSemester;
        Semesterbezeichnung semesterbezeichnungPreviousSemester;
        if (currentSemester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
            schuljahrPreviousSemester = Schuljahre.getPreviousSchuljahr(currentSemester.getSchuljahr());
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ZWEITES_SEMESTER;

        } else {
            schuljahrPreviousSemester = currentSemester.getSchuljahr();
            semesterbezeichnungPreviousSemester = Semesterbezeichnung.ERSTES_SEMESTER;
        }
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        findAllSemestersCommand.setEntityManager(entityManager);
        findAllSemestersCommand.execute();
        List<Semester> semestersAll = findAllSemestersCommand.getSemestersAll();
        FindSemesterForSchuljahrSemesterbezeichnungCommand findSemesterForSchuljahrSemesterbezeichnungCommand = new FindSemesterForSchuljahrSemesterbezeichnungCommand(schuljahrPreviousSemester, semesterbezeichnungPreviousSemester, semestersAll);
        findSemesterForSchuljahrSemesterbezeichnungCommand.execute();
        previousSemester = findSemesterForSchuljahrSemesterbezeichnungCommand.getSemesterFound();
    }

    public Semester getPreviousSemester() {
        return previousSemester;
    }
}
