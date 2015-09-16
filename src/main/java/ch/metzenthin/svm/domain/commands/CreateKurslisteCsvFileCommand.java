package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Kurs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class CreateKurslisteCsvFileCommand extends CreateListeCommand {

    // input
    private List<? extends Kurs> kursList;
    private final File outputFile;

    public CreateKurslisteCsvFileCommand(List<? extends Kurs> kursList, File outputFile) {
        this.kursList = kursList;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {

        Character separator = ';';

        try {
            FileWriter writer = new FileWriter(outputFile);

            // Header
            writer.append("Kurstyp");
            writer.append(separator);
            writer.append("Alter");
            writer.append(separator);
            writer.append("Stufe");
            writer.append(separator);
            writer.append("Tag");
            writer.append(separator);
            writer.append("Von");
            writer.append(separator);
            writer.append("Bis");
            writer.append(separator);
            writer.append("Ort");
            writer.append(separator);
            writer.append("Leitung");
            writer.append(separator);
            writer.append("Bemerkungen");
            writer.append('\n');

            // Daten
            for (Kurs kurs : kursList) {
                writer.append(kurs.getKurstyp().getBezeichnung());
                writer.append(separator);
                writer.append(kurs.getAltersbereich());
                writer.append(separator);
                writer.append(kurs.getStufe());
                writer.append(separator);
                writer.append(kurs.getWochentag().toString());
                writer.append(separator);
                writer.append(kurs.getZeitBeginn().toString());
                writer.append(separator);
                writer.append(kurs.getZeitEnde().toString());
                writer.append(separator);
                writer.append(kurs.getKursort().getBezeichnung());
                writer.append(separator);
                writer.append(kurs.getMitarbeitersShortAsStr());
                writer.append(separator);
                if (checkNotEmpty(kurs.getBemerkungen())) {
                    writer.append(kurs.getBemerkungen());
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
