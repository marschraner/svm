package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.checkIfTwoPeriodsOverlap;

/**
 * @author Martin Schraner
 */
public class CheckSemesterUeberlapptAndereSemesterCommand implements Command {

    // input
    private final Semester semester;
    private final Semester semesterOrigin;
    private final List<Semester> bereitsErfassteSemester;

    // output
    private boolean ueberlappt;

    public CheckSemesterUeberlapptAndereSemesterCommand(Semester semester, Semester semesterOrigin, List<Semester> bereitsErfassteSemester) {
        this.semester = semester;
        this.semesterOrigin = semesterOrigin;
        this.bereitsErfassteSemester = bereitsErfassteSemester;
    }

    @Override
    public void execute() {
        for (Semester bereitsErfasstesSemester : bereitsErfassteSemester) {
            if (!bereitsErfasstesSemester.isIdenticalWith(semesterOrigin) && checkIfTwoPeriodsOverlap(bereitsErfasstesSemester.getSemesterbeginn(), bereitsErfasstesSemester.getSemesterende(), semester.getSemesterbeginn(), semester.getSemesterende())) {
                ueberlappt = true;
                return;
            }
        }
        ueberlappt = false;
    }

    public boolean isUeberlappt() {
        return ueberlappt;
    }
}
