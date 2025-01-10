package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.domain.model.NachnameGratiskindFormatter;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class CreateRechnungslisteCsvFileCommand extends CreateListeCommand {

    // input
    private final List<? extends Semesterrechnung> semesterrechnungList;
    private final Semester previousSemester;
    private final File outputFile;

    public CreateRechnungslisteCsvFileCommand(List<? extends Semesterrechnung> semesterrechnungList, Semester previousSemester, File outputFile) {
        this.semesterrechnungList = semesterrechnungList;
        this.previousSemester = previousSemester;
        this.outputFile = outputFile;
    }

    @SuppressWarnings({"DuplicatedCode", "java:S3776", "java:S6541"})
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
            out.write("PLZ/Ort");
            out.write(separator);

            // Vorrechnung
            out.write("R.Datum V");
            out.write(separator);
            out.write("Anz. V");
            out.write(separator);
            out.write("Wochenb. V");
            out.write(separator);
            out.write("Schulg. V");
            out.write(separator);
            out.write("Ermäss. V");
            out.write(separator);
            out.write("Zuschl. V");
            out.write(separator);
            out.write("Stipend. V");
            out.write(separator);
            out.write("Rechnungsb. V");
            out.write(separator);
            out.write("1. Zahl. V");
            out.write(separator);
            out.write("D. 1. Zahl. V");
            out.write(separator);
            out.write("2. Zahl. V");
            out.write(separator);
            out.write("D. 2. Zahl. V");
            out.write(separator);
            out.write("3. Zahl. V");
            out.write(separator);
            out.write("D. 3. Zahl. V");
            out.write(separator);
            out.write("Restb. V");
            out.write(separator);

            // Nachrechnung
            out.write("R.Datum N");
            out.write(separator);
            out.write("Anz. N");
            out.write(separator);
            out.write("Wochenb. N");
            out.write(separator);
            out.write("Schulg. N");
            out.write(separator);
            out.write("Ermäss. N");
            out.write(separator);
            out.write("Zuschl. N");
            out.write(separator);
            out.write("Stipend. N");
            out.write(separator);
            out.write("Rechnungsb. N");
            out.write(separator);
            out.write("1. Zahl. N");
            out.write(separator);
            out.write("D. 1. Zahl. N");
            out.write(separator);
            out.write("2. Zahl. N");
            out.write(separator);
            out.write("D. 2. Zahl. N");
            out.write(separator);
            out.write("3. Zahl. N");
            out.write(separator);
            out.write("D. 3. Zahl. N");
            out.write(separator);
            out.write("Restb. N");
            out.write(separator);

            // Rest
            out.write("Diff. Schulg.");
            out.write(separator);
            out.write("Bemerkungen");
            out.write(separator);
            out.write("Schüler");
            out.write('\n');

            // Daten
            for (Semesterrechnung semesterrechnung : semesterrechnungList) {
                Angehoeriger rechnungsempfaenger = semesterrechnung.getRechnungsempfaenger();
                out.write(rechnungsempfaenger.getAnrede().toString());
                out.write(separator);
                out.write(rechnungsempfaenger.getVorname());
                out.write(separator);
                out.write(nachnameGratiskindFormatter.format(rechnungsempfaenger.getNachname()));
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getStrasseHausnummer());
                out.write(separator);
                out.write(rechnungsempfaenger.getAdresse().getPlzOrt());
                out.write(separator);

                // Vorrechnung
                boolean showVorrechnung = semesterrechnung.getRechnungsdatumVorrechnung() != null
                        || (semesterrechnung.getWochenbetragVorrechnung() != null && semesterrechnung.getWochenbetragVorrechnung().compareTo(BigDecimal.ZERO) != 0)
                        || (semesterrechnung.getErmaessigungVorrechnung() != null && semesterrechnung.getErmaessigungVorrechnung().compareTo(BigDecimal.ZERO) != 0)
                        || (semesterrechnung.getZuschlagVorrechnung() != null && semesterrechnung.getZuschlagVorrechnung().compareTo(BigDecimal.ZERO) != 0);
                if (semesterrechnung.getRechnungsdatumVorrechnung() != null) {
                    out.write(asString(semesterrechnung.getRechnungsdatumVorrechnung()));
                }
                out.write(separator);
                if (showVorrechnung) {
                    out.write(Integer.toString(semesterrechnung.getAnzahlWochenVorrechnung()));
                }
                out.write(separator);
                if (showVorrechnung) {
                    out.write(semesterrechnung.getWochenbetragVorrechnung().toString());
                }
                out.write(separator);
                BigDecimal schulgeldVorrechnung = semesterrechnung.getSchulgeldVorrechnung();
                if (showVorrechnung && schulgeldVorrechnung != null) {
                    out.write(schulgeldVorrechnung.toString());
                }
                out.write(separator);
                if (showVorrechnung && semesterrechnung.getErmaessigungVorrechnung() != null && semesterrechnung.getErmaessigungVorrechnung().compareTo(BigDecimal.ZERO) != 0) {
                    out.write(semesterrechnung.getErmaessigungVorrechnung().toString());
                }
                out.write(separator);
                if (showVorrechnung && semesterrechnung.getZuschlagVorrechnung() != null && semesterrechnung.getZuschlagVorrechnung().compareTo(BigDecimal.ZERO) != 0) {
                    out.write(semesterrechnung.getZuschlagVorrechnung().toString());
                }
                out.write(separator);
                BigDecimal ermaessigungStipendiumVorrechnung = semesterrechnung.getErmaessigungStipendiumVorrechnung();
                if (showVorrechnung && ermaessigungStipendiumVorrechnung != null && ermaessigungStipendiumVorrechnung.compareTo(BigDecimal.ZERO) != 0) {
                    out.write(ermaessigungStipendiumVorrechnung.toString());
                }
                out.write(separator);
                BigDecimal rechnungsbetragVorrechnung = semesterrechnung.getRechnungsbetragVorrechnung();
                if (showVorrechnung && rechnungsbetragVorrechnung != null) {
                    out.write(rechnungsbetragVorrechnung.toString());
                }
                out.write(separator);
                if (semesterrechnung.getBetragZahlung1Vorrechnung() != null) {
                    out.write(semesterrechnung.getBetragZahlung1Vorrechnung().toString());
                }
                out.write(separator);
                if (semesterrechnung.getDatumZahlung1Vorrechnung() != null) {
                    out.write(asString(semesterrechnung.getDatumZahlung1Vorrechnung()));
                }
                out.write(separator);
                if (semesterrechnung.getBetragZahlung2Vorrechnung() != null) {
                    out.write(semesterrechnung.getBetragZahlung2Vorrechnung().toString());
                }
                out.write(separator);
                if (semesterrechnung.getDatumZahlung2Vorrechnung() != null) {
                    out.write(asString(semesterrechnung.getDatumZahlung2Vorrechnung()));
                }
                out.write(separator);
                if (semesterrechnung.getBetragZahlung3Vorrechnung() != null) {
                    out.write(semesterrechnung.getBetragZahlung3Vorrechnung().toString());
                }
                out.write(separator);
                if (semesterrechnung.getDatumZahlung3Vorrechnung() != null) {
                    out.write(asString(semesterrechnung.getDatumZahlung3Vorrechnung()));
                }
                out.write(separator);
                BigDecimal restbetragVorrechnung = semesterrechnung.getRestbetragVorrechnung();
                if (restbetragVorrechnung != null) {
                    out.write(restbetragVorrechnung.toString());
                }
                out.write(separator);

                // Nachrechnung
                if (semesterrechnung.getRechnungsdatumNachrechnung() != null) {
                    out.write(asString(semesterrechnung.getRechnungsdatumNachrechnung()));
                }
                out.write(separator);
                out.write(Integer.toString(semesterrechnung.getAnzahlWochenNachrechnung()));
                out.write(separator);
                out.write(semesterrechnung.getWochenbetragNachrechnung().toString());
                out.write(separator);
                BigDecimal schulgeldNachrechnung = semesterrechnung.getSchulgeldNachrechnung();
                if (schulgeldNachrechnung != null) {
                    out.write(schulgeldNachrechnung.toString());
                }
                out.write(separator);
                if (semesterrechnung.getErmaessigungNachrechnung() != null && semesterrechnung.getErmaessigungNachrechnung().compareTo(BigDecimal.ZERO) != 0) {
                    out.write(semesterrechnung.getErmaessigungNachrechnung().toString());
                }
                out.write(separator);
                if (semesterrechnung.getZuschlagNachrechnung() != null && semesterrechnung.getZuschlagNachrechnung().compareTo(BigDecimal.ZERO) != 0) {
                    out.write(semesterrechnung.getZuschlagNachrechnung().toString());
                }
                out.write(separator);
                BigDecimal ermaessigungStipendiumNachrechnung = semesterrechnung.getErmaessigungStipendiumNachrechnung();
                if (ermaessigungStipendiumNachrechnung != null && ermaessigungStipendiumNachrechnung.compareTo(BigDecimal.ZERO) != 0) {
                    out.write(ermaessigungStipendiumNachrechnung.toString());
                }
                out.write(separator);
                BigDecimal rechnungsbetragNachrechnung = semesterrechnung.getRechnungsbetragNachrechnung();
                if (rechnungsbetragNachrechnung != null) {
                    out.write(rechnungsbetragNachrechnung.toString());
                }
                out.write(separator);
                if (semesterrechnung.getBetragZahlung1Nachrechnung() != null) {
                    out.write(semesterrechnung.getBetragZahlung1Nachrechnung().toString());
                }
                out.write(separator);
                if (semesterrechnung.getDatumZahlung1Nachrechnung() != null) {
                    out.write(asString(semesterrechnung.getDatumZahlung1Nachrechnung()));
                }
                out.write(separator);
                if (semesterrechnung.getBetragZahlung2Nachrechnung() != null) {
                    out.write(semesterrechnung.getBetragZahlung2Nachrechnung().toString());
                }
                out.write(separator);
                if (semesterrechnung.getDatumZahlung2Nachrechnung() != null) {
                    out.write(asString(semesterrechnung.getDatumZahlung2Nachrechnung()));
                }
                out.write(separator);
                if (semesterrechnung.getBetragZahlung3Nachrechnung() != null) {
                    out.write(semesterrechnung.getBetragZahlung3Nachrechnung().toString());
                }
                out.write(separator);
                if (semesterrechnung.getDatumZahlung3Nachrechnung() != null) {
                    out.write(asString(semesterrechnung.getDatumZahlung3Nachrechnung()));
                }
                out.write(separator);
                BigDecimal restbetragNachrechnung = semesterrechnung.getRestbetragNachrechnung();
                if (restbetragNachrechnung != null) {
                    out.write(restbetragNachrechnung.toString());
                }
                out.write(separator);

                // Rest
                BigDecimal differenzSchulgeld = semesterrechnung.getDifferenzSchulgeld();
                if (differenzSchulgeld != null) {
                    out.write(differenzSchulgeld.toString());
                }
                out.write(separator);
                if (checkNotEmpty(semesterrechnung.getBemerkungen())) {
                    out.write(semesterrechnung.getBemerkungenLineBreaksReplacedByCommaOrPeriod().replace(";", ","));
                }
                out.write(separator);
                String schuelerAsStr = semesterrechnung.getAktiveSchuelerRechnungsempfaengerAsStr(previousSemester);
                out.write(schuelerAsStr);
                out.write('\n');
            }

            out.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new SvmRuntimeException("Fehler beim Erstellen der csv-Datei", e);
        }

    }
}
