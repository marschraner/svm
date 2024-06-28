package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindNextSemesterCommand implements Command {

    // input
    private final Semester currentSemester;

    // output
    private Semester nextSemester;

    public FindNextSemesterCommand(Semester currentSemester) {
        this.currentSemester = currentSemester;
    }

    @SuppressWarnings("ExtractMethodRecommender")
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
