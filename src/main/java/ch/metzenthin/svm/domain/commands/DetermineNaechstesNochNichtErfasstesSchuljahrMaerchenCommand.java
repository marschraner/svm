package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DetermineNaechstesNochNichtErfasstesSchuljahrMaerchenCommand implements Command {

    // input
    private final List<Maerchen> bereitsErfassteMaerchens;

    // output
    private String naechstesNochNichtErfasstesSchuljahrMaerchen;

    public DetermineNaechstesNochNichtErfasstesSchuljahrMaerchenCommand(List<Maerchen> bereitsErfassteMaerchens) {
        this.bereitsErfassteMaerchens = bereitsErfassteMaerchens;
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
        while (isMaerchenBereitsErfasst(naechstesSchuljahr) && schuljahr1 < Schuljahre.SCHULJAHR_VALID_MAX) {
            schuljahr1++;
            schuljahr2++;
            naechstesSchuljahr = schuljahr1 + "/" + schuljahr2;
        }
        naechstesNochNichtErfasstesSchuljahrMaerchen = naechstesSchuljahr;
    }

    private boolean isMaerchenBereitsErfasst(String naechstesSchuljahr) {
        for (Maerchen bereitsErfasstesMaerchen : bereitsErfassteMaerchens) {
            if (bereitsErfasstesMaerchen.getSchuljahr().equals(naechstesSchuljahr)) {
                return true;
            }
        }
        return false;
    }

    public String getNaechstesNochNichtErfasstesSchuljahrMaerchen() {
        return naechstesNochNichtErfasstesSchuljahrMaerchen;
    }
}
