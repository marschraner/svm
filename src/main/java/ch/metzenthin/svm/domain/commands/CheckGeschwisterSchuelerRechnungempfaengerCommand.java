package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckGeschwisterSchuelerRechnungempfaengerCommand implements Command {

    // input
    private Schueler schueler;

    // output
    private List<Schueler> angemeldeteGeschwisterList = new ArrayList<>();
    private List<Schueler> andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList = new ArrayList<>();

    public CheckGeschwisterSchuelerRechnungempfaengerCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        determineGeschwister();
        determineAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaenger();
    }

    private void determineGeschwister() {
        // Kinder der Mutter
        if (schueler.getMutter() != null) {
            for (Schueler kind : schueler.getMutter().getKinderMutter()) {
                if (!kind.isIdenticalWith(schueler) && kind.getAbmeldedatum() == null) {
                    angemeldeteGeschwisterList.add(kind);
                }
            }
        }

        // Kinder des Vaters
        if (schueler.getVater() != null) {
            for (Schueler kind : schueler.getVater().getKinderVater()) {
                if (!kind.isIdenticalWith(schueler) && kind.getAbmeldedatum() == null && !checkIfSchuelerAlreadyInList(kind, angemeldeteGeschwisterList)) {
                    angemeldeteGeschwisterList.add(kind);
                }
            }
        }
    }

    private void determineAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaenger() {
        // Mutter
        if (schueler.getMutter() != null) {
           for (Schueler schueler1 : schueler.getMutter().getSchuelerRechnungsempfaenger()) {
                // Nicht nochmals aufnehmen, falls schon in Geschwister-Liste!
                if (!schueler1.isIdenticalWith(schueler) && schueler1.getAbmeldedatum() == null && !checkIfSchuelerAlreadyInList(schueler1, angemeldeteGeschwisterList)) {
                    andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.add(schueler1);
                }
            }
        }

        // Vater
        if (schueler.getVater() != null) {
            for (Schueler schueler1 : schueler.getVater().getSchuelerRechnungsempfaenger()) {
                if (!schueler1.isIdenticalWith(schueler) && schueler1.getAbmeldedatum() == null && !checkIfSchuelerAlreadyInList(schueler1, angemeldeteGeschwisterList) && !checkIfSchuelerAlreadyInList(schueler1, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList)) {
                    andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList.add(schueler1);
                }
            }
        }

        // Rechnungsempfänger Drittperson (d.h. weder Mutter noch Vater ist identisch mit Rechnungsempfänger)
        if (!(schueler.getMutter() != null && schueler.getMutter().isIdenticalWith(schueler.getRechnungsempfaenger()))
                || (schueler.getVater() != null) && schueler.getVater().isIdenticalWith(schueler.getRechnungsempfaenger())) {
            for (Schueler schueler1 : schueler.getRechnungsempfaenger().getSchuelerRechnungsempfaenger()) {
                if (!schueler1.isIdenticalWith(schueler) && schueler1.getAbmeldedatum() == null && !checkIfSchuelerAlreadyInList(schueler1, angemeldeteGeschwisterList) && !checkIfSchuelerAlreadyInList(schueler1, andereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList)) {
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
