package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
            FileWriter writer = new FileWriter(outputFile);

            // Header
            writer.append("Anrede");
            writer.append(separator);
            writer.append("Vorname");
            writer.append(separator);
            writer.append("Nachname");
            writer.append(separator);
            writer.append("Strasse");
            writer.append(separator);
            writer.append("PLZ");
            writer.append(separator);
            writer.append("Ort");
            writer.append(separator);
            writer.append("R.Datum");
            writer.append(separator);
            writer.append("Anz.");
            writer.append(separator);
            writer.append("Wochenbetrag");
            writer.append(separator);
            writer.append("Schulgeld");
            writer.append('\n');

            // Daten
            for (Semesterrechnung semesterrechnung : semesterrechnungList) {
                Angehoeriger rechnungsempfaenger = semesterrechnung.getRechnungsempfaenger();
                String anrede = rechnungsempfaenger.getAnrede().toString();
                writer.append(anrede);
                writer.append(separator);
                writer.append(rechnungsempfaenger.getVorname());
                writer.append(separator);
                writer.append(rechnungsempfaenger.getNachname());
                writer.append(separator);
                writer.append(rechnungsempfaenger.getAdresse().getStrHausnummer());
                writer.append(separator);
                writer.append(rechnungsempfaenger.getAdresse().getPlz());
                writer.append(separator);
                writer.append(rechnungsempfaenger.getAdresse().getOrt());
                writer.append(separator);
                if (rechnungstyp == Rechnungstyp.VORRECHNUNG) {
                    writer.append(asString(semesterrechnung.getRechnungsdatumVorrechnung()));
                    writer.append(separator);
                    writer.append(Integer.toString(semesterrechnung.getAnzahlWochenVorrechnung()));
                    writer.append(separator);
                    writer.append(semesterrechnung.getWochenbetragVorrechnung().toString());
                    writer.append(separator);
                    writer.append(semesterrechnung.getSchulgeldVorrechnung().toString());
                } else {
                    writer.append(asString(semesterrechnung.getRechnungsdatumNachrechnung()));
                    writer.append(separator);
                    writer.append(Integer.toString(semesterrechnung.getAnzahlWochenNachrechnung()));
                    writer.append(separator);
                    writer.append(semesterrechnung.getWochenbetragNachrechnung().toString());
                    writer.append(separator);
                    writer.append(semesterrechnung.getSchulgeldNachrechnung().toString());
                }
                writer.append('\n');
            }

            writer.flush();
            writer.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
