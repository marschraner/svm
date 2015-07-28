package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Schuljahre;
import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DetermineNaechstesNochNichtErfasstesSemesterCommand implements Command {

    // input
    private List<Semester> bereitsErfassteSemesters;

    // output
    private Semester naechstesNochNichtErfasstesSemester;

    public DetermineNaechstesNochNichtErfasstesSemesterCommand(List<Semester> bereitsErfassteSemesters) {
        this.bereitsErfassteSemesters = bereitsErfassteSemesters;
    }

    @Override
    public void execute() {
        Calendar today = new GregorianCalendar();
        int schuljahr1;
        if (today.get(Calendar.MONTH) <= Calendar.MAY) {
            schuljahr1 = today.get(Calendar.YEAR) - 1;
        } else {
            schuljahr1 = today.get(Calendar.YEAR);
        }
        int schuljahr2 = schuljahr1 + 1;
        String naechtesSchuljahr = schuljahr1 + "/" + schuljahr2;
        Semesterbezeichnung naechsteSemesterbezeichnung;
        if (today.get(Calendar.MONTH) >= Calendar.FEBRUARY || today.get(Calendar.MONTH) <= Calendar.MAY) {
            naechsteSemesterbezeichnung = Semesterbezeichnung.ZWEITES_SEMESTER;
        } else {
            naechsteSemesterbezeichnung = Semesterbezeichnung.ERSTES_SEMESTER;
        }
        while (isSemesterBereitsErfasst(naechtesSchuljahr, naechsteSemesterbezeichnung) && schuljahr1 < Schuljahre.SCHULJAHR_VALID_MAX) {
            schuljahr1++;
            schuljahr2++;
            naechtesSchuljahr = schuljahr1 + "/" + schuljahr2;
            if (naechsteSemesterbezeichnung == Semesterbezeichnung.ERSTES_SEMESTER) {
                naechsteSemesterbezeichnung = Semesterbezeichnung.ZWEITES_SEMESTER;
            } else {
                naechsteSemesterbezeichnung = Semesterbezeichnung.ERSTES_SEMESTER;
            }
        }
        naechstesNochNichtErfasstesSemester = new Semester(naechtesSchuljahr, naechsteSemesterbezeichnung, null, null, 0);
    }

    private boolean isSemesterBereitsErfasst(String naechtesSchuljahr, Semesterbezeichnung naechsteSemesterbezeichnung) {
        for (Semester bereitsErfasstesSemester : bereitsErfassteSemesters) {
            if (bereitsErfasstesSemester.getSchuljahr().equals(naechtesSchuljahr) && bereitsErfasstesSemester.getSemesterbezeichnung().equals(naechsteSemesterbezeichnung)) {
                return true;
            }
        }
        return false;
    }

    public Semester getNaechstesNochNichtErfasstesSemester() {
        return naechstesNochNichtErfasstesSemester;
    }
}