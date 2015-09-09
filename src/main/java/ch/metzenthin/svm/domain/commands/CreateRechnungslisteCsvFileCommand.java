package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class CreateRechnungslisteCsvFileCommand extends CreateListeCommand {

    // input
    private List<? extends Semesterrechnung> semesterrechnungList;
    private final File outputFile;

    public CreateRechnungslisteCsvFileCommand(List<? extends Semesterrechnung> semesterrechnungList, File outputFile) {
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
            writer.append("R.Datum V");
            writer.append(separator);
            writer.append("Ermäss. V");
            writer.append(separator);
            writer.append("E.Grund V");
            writer.append(separator);
            writer.append("Zuschl. V");
            writer.append(separator);
            writer.append("Z.Grund V");
            writer.append(separator);
            writer.append("Anz. V");
            writer.append(separator);
            writer.append("Wochenb. V");
            writer.append(separator);
            writer.append("Schulgeld V");
            writer.append(separator);
            writer.append("R.Datum N");
            writer.append(separator);
            writer.append("Ermäss. N");
            writer.append(separator);
            writer.append("E.Grund N");
            writer.append(separator);
            writer.append("Zuschl. N");
            writer.append(separator);
            writer.append("Z.Grund N");
            writer.append(separator);
            writer.append("Anz. N");
            writer.append(separator);
            writer.append("Wochenb. N");
            writer.append(separator);
            writer.append("Schulgeld N");
            writer.append(separator);
            writer.append("D. 1. Zahlung");
            writer.append(separator);
            writer.append("1. Zahlung");
            writer.append(separator);
            writer.append("D. 2. Zahlung");
            writer.append(separator);
            writer.append("2. Zahlung");
            writer.append(separator);
            writer.append("D. 3. Zahlung");
            writer.append(separator);
            writer.append("3. Zahlung");
            writer.append(separator);
            writer.append("Restbetrag");
            writer.append(separator);
            writer.append("Bemerkungen");
            writer.append(separator);
            writer.append("1. Schüler");
            writer.append(separator);
            writer.append("2. Schüler");
            writer.append(separator);
            writer.append("3. Schüler");
            writer.append(separator);
            writer.append("4. Schüler");
            writer.append(separator);
            writer.append("5. Schüler");
            writer.append(separator);
            writer.append("6. Schüler");
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
                if (semesterrechnung.getRechnungsdatumVorrechnung() != null) {
                    writer.append(asString(semesterrechnung.getRechnungsdatumVorrechnung()));
                }
                writer.append(separator);
                writer.append(semesterrechnung.getErmaessigungVorrechnung().toString());
                writer.append(separator);
                if (checkNotEmpty(semesterrechnung.getErmaessigungsgrundVorrechnung())) {
                    writer.append(semesterrechnung.getErmaessigungsgrundVorrechnung());
                }
                writer.append(separator);
                writer.append(semesterrechnung.getZuschlagVorrechnung().toString());
                writer.append(separator);
                if (checkNotEmpty(semesterrechnung.getZuschlagsgrundVorrechnung())) {
                    writer.append(semesterrechnung.getZuschlagsgrundVorrechnung());
                }
                writer.append(separator);
                writer.append(Integer.toString(semesterrechnung.getAnzahlWochenVorrechnung()));
                writer.append(separator);
                writer.append(semesterrechnung.getWochenbetragVorrechnung().toString());
                writer.append(separator);
                writer.append(semesterrechnung.getSchulgeldVorrechnung().toString());
                writer.append(separator);
                if (semesterrechnung.getRechnungsdatumNachrechnung() != null) {
                    writer.append(asString(semesterrechnung.getRechnungsdatumNachrechnung()));
                }
                writer.append(separator);
                writer.append(semesterrechnung.getErmaessigungNachrechnung().toString());
                writer.append(separator);
                if (checkNotEmpty(semesterrechnung.getErmaessigungsgrundNachrechnung())) {
                    writer.append(semesterrechnung.getErmaessigungsgrundNachrechnung());
                }
                writer.append(separator);
                writer.append(semesterrechnung.getZuschlagNachrechnung().toString());
                writer.append(separator);
                if (checkNotEmpty(semesterrechnung.getZuschlagsgrundNachrechnung())) {
                    writer.append(semesterrechnung.getZuschlagsgrundNachrechnung());
                }
                writer.append(separator);
                writer.append(Integer.toString(semesterrechnung.getAnzahlWochenNachrechnung()));
                writer.append(separator);
                writer.append(semesterrechnung.getWochenbetragNachrechnung().toString());
                writer.append(separator);
                writer.append(semesterrechnung.getSchulgeldNachrechnung().toString());
                writer.append(separator);
                if (semesterrechnung.getDatumZahlung1() != null) {
                    writer.append(asString(semesterrechnung.getDatumZahlung1()));
                }
                writer.append(separator);
                if (semesterrechnung.getBetragZahlung1() != null) {
                  writer.append(semesterrechnung.getBetragZahlung1().toString());
                }
                writer.append(separator);
                if (semesterrechnung.getDatumZahlung2() != null) {
                    writer.append(asString(semesterrechnung.getDatumZahlung2()));
                }
                writer.append(separator);
                if (semesterrechnung.getBetragZahlung2() != null) {
                    writer.append(semesterrechnung.getBetragZahlung2().toString());
                }
                writer.append(separator);
                if (semesterrechnung.getDatumZahlung3() != null) {
                    writer.append(asString(semesterrechnung.getDatumZahlung3()));
                }
                writer.append(separator);
                if (semesterrechnung.getBetragZahlung3() != null) {
                    writer.append(semesterrechnung.getBetragZahlung3().toString());
                }
                writer.append(separator);
                BigDecimal restbetrag = semesterrechnung.getRestbetrag();
                if (restbetrag != null) {
                    writer.append(restbetrag.toString());
                }
                writer.append(separator);
                if (checkNotEmpty(semesterrechnung.getBemerkungen())) {
                    writer.append(semesterrechnung.getBemerkungen());
                }
                writer.append(separator);
                String schuelerAsStr = rechnungsempfaenger.getSchuelerRechnungsempfaengerAsStr();
                schuelerAsStr = schuelerAsStr.replaceAll(",\\s",",");
                writer.append(schuelerAsStr);
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
