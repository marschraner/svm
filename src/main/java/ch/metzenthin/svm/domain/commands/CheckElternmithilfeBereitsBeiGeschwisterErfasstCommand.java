package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public class CheckElternmithilfeBereitsBeiGeschwisterErfasstCommand implements Command {

    // Input
    private Schueler schueler;
    private Maerchen maerchen;

    // Output
    private Schueler geschwisterMitBereitsErfassterElternmithilfe = null;

    public CheckElternmithilfeBereitsBeiGeschwisterErfasstCommand(Schueler schueler, Maerchen maerchen) {
        this.schueler = schueler;
        this.maerchen = maerchen;
    }

    @Override
    public void execute() {

        Set<Schueler> angemeldeteGeschwister = new HashSet<>();
        // Kinder der Mutter
        if (schueler.getMutter() != null) {
            for (Schueler kind : schueler.getMutter().getKinderMutter()) {
                if (!kind.isIdenticalWith(schueler) && kind.getAnmeldungen().get(0).getAbmeldedatum() == null) {
                    angemeldeteGeschwister.add(kind);
                }
            }
        }

        // Kinder des Vaters
        if (schueler.getVater() != null) {
            for (Schueler kind : schueler.getVater().getKinderVater()) {
                if (!kind.isIdenticalWith(schueler) && kind.getAnmeldungen().get(0).getAbmeldedatum() == null) {
                    angemeldeteGeschwister.add(kind);
                }
            }
        }

        for (Schueler geschwister : angemeldeteGeschwister) {
            for (Maercheneinteilung maercheneinteilung : geschwister.getMaercheneinteilungen()) {
                if (maercheneinteilung.getMaerchen().isIdenticalWith(maerchen) && maercheneinteilung.getElternmithilfe() != null) {
                    geschwisterMitBereitsErfassterElternmithilfe = geschwister;
                }
            }
        }
    }

    public Schueler getGeschwisterMitBereitsErfassterElternmithilfe() {
        return geschwisterMitBereitsErfassterElternmithilfe;
    }

}
