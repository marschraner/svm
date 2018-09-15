package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.NachnameGratiskindFormatter;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.io.*;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class CreateSchuelerlisteCsvFileCommand extends CreateListeCommand {

    // input
    private SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final File outputFile;

    public CreateSchuelerlisteCsvFileCommand(SchuelerSuchenTableModel schuelersuchenTableModel, File outputFile) {
        this.schuelerSuchenTableModel = schuelersuchenTableModel;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {

        Character separator = ';';

        NachnameGratiskindFormatter nachnameGratiskindFormatter = new NachnameGratiskindFormatter();

        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "8859_1"));

            // Header
            out.write("Nachname");
            out.write(separator);
            out.write("Vorname");
            out.write(separator);
            out.write("Strasse/Nr.");
            out.write(separator);
            out.write("PLZ/Ort");
            out.write(separator);
            out.write("Geburtsdatum");
            out.write(separator);
            out.write("Mutter");
            out.write(separator);
            out.write("Vater");
            out.write(separator);
            out.write("Rechnungsempfänger");
            out.write(separator);
            out.write("Kurse");
            out.write(separator);
            out.write("Gruppe");
            out.write(separator);
            out.write("Anmeldung(en)");
            out.write(separator);
            out.write("Codes");
            out.write('\n');

            // Daten
            for (Schueler schueler : schuelerSuchenTableModel.getSelektierteSchuelerList()) {
                out.write(schueler.getNachname());
                out.write(separator);
                out.write(schueler.getVorname());
                out.write(separator);
                out.write(schueler.getAdresse().getStrHausnummer());
                out.write(separator);
                out.write(schueler.getAdresse().getPlzOrt());
                out.write(separator);
                out.write(asString(schueler.getGeburtsdatum()));
                out.write(separator);
                if (schueler.getMutter() != null) {
                    out.write(nachnameGratiskindFormatter.format(schueler.getMutter().getNachname()) + " " + schueler.getMutter().getVorname());
                }
                out.write(separator);
                if (schueler.getVater() != null) {
                    out.write(nachnameGratiskindFormatter.format(schueler.getVater().getNachname()) + " " + schueler.getVater().getVorname());
                }
                out.write(separator);
                String rechnungsempfaenger;
                if (schueler.getMutter() != null && schueler.getMutter().isIdenticalWith(schueler.getRechnungsempfaenger())) {
                    rechnungsempfaenger = "Mutter";
                } else if (schueler.getVater() != null && schueler.getVater().isIdenticalWith(schueler.getRechnungsempfaenger())) {
                    rechnungsempfaenger = "Vater";
                } else {
                    rechnungsempfaenger = nachnameGratiskindFormatter.format(schueler.getRechnungsempfaenger().getNachname()) + " " + schueler.getRechnungsempfaenger().getVorname();
                }
                out.write(rechnungsempfaenger);
                out.write(separator);
                out.write(getKurseSchueler(schueler));
                out.write(separator);
                Maercheneinteilung maercheneinteilung = schuelerSuchenTableModel.getMaercheneinteilungen().get(schueler);
                if (maercheneinteilung != null) {
                    out.write(maercheneinteilung.getGruppe().toString());
                }
                out.write(separator);
                out.write(getAnmeldungen(schueler));
                out.write(separator);
                out.write(schueler.getSchuelerCodesAsStr());
                out.write('\n');
            }

            out.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getKurseSchueler(Schueler schueler) {
        List<Kurs> kurse = schuelerSuchenTableModel.getKurse().get(schueler);
        if (kurse == null || kurse.isEmpty()) {
            return "";
        }
        StringBuilder kurseAsStr = new StringBuilder(kurse.get(0).toStringShort());
        for (int i = 1; i < kurse.size(); i++) {
            kurseAsStr.append(", ").append(kurse.get(i).toStringShort());
        }
        return kurseAsStr.toString();
    }

    public String getAnmeldungen(Schueler schueler) {
        List<Anmeldung> anmeldungen = schueler.getAnmeldungen();
        StringBuilder anmeldungenAsStr = new StringBuilder(anmeldungen.get(anmeldungen.size() - 1).toString());
        for (int i = anmeldungen.size() - 2; i >= 0; i--) {
            anmeldungenAsStr.append(", ").append(anmeldungen.get(i).toString());
        }
        return anmeldungenAsStr.toString();
    }

}
