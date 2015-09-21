package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.io.*;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class CreateRechnungenSerienbriefCsvFileCommand extends CreateListeCommand {

    // input
    private List<? extends Semesterrechnung> semesterrechnungList;
    private Rechnungstyp rechnungstyp;
    private final File outputFile;

    public CreateRechnungenSerienbriefCsvFileCommand(List<? extends Semesterrechnung> semesterrechnungList, Rechnungstyp rechnungstyp, File outputFile) {
        this.semesterrechnungList = semesterrechnungList;
        this.rechnungstyp = rechnungstyp;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {

        Character separator = ';';

        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "8859_1"));

            // Header
            out.write("Anrede");
            out.write(separator);
            out.write("Vorname");
            out.write(separator);
            out.write("Nachname");
            out.write(separator);
            out.write("Strasse");
            out.write(separator);
            out.write("PLZ");
            out.write(separator);
            out.write("Ort");
            out.write(separator);
            out.write("R.Datum");
            out.write(separator);
            out.write("Anz.");
            out.write(separator);
            out.write("Wochenbetrag");
            out.write(separator);
            out.write("Schulgeld");
            out.write('\n');

            // Daten
            for (Semesterrechnung semesterrechnung : semesterrechnungList) {
                if (rechnungstyp == Rechnungstyp.VORRECHNUNG) {
                    if (semesterrechnung.getRechnungsdatumVorrechnung() == null ||
                            semesterrechnung.getWochenbetragVorrechnung() == null ||
                            semesterrechnung.getSchulgeldVorrechnung() == null) {
                        continue;
                    }
                } else {
                    if (semesterrechnung.getRechnungsdatumNachrechnung() == null ||
                            semesterrechnung.getWochenbetragNachrechnung() == null ||
                            semesterrechnung.getSchulgeldNachrechnung() == null) {
                        continue;
                    }
                }
                Angehoeriger rechnungsempfaenger = semesterrechnung.getRechnungsempfaenger();
                String anrede = rechnungsempfaenger.getAnrede().toString();
                out.write(anrede);
                out.write(separator);
                out.write(rechnungsempfaenger.getVorname());
                out.write(separator);
                out.write(rechnungsempfaenger.getNachname());
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getStrasseHausnummer());
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getPlz());
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getOrt());
                out.write(separator);
                if (rechnungstyp == Rechnungstyp.VORRECHNUNG) {
                    out.write(asString(semesterrechnung.getRechnungsdatumVorrechnung()));
                    out.write(separator);
                    out.write(Integer.toString(semesterrechnung.getAnzahlWochenVorrechnung()));
                    out.write(separator);
                    out.write(semesterrechnung.getWochenbetragVorrechnung().toString());
                    out.write(separator);
                    out.write(semesterrechnung.getSchulgeldVorrechnung().toString());
                } else {
                    out.write(asString(semesterrechnung.getRechnungsdatumNachrechnung()));
                    out.write(separator);
                    out.write(Integer.toString(semesterrechnung.getAnzahlWochenNachrechnung()));
                    out.write(separator);
                    out.write(semesterrechnung.getWochenbetragNachrechnung().toString());
                    out.write(separator);
                    out.write(semesterrechnung.getSchulgeldNachrechnung().toString());
                }
                out.write('\n');
            }

            out.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
