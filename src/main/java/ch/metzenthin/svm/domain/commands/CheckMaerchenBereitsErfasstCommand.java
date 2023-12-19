package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckMaerchenBereitsErfasstCommand implements Command {

    // input
    private final Maerchen maerchen;
    private final Maerchen maerchenOrigin;
    private final List<Maerchen> bereitsErfassteMaerchens;

    // output
    private boolean bereitsErfasst;

    public CheckMaerchenBereitsErfasstCommand(Maerchen maerchen, Maerchen maerchenOrigin, List<Maerchen> bereitsErfassteMaerchens) {
        this.maerchen = maerchen;
        this.maerchenOrigin = maerchenOrigin;
        this.bereitsErfassteMaerchens = bereitsErfassteMaerchens;
    }

    @Override
    public void execute() {
        for (Maerchen bereitsErfasstesMaerchen : bereitsErfassteMaerchens) {
            if (!bereitsErfasstesMaerchen.isIdenticalWith(maerchenOrigin) && bereitsErfasstesMaerchen.getSchuljahr().equals(maerchen.getSchuljahr())) {
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
