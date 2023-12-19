package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Dispensation;

import java.util.List;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.checkIfTwoPeriodsOverlap;

/**
 * @author Martin Schraner
 */
public class CheckDispensationUeberlapptAndereDispensationenCommand implements Command {

    // input
    private final Dispensation dispensation;
    private final Dispensation dispensationOrigin;
    private final List<Dispensation> bereitsErfassteDispensationen;

    // output
    private boolean ueberlappt;

    public CheckDispensationUeberlapptAndereDispensationenCommand(Dispensation dispensation, Dispensation dispensationOrigin, List<Dispensation> bereitsErfassteDispensationen) {
        this.dispensation = dispensation;
        this.dispensationOrigin = dispensationOrigin;
        this.bereitsErfassteDispensationen = bereitsErfassteDispensationen;
    }

    @Override
    public void execute() {
        for (Dispensation bereitsErfassteDispensation : bereitsErfassteDispensationen) {
            if (!bereitsErfassteDispensation.isIdenticalWith(dispensationOrigin) && checkIfTwoPeriodsOverlap(bereitsErfassteDispensation.getDispensationsbeginn(), bereitsErfassteDispensation.getDispensationsende(), dispensation.getDispensationsbeginn(), dispensation.getDispensationsende())) {
                ueberlappt = true;
                return;
            }
        }
        ueberlappt = false;
    }

    public boolean isUeberlappt() {
        return ueberlappt;
    }
}
