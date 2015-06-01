package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Martin Schraner
 */
public class CheckDrittpersonIdentischMitElternteilCommand implements Command {

    private Angehoeriger mutter;
    private Angehoeriger vater;
    private Angehoeriger rechnungsempfaengerDrittperson;

    public static String ERROR_IDENTISCH_MIT_MUTTER = "Rechnungsempfänger Drittperson ist identisch mit Mutter. Setze Mutter als Rechnungsempfängerin.";
    public static String ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_MUTTER = "Rechnungsempfänger Drittperson und Mutter scheinen identisch zu sein. Setze Mutter als Rechnungsempfängerin oder präzisiere Mutter.";
    public static String ERROR_IDENTISCH_MIT_VATER = "Rechnungsempfänger Drittperson ist identisch mit Vater. Setze Vater als Rechnungsempfänger.";
    public static String ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_VATER = "Rechnungsempfänger Drittperson und Vater scheinen identisch zu sein. Setze Vater als Rechnungsempfänger oder präzisiere Vater.";

    // output
    private boolean identical;
    private String errorDrittpersonIdentischMitElternteil;

    public CheckDrittpersonIdentischMitElternteilCommand(Angehoeriger mutter, Angehoeriger vater, Angehoeriger rechnungsempfaengerDrittperson) {
        this.mutter = mutter;
        this.vater = vater;
        this.rechnungsempfaengerDrittperson = rechnungsempfaengerDrittperson;
    }

    @Override
    public void execute() {

        if (rechnungsempfaengerDrittperson == null) {
            identical = false;
            errorDrittpersonIdentischMitElternteil = "";
        }

        else if (mutter.isIdenticalWith(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorDrittpersonIdentischMitElternteil = ERROR_IDENTISCH_MIT_MUTTER;
        }

        else if (mutter.isPartOf(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorDrittpersonIdentischMitElternteil = ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_MUTTER;
        }

        else if (vater.isIdenticalWith(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorDrittpersonIdentischMitElternteil = ERROR_IDENTISCH_MIT_VATER;
        }

        else if (vater.isPartOf(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorDrittpersonIdentischMitElternteil = ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_VATER;
        }

        else {
            identical = false;
            errorDrittpersonIdentischMitElternteil = "";
        }

    }

    public boolean isIdentical() {
        return identical;
    }

    public String getErrorDrittpersonIdentischMitElternteil() {
        return errorDrittpersonIdentischMitElternteil;
    }
}
