package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Person;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateAdressenCsvFileCommand extends CreateListeCommand {

    // input
    private List<? extends Person> personList;
    private final File outputFile;

    public CreateAdressenCsvFileCommand(List<? extends Person> personList, File outputFile) {
        this.personList = personList;
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
            writer.append("StrasseNr");
            writer.append(separator);
            writer.append("PLZ");
            writer.append(separator);
            writer.append("Ort");
            writer.append('\n');

            // Daten
            for (Person person : personList) {
                String anrede = (person.getAnrede() == Anrede.KEINE ? "" : person.getAnrede().toString());
                writer.append(anrede);
                writer.append(separator);
                writer.append(person.getVorname());
                writer.append(separator);
                writer.append(person.getNachname());
                writer.append(separator);
                writer.append(person.getAdresse().getStrHausnummer());
                writer.append(separator);
                writer.append(person.getAdresse().getPlz());
                writer.append(separator);
                writer.append(person.getAdresse().getOrt());
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
