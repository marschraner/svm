package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class CheckIdentischeAdressenCommand extends GenericDaoCommand {

    // input + output
    private Schueler schueler;
    private Angehoeriger mutter;
    private Angehoeriger vater;
    private Angehoeriger rechnungsempfaengerDrittperson;

    // output
    private String infoIdentischeAdressen = "";
    private String infoAbweichendeAdressen = "";

    public CheckIdentischeAdressenCommand(Schueler schueler, Angehoeriger mutter, Angehoeriger vater, Angehoeriger rechnungsempfaengerDrittperson) {
        this.schueler = schueler;
        this.mutter = mutter;
        this.vater = vater;
        this.rechnungsempfaengerDrittperson = rechnungsempfaengerDrittperson;
    }

    @Override
    public void execute() {

        boolean mutterHasAdresse = false;
        boolean vaterHasAdresse = false;
        if (mutter != null) {
            mutterHasAdresse = mutter.getAdresse() != null;
        }
        if (vater != null) {
            vaterHasAdresse = vater.getAdresse() != null;
        }

        // 1. Alle 4 Adressen identisch
        if (mutterHasAdresse && vaterHasAdresse
                && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            schueler.setNewAdresse(mutter.getAdresse());
            vater.setNewAdresse(mutter.getAdresse());
            rechnungsempfaengerDrittperson.setNewAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Schüler, Mutter, Vater und Rechnungsempfänger Drittperson haben identische Adressen";
        }

        // 2. 3 Adressen identisch
        else if (mutterHasAdresse && vaterHasAdresse
                && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())) {
            schueler.setNewAdresse(mutter.getAdresse());
            vater.setNewAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Schüler, Mutter und Vater haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null) {
                infoAbweichendeAdressen = "Rechnungsempfänger Drittperson hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse
                && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            schueler.setNewAdresse(mutter.getAdresse());
            rechnungsempfaengerDrittperson.setNewAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Schüler, Mutter und Rechnungsempfänger Drittperson haben identische Adressen";
            if (vaterHasAdresse) {
                infoAbweichendeAdressen = "Vater hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && vaterHasAdresse
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            schueler.setNewAdresse(vater.getAdresse());
            rechnungsempfaengerDrittperson.setNewAdresse(vater.getAdresse());
            infoIdentischeAdressen = "Schüler, Vater und Rechnungsempfänger Drittperson haben identische Adressen";
            if (mutterHasAdresse) {
                infoAbweichendeAdressen = "Mutter hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
                && mutter.getAdresse().isIdenticalWith(vater.getAdresse())
                && mutter.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            vater.setNewAdresse(mutter.getAdresse());
            rechnungsempfaengerDrittperson.setNewAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Mutter, Vater und Rechnungsempfänger Drittperson haben identische Adressen";
            infoAbweichendeAdressen = "Schüler hat abweichende Adresse";
        }

        // 3. 2 Adressen identisch
        else if (mutterHasAdresse && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())) {
            schueler.setNewAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Schüler und Mutter haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null && vaterHasAdresse) {
                infoAbweichendeAdressen = "Vater und Rechnungsempfänger Drittperson haben abweichende Adressen";
            } else if (rechnungsempfaengerDrittperson != null) {
                infoAbweichendeAdressen = "Rechnungsempfänger Drittperson hat abweichende Adressen";
            } else if (vaterHasAdresse) {
                infoAbweichendeAdressen = "Vater hat abweichende Adressen";
            }
        }

        else if (vaterHasAdresse && schueler.getAdresse().isIdenticalWith(vater.getAdresse())) {
            schueler.setNewAdresse(vater.getAdresse());
            infoIdentischeAdressen = "Schüler und Vater haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null && mutterHasAdresse) {
                infoAbweichendeAdressen = "Mutter und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else if (rechnungsempfaengerDrittperson != null) {
                infoAbweichendeAdressen = "Rechnungsempfänger Drittperson hat abweichende Adressen";
            }
            else if (mutterHasAdresse) {
                infoAbweichendeAdressen = "Mutter hat abweichende Adressen";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            schueler.setNewAdresse(rechnungsempfaengerDrittperson.getAdresse());
            infoIdentischeAdressen = "Schüler und Rechnungsempfänger haben identische Adressen";
            if (mutter.getAdresse() != null)
                infoAbweichendeAdressen = "Mutter und Vater haben abweichende Adressen";
        }

        else if (mutterHasAdresse && vaterHasAdresse && mutter.getAdresse().isIdenticalWith(vater.getAdresse())) {
            vater.setNewAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Mutter und Vater haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null) {
                infoAbweichendeAdressen = "Schüler und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else {
                infoAbweichendeAdressen = "Schüler hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && mutter.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            rechnungsempfaengerDrittperson.setNewAdresse(mutter.getAdresse());
            infoIdentischeAdressen = "Mutter und Rechnungsempfänger Drittperson haben identische Adressen";
            if (vaterHasAdresse) {
                infoAbweichendeAdressen = "Schüler und Vater haben abweichende Adressen";
            }
            else {
                infoAbweichendeAdressen = "Schüler hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && vaterHasAdresse && vater.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            rechnungsempfaengerDrittperson.setNewAdresse(vater.getAdresse());
            infoIdentischeAdressen = "Vater und Rechnungsempfänger Drittperson haben identische Adressen";
            if (mutterHasAdresse) {
                infoAbweichendeAdressen = "Schüler und Mutter haben abweichende Adressen";
            }
            else {
                infoAbweichendeAdressen = "Schüler hat abweichende Adresse";
            }
        }

        // 4. alle Adressen verschieden
        else {
            if (rechnungsempfaengerDrittperson != null && vaterHasAdresse && mutterHasAdresse) {
                infoAbweichendeAdressen = "Schüler, Mutter, Vater und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else if (rechnungsempfaengerDrittperson != null && !vaterHasAdresse && mutterHasAdresse) {
                infoAbweichendeAdressen = "Schüler, Mutter und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else if (rechnungsempfaengerDrittperson != null && vaterHasAdresse && !mutterHasAdresse) {
                infoAbweichendeAdressen = "Schüler, Vater und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else if (rechnungsempfaengerDrittperson == null && vaterHasAdresse && mutterHasAdresse) {
                infoAbweichendeAdressen = "Schüler, Mutter und Vater haben abweichende Adressen";
            }
            else if (rechnungsempfaengerDrittperson == null && !vaterHasAdresse && mutterHasAdresse) {
                infoAbweichendeAdressen = "Schüler und Mutter haben abweichende Adressen";
            }
            else {
                infoAbweichendeAdressen = "Schüler und Vater haben abweichende Adressen";
            }
        }

    }

    public Schueler getSchueler() {
        return schueler;
    }

    public Angehoeriger getMutter() {
        return mutter;
    }

    public Angehoeriger getVater() {
        return vater;
    }

    public Angehoeriger getRechnungsempfaengerDrittperson() {
        return rechnungsempfaengerDrittperson;
    }

    public String getInfoIdentischeAdressen() {
        return infoIdentischeAdressen;
    }

    public String getInfoAbweichendeAdressen() {
        return infoAbweichendeAdressen;
    }
}
