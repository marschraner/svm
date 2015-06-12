package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class CheckIdentischeAdressenCommand implements Command {

    // input + output
    private Schueler schueler;

    // output
    private String identischeAdressen = "";
    private String abweichendeAdressen = "";

    public CheckIdentischeAdressenCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        
        Angehoeriger mutter = schueler.getMutter();
        Angehoeriger vater = schueler.getVater();
        Angehoeriger rechnungsempfaengerDrittperson = null;
        if (!((mutter != null && mutter.isIdenticalWith(schueler.getRechnungsempfaenger())) || (vater != null && vater.isIdenticalWith(schueler.getRechnungsempfaenger())))) {
            rechnungsempfaengerDrittperson = schueler.getRechnungsempfaenger();
        }

        boolean mutterHasAdresse = false;
        boolean vaterHasAdresse = false;
        if (mutter != null) {
            mutterHasAdresse = mutter.getAdresse() != null;
        }
        if (vater != null) {
            vaterHasAdresse = vater.getAdresse() != null;
        }
        
        String schuelerStr = (schueler.getGeschlecht() == Geschlecht.W ? "Schülerin" : "Schüler");

        // 1. Alle 4 Adressen identisch
        if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
                && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            schueler.setAdresse(mutter.getAdresse());
            vater.setAdresse(mutter.getAdresse());
            rechnungsempfaengerDrittperson.setAdresse(mutter.getAdresse());
            identischeAdressen = schuelerStr + ", Mutter, Vater und Rechnungsempfänger Drittperson haben identische Adressen";
        }

        // 2. 3 Adressen identisch
        else if (mutterHasAdresse && vaterHasAdresse
                && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())) {
            schueler.setAdresse(mutter.getAdresse());
            vater.setAdresse(mutter.getAdresse());
            identischeAdressen = schuelerStr + ", Mutter und Vater haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null) {
                abweichendeAdressen = "Rechnungsempfänger Drittperson hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse
                && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            schueler.setAdresse(mutter.getAdresse());
            rechnungsempfaengerDrittperson.setAdresse(mutter.getAdresse());
            identischeAdressen = schuelerStr + ", Mutter und Rechnungsempfänger Drittperson haben identische Adressen";
            if (vaterHasAdresse) {
                abweichendeAdressen = "Vater hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && vaterHasAdresse
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            schueler.setAdresse(vater.getAdresse());
            rechnungsempfaengerDrittperson.setAdresse(vater.getAdresse());
            identischeAdressen = schuelerStr + ", Vater und Rechnungsempfänger Drittperson haben identische Adressen";
            if (mutterHasAdresse) {
                abweichendeAdressen = "Mutter hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
                && mutter.getAdresse().isIdenticalWith(vater.getAdresse())
                && mutter.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            vater.setAdresse(mutter.getAdresse());
            rechnungsempfaengerDrittperson.setAdresse(mutter.getAdresse());
            identischeAdressen = "Mutter, Vater und Rechnungsempfänger Drittperson haben identische Adressen";
            abweichendeAdressen = schuelerStr + " hat abweichende Adresse";
        }

        // 3.a 2 Adressen paarweise identisch
        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
            && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
            && rechnungsempfaengerDrittperson.getAdresse().isIdenticalWith(vater.getAdresse())) {
            schueler.setAdresse(mutter.getAdresse());
            rechnungsempfaengerDrittperson.setAdresse(vater.getAdresse());
            identischeAdressen = schuelerStr + " und Mutter haben identische Adressen, Vater und Rechnungsempfänger Drittperson haben identische Adressen";
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
            && schueler.getAdresse().isIdenticalWith(vater.getAdresse())
            && rechnungsempfaengerDrittperson.getAdresse().isIdenticalWith(mutter.getAdresse())) {
            schueler.setAdresse(vater.getAdresse());
            rechnungsempfaengerDrittperson.setAdresse(mutter.getAdresse());
            identischeAdressen = schuelerStr + " und Vater haben identische Adressen, Mutter und Rechnungsempfänger Drittperson haben identische Adressen";
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
            && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())
            && vater.getAdresse().isIdenticalWith(mutter.getAdresse())) {
            schueler.setAdresse(rechnungsempfaengerDrittperson.getAdresse());
            vater.setAdresse(mutter.getAdresse());
            identischeAdressen = schuelerStr + " und Rechnungsempfänger Drittperson haben identische Adressen, Mutter und Vater haben identische Adressen";
        }

        // 3.b 2 Adressen identisch, restliche verschieden
        else if (mutterHasAdresse && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())) {
            schueler.setAdresse(mutter.getAdresse());
            identischeAdressen = schuelerStr + " und Mutter haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null && vaterHasAdresse) {
                abweichendeAdressen = "Vater und Rechnungsempfänger Drittperson haben abweichende Adressen";
            } else if (rechnungsempfaengerDrittperson != null) {
                abweichendeAdressen = "Rechnungsempfänger Drittperson hat abweichende Adresse";
            } else if (vaterHasAdresse) {
                abweichendeAdressen = "Vater hat abweichende Adresse";
            }
        }

        else if (vaterHasAdresse && schueler.getAdresse().isIdenticalWith(vater.getAdresse())) {
            schueler.setAdresse(vater.getAdresse());
            identischeAdressen = schuelerStr + " und Vater haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null && mutterHasAdresse) {
                abweichendeAdressen = "Mutter und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else if (rechnungsempfaengerDrittperson != null) {
                abweichendeAdressen = "Rechnungsempfänger Drittperson hat abweichende Adresse";
            }
            else if (mutterHasAdresse) {
                abweichendeAdressen = "Mutter hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            schueler.setAdresse(rechnungsempfaengerDrittperson.getAdresse());
            identischeAdressen = schuelerStr + " und Rechnungsempfänger Drittperson haben identische Adressen";
            if (mutterHasAdresse && vaterHasAdresse) {
                abweichendeAdressen = "Mutter und Vater haben abweichende Adressen";
            }
            else if (mutterHasAdresse) {
                abweichendeAdressen = "Mutter hat abweichende Adresse";
            }
            else if (vaterHasAdresse) {
                abweichendeAdressen = "Vater hat abweichende Adresse";
            }
        }

        else if (mutterHasAdresse && vaterHasAdresse && mutter.getAdresse().isIdenticalWith(vater.getAdresse())) {
            vater.setAdresse(mutter.getAdresse());
            identischeAdressen = "Mutter und Vater haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null) {
                abweichendeAdressen = schuelerStr + " und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else {
                abweichendeAdressen = schuelerStr + " hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && mutter.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            rechnungsempfaengerDrittperson.setAdresse(mutter.getAdresse());
            identischeAdressen = "Mutter und Rechnungsempfänger Drittperson haben identische Adressen";
            if (vaterHasAdresse) {
                abweichendeAdressen = schuelerStr + " und Vater haben abweichende Adressen";
            }
            else {
                abweichendeAdressen = schuelerStr + " hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && vaterHasAdresse && vater.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            rechnungsempfaengerDrittperson.setAdresse(vater.getAdresse());
            identischeAdressen = "Vater und Rechnungsempfänger Drittperson haben identische Adressen";
            if (mutterHasAdresse) {
                abweichendeAdressen = schuelerStr + " und Mutter haben abweichende Adressen";
            }
            else {
                abweichendeAdressen = schuelerStr + " hat abweichende Adresse";
            }
        }

        // 4. alle Adressen verschieden
        else {
            if (rechnungsempfaengerDrittperson != null && vaterHasAdresse && mutterHasAdresse) {
                abweichendeAdressen = schuelerStr + ", Mutter, Vater und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else if (rechnungsempfaengerDrittperson != null && !vaterHasAdresse && mutterHasAdresse) {
                abweichendeAdressen = schuelerStr + ", Mutter und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else if (rechnungsempfaengerDrittperson != null && vaterHasAdresse) {
                abweichendeAdressen = schuelerStr + ", Vater und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else if (rechnungsempfaengerDrittperson != null) {
                abweichendeAdressen = schuelerStr + " und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else if (vaterHasAdresse && mutterHasAdresse) {
                abweichendeAdressen = schuelerStr + ", Mutter und Vater haben abweichende Adressen";
            }
            else if (!vaterHasAdresse && mutterHasAdresse) {
                abweichendeAdressen = schuelerStr + " und Mutter haben abweichende Adressen";
            }
            else if (vaterHasAdresse) {
                abweichendeAdressen = schuelerStr + " und Vater haben abweichende Adressen";
            }
        }

    }

    public String getIdentischeAdressen() {
        return identischeAdressen;
    }

    public String getAbweichendeAdressen() {
        return abweichendeAdressen;
    }
}
