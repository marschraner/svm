package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateMahnungenSerienbriefCsvFileCommand extends CreateListeCommand {

    // input
    private List<? extends Semesterrechnung> semesterrechnungList;
    private final File outputFile;

    public CreateMahnungenSerienbriefCsvFileCommand(List<? extends Semesterrechnung> semesterrechnungList, File outputFile) {
        this.semesterrechnungList = semesterrechnungList;
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
            writer.append("Restbetrag");
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
                writer.append(semesterrechnung.getRestbetrag().toString());
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
