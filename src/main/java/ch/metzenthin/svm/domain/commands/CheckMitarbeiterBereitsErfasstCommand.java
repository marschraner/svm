package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckMitarbeiterBereitsErfasstCommand implements Command {

    // input
    private final Mitarbeiter mitarbeiter;
    private final Mitarbeiter mitarbeiterOrigin;
    private final List<Mitarbeiter> bereitsErfassteLehrkraefte;

    // output
    private boolean bereitsErfasst;

    public CheckMitarbeiterBereitsErfasstCommand(Mitarbeiter mitarbeiter, Mitarbeiter mitarbeiterOrigin, List<Mitarbeiter> bereitsErfassteLehrkraefte) {
        this.mitarbeiter = mitarbeiter;
        this.mitarbeiterOrigin = mitarbeiterOrigin;
        this.bereitsErfassteLehrkraefte = bereitsErfassteLehrkraefte;
    }

    @Override
    public void execute() {
        for (Mitarbeiter bereitsErfassteMitarbeiter : bereitsErfassteLehrkraefte) {
            if (!bereitsErfassteMitarbeiter.isIdenticalWith(mitarbeiterOrigin) && bereitsErfassteMitarbeiter.isIdenticalWith(mitarbeiter)) {
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
