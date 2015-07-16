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
    private Angehoeriger mutter;
    private Angehoeriger vater;
    private Angehoeriger rechnungsempfaenger;

    // output
    private String identischeAdressen = "";
    private String abweichendeAdressen = "";

    public CheckIdentischeAdressenCommand(Schueler schueler) {
        this(schueler, null, null, null);
    }

    public CheckIdentischeAdressenCommand(Schueler schueler, Angehoeriger mutterFoundInDatabase, Angehoeriger vaterFoundInDatabase, Angehoeriger rechnungsempfaengerFoundInDatabase) {
        this.schueler = schueler;
        mutter = (mutterFoundInDatabase != null) ? mutterFoundInDatabase : schueler.getMutter();
        vater = (vaterFoundInDatabase != null) ? vaterFoundInDatabase : schueler.getVater();
        rechnungsempfaenger = (rechnungsempfaengerFoundInDatabase != null) ? rechnungsempfaengerFoundInDatabase : schueler.getRechnungsempfaenger();
    }

    @Override
    public void execute() {
        
        Angehoeriger rechnungsempfaengerDrittperson = null;
        if (!((mutter != null && mutter.isIdenticalWith(rechnungsempfaenger)) || (vater != null && vater.isIdenticalWith(rechnungsempfaenger)))) {
            rechnungsempfaengerDrittperson = rechnungsempfaenger;
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
            identischeAdressen = schuelerStr + ", Mutter, Vater und Rechnungsempfänger Drittperson haben identische Adressen";
        }

        // 2. 3 Adressen identisch
        else if (mutterHasAdresse && vaterHasAdresse
                && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())) {
            identischeAdressen = schuelerStr + ", Mutter und Vater haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null) {
                abweichendeAdressen = "Rechnungsempfänger Drittperson hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse
                && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            identischeAdressen = schuelerStr + ", Mutter und Rechnungsempfänger Drittperson haben identische Adressen";
            if (vaterHasAdresse) {
                abweichendeAdressen = "Vater hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && vaterHasAdresse
                && schueler.getAdresse().isIdenticalWith(vater.getAdresse())
                && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            identischeAdressen = schuelerStr + ", Vater und Rechnungsempfänger Drittperson haben identische Adressen";
            if (mutterHasAdresse) {
                abweichendeAdressen = "Mutter hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
                && mutter.getAdresse().isIdenticalWith(vater.getAdresse())
                && mutter.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            identischeAdressen = "Mutter, Vater und Rechnungsempfänger Drittperson haben identische Adressen";
            abweichendeAdressen = schuelerStr + " hat abweichende Adresse";
        }

        // 3.a 2 Adressen paarweise identisch
        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
            && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())
            && rechnungsempfaengerDrittperson.getAdresse().isIdenticalWith(vater.getAdresse())) {
            identischeAdressen = schuelerStr + " und Mutter haben identische Adressen, Vater und Rechnungsempfänger Drittperson haben identische Adressen";
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
            && schueler.getAdresse().isIdenticalWith(vater.getAdresse())
            && rechnungsempfaengerDrittperson.getAdresse().isIdenticalWith(mutter.getAdresse())) {
            identischeAdressen = schuelerStr + " und Vater haben identische Adressen, Mutter und Rechnungsempfänger Drittperson haben identische Adressen";
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && vaterHasAdresse
            && schueler.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())
            && vater.getAdresse().isIdenticalWith(mutter.getAdresse())) {
            identischeAdressen = schuelerStr + " und Rechnungsempfänger Drittperson haben identische Adressen, Mutter und Vater haben identische Adressen";
        }

        // 3.b 2 Adressen identisch, restliche verschieden
        else if (mutterHasAdresse && schueler.getAdresse().isIdenticalWith(mutter.getAdresse())) {
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
            identischeAdressen = "Mutter und Vater haben identische Adressen";
            if (rechnungsempfaengerDrittperson != null) {
                abweichendeAdressen = schuelerStr + " und Rechnungsempfänger Drittperson haben abweichende Adressen";
            }
            else {
                abweichendeAdressen = schuelerStr + " hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && mutterHasAdresse && mutter.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
            identischeAdressen = "Mutter und Rechnungsempfänger Drittperson haben identische Adressen";
            if (vaterHasAdresse) {
                abweichendeAdressen = schuelerStr + " und Vater haben abweichende Adressen";
            }
            else {
                abweichendeAdressen = schuelerStr + " hat abweichende Adresse";
            }
        }

        else if (rechnungsempfaengerDrittperson != null && vaterHasAdresse && vater.getAdresse().isIdenticalWith(rechnungsempfaengerDrittperson.getAdresse())) {
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
