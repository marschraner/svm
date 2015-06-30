package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CheckGeschwisterSchuelerRechnungempfaengerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattModelImpl implements SchuelerDatenblattModel {

    private final Schueler schueler;

    public SchuelerDatenblattModelImpl(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public String getSchuelerNachname() {
        return schueler.getNachname();
    }

    @Override
    public String getSchuelerVorname() {
        return schueler.getVorname();
    }

    @Override
    public String getSchuelerAsString() {
        return schueler.toString();
    }

    @Override
    public String getMutterAsString() {
        Angehoeriger mutter = schueler.getMutter();
        if (mutter != null) {
            return mutter.toString();
        }
        return "-";
    }

    @Override
    public String getVaterAsString() {
        Angehoeriger vater = schueler.getVater();
        if (vater != null) {
            return vater.toString();
        }
        return "-";
    }

    @Override
    public String getRechnungsempfaengerAsString() {
        Angehoeriger rechnungsempfaenger = schueler.getRechnungsempfaenger();
        if (rechnungsempfaenger != null) {
            if (rechnungsempfaenger == schueler.getMutter()) {
                return "Mutter";
            }
            if (rechnungsempfaenger == schueler.getVater()) {
                return "Vater";
            }
            return rechnungsempfaenger.toString();
        }
        return "-";
    }

    @Override
    public String getGeschwisterAsString() {
        CheckGeschwisterSchuelerRechnungempfaengerCommand command = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler);
        command.execute();
        List<Schueler> angemeldeteGeschwister = command.getAngemeldeteGeschwisterList();
        if (!angemeldeteGeschwister.isEmpty()) {
            StringBuilder infoGeschwisterStb = new StringBuilder("<html>");
            for (Schueler geschwister : angemeldeteGeschwister) {
                if (infoGeschwisterStb.length() > 6) {
                    infoGeschwisterStb.append("<br>");
                }
                infoGeschwisterStb.append(geschwister.toString());
            }
            infoGeschwisterStb.append("</html>");
            return infoGeschwisterStb.toString();
        }
        return "-";
    }

    @Override
    public String getSchuelerGleicherRechnungsempfaengerAsString() {
        CheckGeschwisterSchuelerRechnungempfaengerCommand command = new CheckGeschwisterSchuelerRechnungempfaengerCommand(schueler);
        command.execute();
        List<Schueler> schuelerList = command.getAndereSchuelerMitVaterMutterOderDrittpersonAlsRechnungsempfaengerList();
        if (!schuelerList.isEmpty()) {
            StringBuilder infoSchuelerGleicherRechnungsempfaenger = new StringBuilder("<html>");
            for (Schueler schueler : schuelerList) {
                if (infoSchuelerGleicherRechnungsempfaenger.length() > 6) {
                    infoSchuelerGleicherRechnungsempfaenger.append("<br>");
                }
                infoSchuelerGleicherRechnungsempfaenger.append(schueler.toString());
            }
            infoSchuelerGleicherRechnungsempfaenger.append("</html>");
            return infoSchuelerGleicherRechnungsempfaenger.toString();
        }
        return "-";
    }

    @Override
    public String getAnmeldungenAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (!anmeldungen.isEmpty()) {
            StringBuilder infoAnmeldungen = new StringBuilder("<html>");
            for (Anmeldung anmeldung : anmeldungen) {
                if (infoAnmeldungen.length() > 6) {
                    infoAnmeldungen.append("<br>");
                }
                infoAnmeldungen.append(anmeldung.toString());
            }
            infoAnmeldungen.append("</html>");
            return infoAnmeldungen.toString();
        }
        return "-";
    }

}
