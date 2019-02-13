package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.io.*;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
public class CreateMitarbeiterlisteCsvFileCommand extends CreateListeCommand {

    // input
    private List<? extends Mitarbeiter> mitarbeiterList;
    private final File outputFile;
    private boolean nameEinspaltig;

    public CreateMitarbeiterlisteCsvFileCommand(List<? extends Mitarbeiter> mitarbeiterList, File outputFile, boolean nameEinspaltig) {
        this.mitarbeiterList = mitarbeiterList;
        this.outputFile = outputFile;
        this.nameEinspaltig = nameEinspaltig;
    }

    @Override
    public void execute() {

        char separator = ';';

        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "8859_1"));

            // Header
            if (nameEinspaltig) {
                out.write("Name");
            } else {
                out.write("Nachname");
                out.write(separator);
                out.write("Vorname");
            }
            out.write(separator);
            out.write("Strasse/Nr.");
            out.write(separator);
            out.write("PLZ/Ort");
            out.write(separator);
            out.write("Festnetz");
            out.write(separator);
            out.write("Natel");
            out.write(separator);
            out.write("E-Mail");
            out.write(separator);
            out.write("Geburtsdatum");
            out.write(separator);
            out.write("AHV-Nummer");
            out.write(separator);
            out.write("Lehrkraft");
            out.write(separator);
            out.write("Codes");
            out.write(separator);
            out.write("Vertretungsmöglichkeiten");
            out.write(separator);
            out.write("Bemerkungen");
            out.write(separator);
            out.write("aktiv");
            out.write('\n');

            // Daten
            for (Mitarbeiter mitarbeiter : mitarbeiterList) {
                if (nameEinspaltig) {
                    out.write(mitarbeiter.getNachname() + " " + mitarbeiter.getVorname());
                } else {
                    out.write(mitarbeiter.getNachname());
                    out.write(separator);
                    out.write(mitarbeiter.getVorname());
                }
                out.write(separator);
                if (mitarbeiter.getAdresse() != null) {
                    out.write(mitarbeiter.getAdresse().getStrHausnummer());
                }
                out.write(separator);
                if (mitarbeiter.getAdresse() != null) {
                    out.write(mitarbeiter.getAdresse().getPlzOrt());
                }
                out.write(separator);
                if (mitarbeiter.getFestnetz() != null) {
                    out.write(mitarbeiter.getFestnetz());
                }
                out.write(separator);
                if (mitarbeiter.getNatel() != null) {
                    out.write(mitarbeiter.getNatel());
                }
                out.write(separator);
                if (mitarbeiter.getEmail() != null) {
                    out.write(mitarbeiter.getEmail().replace(";", ","));
                }
                out.write(separator);
                if (mitarbeiter.getGeburtsdatum() != null) {
                    out.write(asString(mitarbeiter.getGeburtsdatum()));
                }
                out.write(separator);
                if (mitarbeiter.getAhvNummer() != null) {
                    out.write(mitarbeiter.getAhvNummer());
                }
                out.write(separator);
                out.write(mitarbeiter.getLehrkraft() ? "ja" : "nein");
                out.write(separator);
                out.write(mitarbeiter.getMitarbeiterCodesAsStr());
                out.write(separator);
                if (mitarbeiter.getVertretungsmoeglichkeiten() != null) {
                    out.write(mitarbeiter.getVertretungsmoeglichkeitenLineBreaksReplacedByCommaOrPeriod().replace(";", ","));
                }
                out.write(separator);
                if (mitarbeiter.getBemerkungen() != null) {
                    out.write(mitarbeiter.getBemerkungenLineBreaksReplacedByCommaOrPeriod().replace(";", ","));
                }
                out.write(separator);
                out.write(mitarbeiter.getAktiv() ? "ja" : "nein");
                out.write('\n');
            }

            out.close();

            result = Result.LISTE_ERFOLGREICH_ERSTELLT;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
