package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckLehrkraftBereitsErfasstCommand implements Command {

    // input
    private Lehrkraft lehrkraft;
    private Lehrkraft lehrkraftOrigin;
    private List<Lehrkraft> bereitsErfassteLehrkraefte;

    // output
    private boolean bereitsErfasst;

    public CheckLehrkraftBereitsErfasstCommand(Lehrkraft lehrkraft, Lehrkraft lehrkraftOrigin, List<Lehrkraft> bereitsErfassteLehrkraefte) {
        this.lehrkraft = lehrkraft;
        this.lehrkraftOrigin = lehrkraftOrigin;
        this.bereitsErfassteLehrkraefte = bereitsErfassteLehrkraefte;
    }

    @Override
    public void execute() {
        for (Lehrkraft bereitsErfassteLehrkraft : bereitsErfassteLehrkraefte) {
            if (!bereitsErfassteLehrkraft.isIdenticalWith(lehrkraftOrigin) && bereitsErfassteLehrkraft.isIdenticalWith(lehrkraft)) {
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
