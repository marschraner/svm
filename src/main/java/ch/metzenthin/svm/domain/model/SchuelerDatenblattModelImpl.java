package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.commands.CheckGeschwisterSchuelerRechnungempfaengerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

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
    public String getLabelSchueler() {
        return (schueler.getGeschlecht() == Geschlecht.W ? "Schülerin:" : "Schüler:");
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
    public String getLabelRechnungsempfaenger() {
        return (schueler.getRechnungsempfaenger().getAnrede() == Anrede.FRAU ? "Rechnungsempfängerin:" : "Rechnungsempfänger:");
    }

    @Override
    public String getRechnungsempfaengerAsString() {
        Angehoeriger rechnungsempfaenger = schueler.getRechnungsempfaenger();
        if (rechnungsempfaenger != null) {
            if (isRechnungsempfaengerMutter()) {
                return "Mutter";
            }
            if (isRechnungsempfaengerVater()) {
                return "Vater";
            }
            return rechnungsempfaenger.toString();
        }
        return "-";
    }

    private boolean isRechnungsempfaengerMutter() {
        return isRechnungsempfaenger(schueler.getMutter());
    }

    private boolean isRechnungsempfaengerVater() {
        return isRechnungsempfaenger(schueler.getVater());
    }

    private boolean isRechnungsempfaenger(Angehoeriger angehoeriger) {
        Angehoeriger rechnungsempfaenger = schueler.getRechnungsempfaenger();
        if (rechnungsempfaenger != null) {
            if (rechnungsempfaenger == angehoeriger) {
                return true;
            }
        }
        return false;
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
    public String getLabelSchuelerGleicherRechnungsempfaenger1() {
        return "Andere Schüler mit " + (schueler.getRechnungsempfaenger().getAnrede() == Anrede.FRAU ? "gleicher" : "gleichem");
    }

    @Override
    public String getLabelSchuelerGleicherRechnungsempfaenger2() {
        return (schueler.getRechnungsempfaenger().getAnrede() == Anrede.FRAU ? "Rechnungsempfängerin:" : "Rechnungsempfänger:");
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
    public String getSchuelerGeburtsdatumAsString() {
        return asString(schueler.getGeburtsdatum());
    }

    @Override
    public String getAnmeldedatumAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (!anmeldungen.isEmpty()) {
            if (anmeldungen.get(0).getAnmeldedatum() != null) {
                return asString(anmeldungen.get(0).getAnmeldedatum());
            }
        }
        return "-";
    }

    @Override
    public String getAbmeldedatumAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (!anmeldungen.isEmpty()) {
            if (anmeldungen.get(0).getAbmeldedatum() != null) {
                return asString(anmeldungen.get(0).getAbmeldedatum());
            }
        }
        return "";
    }

    @Override
    public String getFruehereAnmeldungenAsString() {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        if (anmeldungen.size() > 1) {
            StringBuilder fruehereAnmeldungen = new StringBuilder("<html>");
            for (int i = 1; i < anmeldungen.size(); i++) {
                if (fruehereAnmeldungen.length() > 6) {
                    fruehereAnmeldungen.append("<br>");
                }
                fruehereAnmeldungen.append(anmeldungen.get(i).toString());
            }
            fruehereAnmeldungen.append("</html>");
            return fruehereAnmeldungen.toString();
        }
        return "";
    }

    @Override
    public SchuelerModel getSchuelerModel(SvmContext svmContext) {
        SchuelerModel schuelerModel = svmContext.getModelFactory().createSchuelerModel();
        schuelerModel.setSchueler(schueler);
        return schuelerModel;
    }

    @Override
    public AngehoerigerModel getMutterModel(SvmContext svmContext) {
        AngehoerigerModel mutterModel = svmContext.getModelFactory().createAngehoerigerModel();
        mutterModel.setAngehoeriger(schueler.getMutter(), isGleicheAdresseWieSchueler(schueler.getMutter()), isRechnungsempfaengerMutter());
        return mutterModel;
    }

    @Override
    public AngehoerigerModel getVaterModel(SvmContext svmContext) {
        AngehoerigerModel vaterModel = svmContext.getModelFactory().createAngehoerigerModel();
        vaterModel.setAngehoeriger(schueler.getVater(), isGleicheAdresseWieSchueler(schueler.getVater()), isRechnungsempfaengerVater());
        return vaterModel;
    }

    @Override
    public AngehoerigerModel getRechnungsempfaengerModel(SvmContext svmContext) {
        AngehoerigerModel drittempfaengerModel = svmContext.getModelFactory().createAngehoerigerModel();
        if (!isRechnungsempfaengerMutter() && !isRechnungsempfaengerVater()) {
            drittempfaengerModel.setAngehoeriger(schueler.getRechnungsempfaenger(), false, true);
        }
        return drittempfaengerModel;
    }

    private boolean isGleicheAdresseWieSchueler(Angehoeriger angehoeriger) {
        return (angehoeriger != null) && (schueler.getAdresse() == angehoeriger.getAdresse());
    }

}
