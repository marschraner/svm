package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Martin Schraner
 */
public class CheckDrittpersonIdentischMitElternteilCommand extends GenericDaoCommand {

    private Angehoeriger mutter;
    private Angehoeriger vater;
    private Angehoeriger rechnungsempfaengerDrittperson;

    // output
    private boolean identical;
    private String resultMessage;

    public CheckDrittpersonIdentischMitElternteilCommand(Angehoeriger mutter, Angehoeriger vater, Angehoeriger rechnungsempfaengerDrittperson) {
        this.mutter = mutter;
        this.vater = vater;
        this.rechnungsempfaengerDrittperson = rechnungsempfaengerDrittperson;
    }

    @Override
    public void execute() {

        if (rechnungsempfaengerDrittperson == null) {
            identical = false;
            resultMessage = "";
        }

        else if (rechnungsempfaengerDrittperson.isIdenticalWith(mutter)) {
            identical = true;
            resultMessage = "FEHLER: Rechnungsempfänger Drittperson ist identisch mit Mutter. Setze Mutter als Rechnungsempfängerin.";
        }

        else if (rechnungsempfaengerDrittperson.isPartOf(mutter) || mutter.isPartOf(rechnungsempfaengerDrittperson)) {
            identical = true;
            resultMessage = "FEHLER: Rechnungsempfänger Drittperson und Mutter scheinen identisch zu sein. Setze Mutter als Rechnungsempfängerin.";
        }

        else if (rechnungsempfaengerDrittperson.isIdenticalWith(vater)) {
            identical = true;
            resultMessage = "FEHLER: Rechnungsempfänger Drittperson ist identisch mit Vater. Setze Vater als Rechnungsempfänger.";
        }

        else if (rechnungsempfaengerDrittperson.isPartOf(vater) || vater.isPartOf(rechnungsempfaengerDrittperson)) {
            identical = true;
            resultMessage = "FEHLER: Rechnungsempfänger Drittperson und Vater scheinen identisch zu sein. Setze Vater als Rechnungsempfänger.";
        }

        else {
            identical = false;
            resultMessage = "";
        }

    }

    public boolean isIdentical() {
        return identical;
    }

    public String getResultMessage() {
        return resultMessage;
    }
}
