package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Schuljahre;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
class FindSemesterOneYearBeforeCommand extends GenericDaoCommand {

    // input
    private Semester currentSemester;

    // output
    private Semester semesterOneYearBefore;

    FindSemesterOneYearBeforeCommand(Semester currentSemester) {
        this.currentSemester = currentSemester;
    }

    @Override
    public void execute() {
        if (currentSemester == null) {
            return;
        }
        String schuljahrOneYearBefore = Schuljahre.getPreviousSchuljahr(currentSemester.getSchuljahr());
        FindAllSemestersCommand findAllSemestersCommand = new FindAllSemestersCommand();
        findAllSemestersCommand.setEntityManager(entityManager);
        findAllSemestersCommand.execute();
        List<Semester> semestersAll = findAllSemestersCommand.getSemestersAll();
        FindSemesterForSchuljahrSemesterbezeichnungCommand findSemesterForSchuljahrSemesterbezeichnungCommand = new FindSemesterForSchuljahrSemesterbezeichnungCommand(schuljahrOneYearBefore, currentSemester.getSemesterbezeichnung(), semestersAll);
        findSemesterForSchuljahrSemesterbezeichnungCommand.execute();
        semesterOneYearBefore = findSemesterForSchuljahrSemesterbezeichnungCommand.getSemesterFound();
    }

    Semester getSemesterOneYearBefore() {
        return semesterOneYearBefore;
    }
}
