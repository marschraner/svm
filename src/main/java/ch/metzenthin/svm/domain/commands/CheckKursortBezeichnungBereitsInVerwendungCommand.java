package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kursort;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckKursortBezeichnungBereitsInVerwendungCommand implements Command {

    // input
    private Kursort kursort;
    private Kursort kursortOrigin;
    private List<Kursort> bereitsErfassteKursorte;

    // output
    private boolean bereitsInVerwendung;

    public CheckKursortBezeichnungBereitsInVerwendungCommand(Kursort kursort, Kursort kursortOrigin, List<Kursort> bereitsErfassteKursorte) {
        this.kursort = kursort;
        this.kursortOrigin = kursortOrigin;
        this.bereitsErfassteKursorte = bereitsErfassteKursorte;
    }

    @Override
    public void execute() {
        for (Kursort bereitsErfassterKursort : bereitsErfassteKursorte) {
            if (!bereitsErfassterKursort.isIdenticalWith(kursortOrigin) && bereitsErfassterKursort.getBezeichnung().equals(kursort.getBezeichnung())) {
                bereitsInVerwendung = true;
                return;
            }
        }
        bereitsInVerwendung = false;
    }

    public boolean isBereitsInVerwendung() {
        return bereitsInVerwendung;
    }
}
