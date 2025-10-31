package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DetermineNaechstesNochNichtErfasstesSemesterCommand implements Command {

  // input
  private final List<Semester> bereitsErfassteSemesters;

  // output
  private Semester naechstesNochNichtErfasstesSemester;

  public DetermineNaechstesNochNichtErfasstesSemesterCommand(
      List<Semester> bereitsErfassteSemesters) {
    this.bereitsErfassteSemesters = bereitsErfassteSemesters;
  }

  @SuppressWarnings("DuplicatedCode")
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
    String naechstesSchuljahr = schuljahr1 + "/" + schuljahr2;
    Semesterbezeichnung naechsteSemesterbezeichnung;
    if (today.get(Calendar.MONTH) >= Calendar.FEBRUARY
        && today.get(Calendar.MONTH) <= Calendar.MAY) {
      naechsteSemesterbezeichnung = Semesterbezeichnung.ZWEITES_SEMESTER;
    } else {
      naechsteSemesterbezeichnung = Semesterbezeichnung.ERSTES_SEMESTER;
    }
    while (isSemesterBereitsErfasst(naechstesSchuljahr, naechsteSemesterbezeichnung)
        && schuljahr1 < Schuljahre.SCHULJAHR_VALID_MAX) {
      if (naechsteSemesterbezeichnung == Semesterbezeichnung.ERSTES_SEMESTER) {
        naechsteSemesterbezeichnung = Semesterbezeichnung.ZWEITES_SEMESTER;
      } else {
        naechsteSemesterbezeichnung = Semesterbezeichnung.ERSTES_SEMESTER;
        schuljahr1++;
        schuljahr2++;
        naechstesSchuljahr = schuljahr1 + "/" + schuljahr2;
      }
    }
    naechstesNochNichtErfasstesSemester =
        new Semester(
            naechstesSchuljahr, naechsteSemesterbezeichnung, null, null, null, null, null, null);
  }

  private boolean isSemesterBereitsErfasst(
      String naechstesSchuljahr, Semesterbezeichnung naechsteSemesterbezeichnung) {
    for (Semester bereitsErfasstesSemester : bereitsErfassteSemesters) {
      if (bereitsErfasstesSemester.getSchuljahr().equals(naechstesSchuljahr)
          && bereitsErfasstesSemester
              .getSemesterbezeichnung()
              .equals(naechsteSemesterbezeichnung)) {
        return true;
      }
    }
    return false;
  }

  public Semester getNaechstesNochNichtErfasstesSemester() {
    return naechstesNochNichtErfasstesSemester;
  }
}
