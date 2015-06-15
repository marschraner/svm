package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.AngehoerigerModel;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Martin Schraner
 */
public class CheckDrittpersonIdentischMitElternteilCommand implements Command {

    private Angehoeriger mutter;
    private Angehoeriger vater;
    private Angehoeriger rechnungsempfaengerDrittperson;
    private boolean isDrittpersonRechungsempfaenger;

    public static final String ERROR_IDENTISCH_MIT_MUTTER = "Rechnungsempfänger Drittperson ist identisch mit Mutter. Setze Mutter als Rechnungsempfängerin.";
    public static final String ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_MUTTER = "Rechnungsempfänger Drittperson und Mutter scheinen identisch zu sein. Setze Mutter als Rechnungsempfängerin oder präzisiere Mutter.";
    public static final String ERROR_IDENTISCH_MIT_VATER = "Rechnungsempfänger Drittperson ist identisch mit Vater. Setze Vater als Rechnungsempfänger.";
    public static final String ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_VATER = "Rechnungsempfänger Drittperson und Vater scheinen identisch zu sein. Setze Vater als Rechnungsempfänger oder präzisiere Vater.";

    // output
    private boolean identical;
    private String errorDrittpersonIdentischMitElternteil;

    public CheckDrittpersonIdentischMitElternteilCommand(AngehoerigerModel mutterModel, AngehoerigerModel vaterModel, AngehoerigerModel rechnungsempfaengerDrittpersonModel) {
        if (mutterModel.getAngehoeriger() != null && !mutterModel.getAngehoeriger().isEmpty()) {
            mutter = mutterModel.getAngehoeriger();
            if (mutterModel.getAdresse() != null && !mutterModel.getAdresse().isEmpty()) {
                mutter.setAdresse(mutterModel.getAdresse());
            }
        }
        if (vaterModel.getAngehoeriger() != null && !vaterModel.getAngehoeriger().isEmpty()) {
            vater = vaterModel.getAngehoeriger();
            if (vaterModel.getAdresse() != null && !vaterModel.getAdresse().isEmpty()) {
                vater.setAdresse(vaterModel.getAdresse());
            }
        }
        if (rechnungsempfaengerDrittpersonModel.getAngehoeriger() != null && !rechnungsempfaengerDrittpersonModel.getAngehoeriger().isEmpty()) {
            rechnungsempfaengerDrittperson = rechnungsempfaengerDrittpersonModel.getAngehoeriger();
            if (rechnungsempfaengerDrittpersonModel.getAdresse() != null && !rechnungsempfaengerDrittpersonModel.getAdresse().isEmpty()) {
                rechnungsempfaengerDrittperson.setAdresse(rechnungsempfaengerDrittpersonModel.getAdresse());
            }
            isDrittpersonRechungsempfaenger = rechnungsempfaengerDrittpersonModel.isRechnungsempfaenger();
        }
    }

    @Override
    public void execute() {

        if (rechnungsempfaengerDrittperson == null || rechnungsempfaengerDrittperson.isEmpty() || !isDrittpersonRechungsempfaenger) {
            identical = false;
            errorDrittpersonIdentischMitElternteil = "";
        }

        else if (mutter != null && mutter.getVorname() != null && !mutter.getVorname().trim().isEmpty() && mutter.isIdenticalWith(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorDrittpersonIdentischMitElternteil = ERROR_IDENTISCH_MIT_MUTTER;
        }

        else if (mutter != null && mutter.getVorname() != null && !mutter.getVorname().trim().isEmpty() && mutter.isPartOf(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorDrittpersonIdentischMitElternteil = ERROR_WAHRSCHEINLICH_IDENTISCH_MIT_MUTTER;
        }

        else if (vater != null && vater.getVorname() != null && !vater.getVorname().trim().isEmpty() && vater.isIdenticalWith(rechnungsempfaengerDrittperson)) {
            identical = true;
            errorDrittpersonIdentischMitElternteil = ERROR_IDENTISCH_MIT_VATER;
        }

        else if (vater != null && vater.getVorname() != null && !vater.getVorname().trim().isEmpty() && vater.isPartOf(rechnungsempfaengerDrittperson)) {
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
