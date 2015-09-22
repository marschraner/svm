package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurs;
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

        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "8859_1"));

            // Header
            out.write("Nachname");
            out.write(separator);
            out.write("Vorname");
            out.write(separator);
            out.write("Strasse/Nr.");
            out.write(separator);
            out.write("PLZ");
            out.write(separator);
            out.write("Ort");
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
            out.write('\n');

            // Daten
            for (Schueler schueler : schuelerSuchenTableModel.getSchuelerList()) {
                out.write(schueler.getNachname());
                out.write(separator);
                out.write(schueler.getVorname());
                out.write(separator);
                out.write(schueler.getAdresse().getStrasseHausnummer());
                out.write(separator);
                out.write(schueler.getAdresse().getPlz());
                out.write(separator);
                out.write(schueler.getAdresse().getOrt());
                out.write(separator);
                out.write(asString(schueler.getGeburtsdatum()));
                out.write(separator);
                if (schueler.getMutter() != null) {
                    out.write(schueler.getMutter().getVorname() + " " + schueler.getMutter().getNachname());
                }
                out.write(separator);
                if (schueler.getVater() != null) {
                    out.write(schueler.getVater().getVorname() + " " + schueler.getVater().getNachname());
                }
                out.write(separator);
                String rechnungsempfaenger;
                if (schueler.getMutter() != null && schueler.getMutter().isIdenticalWith(schueler.getRechnungsempfaenger())) {
                    rechnungsempfaenger = "Mutter";
                } else if (schueler.getVater() != null && schueler.getVater().isIdenticalWith(schueler.getRechnungsempfaenger())) {
                    rechnungsempfaenger = "Vater";
                } else {
                    rechnungsempfaenger = schueler.getRechnungsempfaenger().getVorname() + " " + schueler.getRechnungsempfaenger().getNachname();
                }
                out.write(rechnungsempfaenger);
                out.write(separator);
                out.write(getKurseSchueler(schueler));
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

}
