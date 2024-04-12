package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.model.NachnameGratiskindFormatter;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class CreateRechnungenSerienbriefCsvFileCommand extends CreateListeCommand {

    // input
    private final List<? extends Semesterrechnung> semesterrechnungList;
    private final Semester previousSemester;
    private final Rechnungstyp rechnungstyp;
    private final File outputFile;

    public CreateRechnungenSerienbriefCsvFileCommand(List<? extends Semesterrechnung> semesterrechnungList, Semester previousSemester, Rechnungstyp rechnungstyp, File outputFile) {
        this.semesterrechnungList = semesterrechnungList;
        this.previousSemester = previousSemester;
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
            out.write("E-Mail");
            out.write(separator);
            out.write("Rechnungsdatum");
            out.write(separator);
            out.write("Bemerkungen");
            out.write(separator);
            out.write("Anzahl Wochen");
            out.write(separator);
            out.write("Wochenbetrag");
            out.write(separator);
            out.write("Schulgeld");
            out.write(separator);
            out.write("Ermässigung");
            out.write(separator);
            out.write("Zuschlag");
            out.write(separator);
            out.write("Stipendium");
            out.write(separator);
            out.write("Rechnungsbetrag");
            out.write(separator);
            out.write("Schüler");
            out.write('\n');

            // Daten
            for (Semesterrechnung semesterrechnung : semesterrechnungList) {
                if (rechnungstyp == Rechnungstyp.VORRECHNUNG) {
                    if (semesterrechnung.getRechnungsdatumVorrechnung() == null ||
                            semesterrechnung.getWochenbetragVorrechnung() == null ||
                            semesterrechnung.getRechnungsbetragVorrechnung() == null) {
                        continue;
                    }
                } else {
                    if (semesterrechnung.getRechnungsdatumNachrechnung() == null ||
                            semesterrechnung.getWochenbetragNachrechnung() == null ||
                            semesterrechnung.getRechnungsbetragNachrechnung() == null) {
                        continue;
                    }
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
                if (rechnungsempfaenger.getEmail() != null) {
                    out.write(rechnungsempfaenger.getEmail());
                }
                out.write(separator);
                if (rechnungstyp == Rechnungstyp.VORRECHNUNG) {
                    if (semesterrechnung.getRechnungsdatumVorrechnung() != null) {
                        out.write(asString(semesterrechnung.getRechnungsdatumVorrechnung()));
                    }
                    out.write(separator);
                    if (semesterrechnung.getBemerkungen() != null) {
                        out.write(semesterrechnung.getBemerkungenLineBreaksReplacedByCommaOrPeriod().replace(";", ","));
                    }
                    out.write(separator);
                    out.write(Integer.toString(semesterrechnung.getAnzahlWochenVorrechnung()));
                    out.write(separator);
                    out.write(semesterrechnung.getWochenbetragVorrechnung().toString());
                    out.write(separator);
                    BigDecimal schulgeldVorrechnung = semesterrechnung.getSchulgeldVorrechnung();
                    if (schulgeldVorrechnung != null) {
                        out.write(schulgeldVorrechnung.toString());
                    }
                    out.write(separator);
                    if (semesterrechnung.getErmaessigungVorrechnung() != null && semesterrechnung.getErmaessigungVorrechnung().compareTo(BigDecimal.ZERO) != 0) {
                        out.write(semesterrechnung.getErmaessigungVorrechnung().toString());
                    }
                    out.write(separator);
                    if (semesterrechnung.getZuschlagVorrechnung() != null && semesterrechnung.getZuschlagVorrechnung().compareTo(BigDecimal.ZERO) != 0) {
                        out.write(semesterrechnung.getZuschlagVorrechnung().toString());
                    }
                    out.write(separator);
                    BigDecimal ermaessigungStipendiumVorrechnung = semesterrechnung.getErmaessigungStipendiumVorrechnung();
                    if (ermaessigungStipendiumVorrechnung != null && ermaessigungStipendiumVorrechnung.compareTo(BigDecimal.ZERO) != 0) {
                        out.write(ermaessigungStipendiumVorrechnung.toString());
                    }
                    out.write(separator);
                    BigDecimal rechnungsbetragVorrechnung = semesterrechnung.getRechnungsbetragVorrechnung();
                    if (rechnungsbetragVorrechnung != null) {
                        out.write(rechnungsbetragVorrechnung.toString());
                    }
                } else {
                    if (semesterrechnung.getRechnungsdatumNachrechnung() != null) {
                        out.write(asString(semesterrechnung.getRechnungsdatumNachrechnung()));
                    }
                    out.write(separator);
                    if (semesterrechnung.getBemerkungen() != null) {
                        out.write(semesterrechnung.getBemerkungenLineBreaksReplacedByCommaOrPeriod().replace(";", ","));
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
                }
                out.write(separator);
                out.write(semesterrechnung.getAktiveSchuelerRechnungsempfaengerAsStr(previousSemester));
                out.write('\n');
            }

            out.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
