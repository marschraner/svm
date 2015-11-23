package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.domain.model.NachnameGratiskindFormatter;
import ch.metzenthin.svm.persistence.entities.Person;

import java.io.*;
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

        NachnameGratiskindFormatter nachnameGratiskindFormatter = new NachnameGratiskindFormatter();

        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "8859_1"));

            // Header
            out.write("Anrede");
            out.write(separator);
            out.write("Vorname");
            out.write(separator);
            out.write("Nachname");
            out.write(separator);
            out.write("Strasse/Nr.");
            out.write(separator);
            out.write("PLZ");
            out.write(separator);
            out.write("Ort");
            out.write('\n');

            // Daten
            for (Person person : personList) {
                if (person.getAdresse() == null) {
                    continue;
                }
                String anrede = (person.getAnrede() == Anrede.KEINE ? "" : person.getAnrede().toString());
                out.write(anrede);
                out.write(separator);
                out.write(person.getVorname());
                out.write(separator);
                out.write(nachnameGratiskindFormatter.format(person.getNachname()));
                out.write(separator);
                out.write(person.getAdresse().getStrasseHausnummer());
                out.write(separator);
                out.write(person.getAdresse().getPlz());
                out.write(separator);
                out.write(person.getAdresse().getOrt());
                out.write('\n');
            }

            out.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
