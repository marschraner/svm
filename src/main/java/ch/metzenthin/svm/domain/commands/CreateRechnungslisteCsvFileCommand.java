package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.io.*;
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
            out.write("PLZ/Ort");
            out.write(separator);
            out.write("R.Datum V");
            out.write(separator);
            out.write("Ermäss. V");
            out.write(separator);
            out.write("E.Grund V");
            out.write(separator);
            out.write("Zuschl. V");
            out.write(separator);
            out.write("Z.Grund V");
            out.write(separator);
            out.write("Anz. V");
            out.write(separator);
            out.write("Wochenb. V");
            out.write(separator);
            out.write("Schulgeld V");
            out.write(separator);
            out.write("R.Datum N");
            out.write(separator);
            out.write("Ermäss. N");
            out.write(separator);
            out.write("E.Grund N");
            out.write(separator);
            out.write("Zuschl. N");
            out.write(separator);
            out.write("Z.Grund N");
            out.write(separator);
            out.write("Anz. N");
            out.write(separator);
            out.write("Wochenb. N");
            out.write(separator);
            out.write("Schulgeld N");
            out.write(separator);
            out.write("D. 1. Zahlung");
            out.write(separator);
            out.write("1. Zahlung");
            out.write(separator);
            out.write("D. 2. Zahlung");
            out.write(separator);
            out.write("2. Zahlung");
            out.write(separator);
            out.write("D. 3. Zahlung");
            out.write(separator);
            out.write("3. Zahlung");
            out.write(separator);
            out.write("Restbetrag");
            out.write(separator);
            out.write("Bemerkungen");
            out.write(separator);
            out.write("1. Schüler");
            out.write(separator);
            out.write("2. Schüler");
            out.write(separator);
            out.write("3. Schüler");
            out.write(separator);
            out.write("4. Schüler");
            out.write(separator);
            out.write("5. Schüler");
            out.write(separator);
            out.write("6. Schüler");
            out.write('\n');

            // Daten
            for (Semesterrechnung semesterrechnung : semesterrechnungList) {
                Angehoeriger rechnungsempfaenger = semesterrechnung.getRechnungsempfaenger();
                out.write(rechnungsempfaenger.getAnrede().toString());
                out.write(separator);
                out.write(rechnungsempfaenger.getVorname());
                out.write(separator);
                out.write(rechnungsempfaenger.getNachname());
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getStrasseHausnummer());
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getPlzOrt());
                out.write(separator);
                if (semesterrechnung.getRechnungsdatumVorrechnung() != null) {
                    out.write(asString(semesterrechnung.getRechnungsdatumVorrechnung()));
                }
                out.write(separator);
                out.write(semesterrechnung.getErmaessigungVorrechnung().toString());
                out.write(separator);
                if (checkNotEmpty(semesterrechnung.getErmaessigungsgrundVorrechnung())) {
                    out.write(semesterrechnung.getErmaessigungsgrundVorrechnung());
                }
                out.write(separator);
                out.write(semesterrechnung.getZuschlagVorrechnung().toString());
                out.write(separator);
                if (checkNotEmpty(semesterrechnung.getZuschlagsgrundVorrechnung())) {
                    out.write(semesterrechnung.getZuschlagsgrundVorrechnung());
                }
                out.write(separator);
                out.write(Integer.toString(semesterrechnung.getAnzahlWochenVorrechnung()));
                out.write(separator);
                out.write(semesterrechnung.getWochenbetragVorrechnung().toString());
                out.write(separator);
                out.write(semesterrechnung.getSchulgeldVorrechnung().toString());
                out.write(separator);
                if (semesterrechnung.getRechnungsdatumNachrechnung() != null) {
                    out.write(asString(semesterrechnung.getRechnungsdatumNachrechnung()));
                }
                out.write(separator);
                out.write(semesterrechnung.getErmaessigungNachrechnung().toString());
                out.write(separator);
                if (checkNotEmpty(semesterrechnung.getErmaessigungsgrundNachrechnung())) {
                    out.write(semesterrechnung.getErmaessigungsgrundNachrechnung());
                }
                out.write(separator);
                out.write(semesterrechnung.getZuschlagNachrechnung().toString());
                out.write(separator);
                if (checkNotEmpty(semesterrechnung.getZuschlagsgrundNachrechnung())) {
                    out.write(semesterrechnung.getZuschlagsgrundNachrechnung());
                }
                out.write(separator);
                out.write(Integer.toString(semesterrechnung.getAnzahlWochenNachrechnung()));
                out.write(separator);
                out.write(semesterrechnung.getWochenbetragNachrechnung().toString());
                out.write(separator);
                out.write(semesterrechnung.getSchulgeldNachrechnung().toString());
                out.write(separator);
                if (semesterrechnung.getDatumZahlung1() != null) {
                    out.write(asString(semesterrechnung.getDatumZahlung1()));
                }
                out.write(separator);
                if (semesterrechnung.getBetragZahlung1() != null) {
                  out.write(semesterrechnung.getBetragZahlung1().toString());
                }
                out.write(separator);
                if (semesterrechnung.getDatumZahlung2() != null) {
                    out.write(asString(semesterrechnung.getDatumZahlung2()));
                }
                out.write(separator);
                if (semesterrechnung.getBetragZahlung2() != null) {
                    out.write(semesterrechnung.getBetragZahlung2().toString());
                }
                out.write(separator);
                if (semesterrechnung.getDatumZahlung3() != null) {
                    out.write(asString(semesterrechnung.getDatumZahlung3()));
                }
                out.write(separator);
                if (semesterrechnung.getBetragZahlung3() != null) {
                    out.write(semesterrechnung.getBetragZahlung3().toString());
                }
                out.write(separator);
                BigDecimal restbetrag = semesterrechnung.getRestbetrag();
                if (restbetrag != null) {
                    out.write(restbetrag.toString());
                }
                out.write(separator);
                if (checkNotEmpty(semesterrechnung.getBemerkungen())) {
                    out.write(semesterrechnung.getBemerkungen());
                }
                out.write(separator);
                String schuelerAsStr = rechnungsempfaenger.getSchuelerRechnungsempfaengerAsStr();
                schuelerAsStr = schuelerAsStr.replaceAll(",\\s",",");
                out.write(schuelerAsStr);
                out.write('\n');
            }

            out.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
