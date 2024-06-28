package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.domain.model.NachnameGratiskindFormatter;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CreateMahnungenSerienbriefCsvFileCommand extends CreateListeCommand {

    // input
    private final List<? extends Semesterrechnung> semesterrechnungList;
    private final Rechnungstyp rechnungstyp;
    private final File outputFile;

    public CreateMahnungenSerienbriefCsvFileCommand(List<? extends Semesterrechnung> semesterrechnungList, Rechnungstyp rechnungstyp, File outputFile) {
        this.semesterrechnungList = semesterrechnungList;
        this.rechnungstyp = rechnungstyp;
        this.outputFile = outputFile;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void execute() {

        char separator = ';';

        NachnameGratiskindFormatter nachnameGratiskindFormatter = new NachnameGratiskindFormatter();

        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.ISO_8859_1));

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
                BigDecimal restbetrag;
                if (rechnungstyp == Rechnungstyp.VORRECHNUNG) {
                    if (semesterrechnung.getRechnungsdatumVorrechnung() == null) {
                        continue;
                    }
                    restbetrag = semesterrechnung.getRestbetragVorrechnung();
                } else {
                    if (semesterrechnung.getRechnungsdatumNachrechnung() == null) {
                        continue;
                    }
                    restbetrag = semesterrechnung.getRestbetragNachrechnung();
                }
                if (restbetrag == null || restbetrag.compareTo(BigDecimal.ZERO) == 0) {
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
                out.write(restbetrag.toString());
                out.write('\n');
            }

            out.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
