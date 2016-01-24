package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.NachnameGratiskindFormatter;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.io.*;
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
            out.write("Strasse");
            out.write(separator);
            out.write("PLZ");
            out.write(separator);
            out.write("Ort");
            out.write(separator);
            out.write("Restbetrag");
            out.write('\n');

            // Daten
            for (Semesterrechnung semesterrechnung : semesterrechnungList) {
                if (semesterrechnung.getRestbetragNachrechnung() == null) {
                    continue;
                }
                Angehoeriger rechnungsempfaenger = semesterrechnung.getRechnungsempfaenger();
                String anrede = rechnungsempfaenger.getAnrede().toString();
                out.write(anrede);
                out.write(separator);
                out.write(rechnungsempfaenger.getVorname());
                out.write(separator);
                out.write(nachnameGratiskindFormatter.format(rechnungsempfaenger.getNachname()));
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getStrHausnummer());
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getPlz());
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getOrt());
                out.write(separator);
                out.write(semesterrechnung.getRestbetragNachrechnung().toString());
                out.write('\n');
            }

            out.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
