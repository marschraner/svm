package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurstyp;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckKurstypBezeichnungBereitsInVerwendungCommand implements Command {

    // input
    private Kurstyp kurstyp;
    private Kurstyp kurstypOrigin;
    private List<Kurstyp> bereitsErfassteKurstypen;

    // output
    private boolean bereitsInVerwendung;

    public CheckKurstypBezeichnungBereitsInVerwendungCommand(Kurstyp kurstyp, Kurstyp kurstypOrigin, List<Kurstyp> bereitsErfassteKurstypen) {
        this.kurstyp = kurstyp;
        this.kurstypOrigin = kurstypOrigin;
        this.bereitsErfassteKurstypen = bereitsErfassteKurstypen;
    }

    @Override
    public void execute() {
        for (Kurstyp bereitsErfassterKurstyp : bereitsErfassteKurstypen) {
            if (!bereitsErfassterKurstyp.isIdenticalWith(kurstypOrigin) && bereitsErfassterKurstyp.getBezeichnung().equals(kurstyp.getBezeichnung())) {
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
