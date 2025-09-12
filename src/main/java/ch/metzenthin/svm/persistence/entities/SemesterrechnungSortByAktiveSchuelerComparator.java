package ch.metzenthin.svm.persistence.entities;

import java.util.Comparator;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungSortByAktiveSchuelerComparator
    implements Comparator<Semesterrechnung> {

  private final Semester previousSemester;

  public SemesterrechnungSortByAktiveSchuelerComparator(Semester previousSemester) {
    this.previousSemester = previousSemester;
  }

  @Override
  public int compare(Semesterrechnung semesterrechnung1, Semesterrechnung semesterrechnung2) {
    // Sortierung nach aktiven Schülern
    List<Schueler> aktiveSchueler1 =
        semesterrechnung1.getAktiveSchuelerRechnungsempfaenger(previousSemester);
    Person person1 =
        (aktiveSchueler1.isEmpty()
            ? semesterrechnung1.getRechnungsempfaenger()
            : aktiveSchueler1.get(0));
    List<Schueler> aktiveSchueler2 =
        semesterrechnung2.getAktiveSchuelerRechnungsempfaenger(previousSemester);
    Person person2 =
        (aktiveSchueler2.isEmpty()
            ? semesterrechnung2.getRechnungsempfaenger()
            : aktiveSchueler2.get(0));
    return person1.compareTo(person2);
  }
}
