package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckKursBereitsErfasstCommand implements Command {

    // input
    private final Kurs kurs;
    private final Semester semester;
    private final Mitarbeiter mitarbeiter1;
    private final Mitarbeiter mitarbeiter2;
    private final Kurs kursOrigin;
    private final List<Kurs> bereitsErfassteKurse;

    // output
    private boolean bereitsErfasst;

    public CheckKursBereitsErfasstCommand(
            Kurs kurs, Semester semester, Mitarbeiter mitarbeiter1, Mitarbeiter mitarbeiter2,
            Kurs kursOrigin, List<Kurs> bereitsErfassteKurse) {
        this.kurs = kurs;
        this.semester = semester;
        this.mitarbeiter1 = mitarbeiter1;
        this.mitarbeiter2 = mitarbeiter2;
        this.kursOrigin = kursOrigin;
        this.bereitsErfassteKurse = bereitsErfassteKurse;
    }

    @Override
    public void execute() {
        Kurs kursWithSemesterAndLehrkraefte = new Kurs();
        kursWithSemesterAndLehrkraefte.copyAttributesFrom(kurs);
        kursWithSemesterAndLehrkraefte.setSemester(semester);
        kursWithSemesterAndLehrkraefte.addLehrkraft(mitarbeiter1);
        if (mitarbeiter2 != null) {
            kursWithSemesterAndLehrkraefte.addLehrkraft(mitarbeiter2);
        }
        for (Kurs bereitsErfassteKurs : bereitsErfassteKurse) {
            if (!bereitsErfassteKurs.isIdenticalWith(kursOrigin)
                    && bereitsErfassteKurs.isIdenticalWith(kursWithSemesterAndLehrkraefte)) {
                bereitsErfasst = true;
                return;
            }
        }
        bereitsErfasst = false;
    }

    public boolean isBereitsErfasst() {
        return bereitsErfasst;
    }
}
