package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckGeschwisterSchuelerRechnungempfaengerCommand implements Command {

    // input
    private Schueler schueler;
    private final boolean isRechnungsempfaengerDrittperson;
    private final Angehoeriger mutter;
    private final Angehoeriger vater;
    private final Angehoeriger rechnungsempfaenger;

    // output
    private List<Schueler> angemeldeteGeschwisterList = new ArrayList<>();
    private List<Schueler> andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = new ArrayList<>();

    public CheckGeschwisterSchuelerRechnungempfaengerCommand(Schueler schueler) {
        this(schueler, null, null, null, false);
    }

    public CheckGeschwisterSchuelerRechnungempfaengerCommand(Schueler schueler, boolean isRechnungsempfaengerDrittperson) {
        this(schueler, null, null, null, isRechnungsempfaengerDrittperson);
    }

    public CheckGeschwisterSchuelerRechnungempfaengerCommand(Schueler schueler, Angehoeriger mutterFoundInDatabase, Angehoeriger vaterFoundInDatabase, Angehoeriger rechnungsempfaengerFoundInDatabase, boolean isRechnungsempfaengerDrittperson) {
        this.schueler = schueler;
        this.isRechnungsempfaengerDrittperson = isRechnungsempfaengerDrittperson;
        mutter = (mutterFoundInDatabase != null) ? mutterFoundInDatabase : schueler.getMutter();
        vater = (vaterFoundInDatabase != null) ? vaterFoundInDatabase : schueler.getVater();
        rechnungsempfaenger = (rechnungsempfaengerFoundInDatabase != null) ? rechnungsempfaengerFoundInDatabase : schueler.getRechnungsempfaenger();
    }

    @Override
    public void execute() {
        determineGeschwister();
        determineAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaenger();
    }

    private void determineGeschwister() {
        // Kinder der Mutter
        if (mutter != null) {
            for (Schueler kind : mutter.getKinderMutter()) {
                if (!kind.isIdenticalWith(schueler) && kind.getAnmeldungen().get(0).getAbmeldedatum() == null) {
                    angemeldeteGeschwisterList.add(kind);
                }
            }
        }

        // Kinder des Vaters
        if (vater != null) {
            for (Schueler kind : vater.getKinderVater()) {
                if (!kind.isIdenticalWith(schueler) && kind.getAnmeldungen().get(0).getAbmeldedatum() == null && !checkIfSchuelerAlreadyInList(kind, angemeldeteGeschwisterList)) {
                    angemeldeteGeschwisterList.add(kind);
                }
            }
        }
    }

    private void determineAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaenger() {
        // Mutter
        if (mutter != null) {
           for (Schueler schueler1 : mutter.getSchuelerRechnungsempfaenger()) {
                // Nicht nochmals aufnehmen, falls schon in Geschwister-Liste!
                if (!schueler1.isIdenticalWith(schueler) && schueler1.getAnmeldungen().get(0).getAbmeldedatum() == null && !checkIfSchuelerAlreadyInList(schueler1, angemeldeteGeschwisterList)) {
                    andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.add(schueler1);
                }
            }
        }

        // Vater
        if (vater != null) {
            for (Schueler schueler1 : vater.getSchuelerRechnungsempfaenger()) {
                if (!schueler1.isIdenticalWith(schueler) && schueler1.getAnmeldungen().get(0).getAbmeldedatum() == null && !checkIfSchuelerAlreadyInList(schueler1, angemeldeteGeschwisterList) && !checkIfSchuelerAlreadyInList(schueler1, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList)) {
                    andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.add(schueler1);
                }
            }
        }

        // Rechnungsempfänger Drittperson (d.h. weder Mutter noch Vater ist identisch mit Rechnungsempfänger)
        if (isRechnungsempfaengerDrittperson) {
            for (Schueler schueler1 : rechnungsempfaenger.getSchuelerRechnungsempfaenger()) {
                if (!schueler1.isIdenticalWith(schueler) && schueler1.getAnmeldungen().get(0).getAbmeldedatum() == null && !checkIfSchuelerAlreadyInList(schueler1, angemeldeteGeschwisterList) && !checkIfSchuelerAlreadyInList(schueler1, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList)) {
                    andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.add(schueler1);
                }
            }
        }
    }

    private boolean checkIfSchuelerAlreadyInList(Schueler schueler, List<Schueler> schuelerList) {
        for (Schueler s : schuelerList) {
            if (s.isIdenticalWith(schueler)) {
                return true;
            }
        }
        return false;
    }

    public List<Schueler> getAngemeldeteGeschwisterList() {
        return angemeldeteGeschwisterList;
    }

    public List<Schueler> getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList() {
        return andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList;
    }
}
