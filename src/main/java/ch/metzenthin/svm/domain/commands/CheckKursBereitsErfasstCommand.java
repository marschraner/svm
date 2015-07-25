package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurs;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckKursBereitsErfasstCommand implements Command {

    // input
    private Kurs kurs;
    private Kurs kursOrigin;
    private List<Kurs> bereitsErfassteKurse;

    // output
    private boolean bereitsErfasst;

    public CheckKursBereitsErfasstCommand(Kurs kurs, Kurs kursOrigin, List<Kurs> bereitsErfassteKurse) {
        this.kurs = kurs;
        this.kursOrigin = kursOrigin;
        this.bereitsErfassteKurse = bereitsErfassteKurse;
    }

    @Override
    public void execute() {
        for (Kurs bereitsErfassteKurs : bereitsErfassteKurse) {
            if (!bereitsErfassteKurs.isIdenticalWith(kursOrigin) && bereitsErfassteKurs.isIdenticalWith(kurs)) {
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
