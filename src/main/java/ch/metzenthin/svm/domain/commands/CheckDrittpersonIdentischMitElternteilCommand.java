package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Martin Schraner
 */
public class CheckDrittpersonIdentischMitElternteilCommand implements Command {

    static final String ERROR_IDENTISCH_MIT_MUTTER = "Rechnungsempfänger Drittperson ist identisch mit Mutter. Setze Mutter als Rechnungsempfängerin.";
    static final String ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_MUTTER = "Rechnungsempfänger Drittperson und Mutter scheinen identisch zu sein. Setze Mutter als Rechnungsempfängerin und/oder korrigiere Mutter.";
    static final String ERROR_IDENTISCH_MIT_VATER = "Rechnungsempfänger Drittperson ist identisch mit Vater. Setze Vater als Rechnungsempfänger.";
    static final String ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_VATER = "Rechnungsempfänger Drittperson und Vater scheinen identisch zu sein. Setze Vater als Rechnungsempfänger und/oder korrigiere Vater.";

    private final Angehoeriger mutter;
    private final Angehoeriger vater;
    private final Angehoeriger rechnungsempfaengerDrittperson;

    // output
    private boolean identical;
    private String errorMessage;

    CheckDrittpersonIdentischMitElternteilCommand(Angehoeriger mutter, Angehoeriger vater, Angehoeriger rechnungsempfaengerDrittperson) {
        this.mutter = mutter;
        this.vater = vater;
        this.rechnungsempfaengerDrittperson = rechnungsempfaengerDrittperson;
    }

    @Override
    public void execute() {

        if (rechnungsempfaengerDrittperson == null || rechnungsempfaengerDrittperson.isEmpty()) {
            identical = false;
            errorMessage = "";
        }

        else if (mutter != null && mutter.getVorname() != null && !mutter.getVorname().trim().isEmpty() && mutter.isIdenticalWith(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorMessage = ERROR_IDENTISCH_MIT_MUTTER;
        }

        else if (mutter != null && mutter.getVorname() != null && !mutter.getVorname().trim().isEmpty() && mutter.isPartOf(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorMessage = ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_MUTTER;
        }

        else if (vater != null && vater.getVorname() != null && !vater.getVorname().trim().isEmpty() && vater.isIdenticalWith(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorMessage = ERROR_IDENTISCH_MIT_VATER;
        }

        else if (vater != null && vater.getVorname() != null && !vater.getVorname().trim().isEmpty() && vater.isPartOf(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorMessage = ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_VATER;
        }

        else {
            identical = false;
            errorMessage = "";
        }

    }

    boolean isIdentical() {
        return identical;
    }

    String getErrorMessage() {
        return errorMessage;
    }
}
