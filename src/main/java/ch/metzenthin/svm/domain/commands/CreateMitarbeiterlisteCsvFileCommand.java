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

    public CreateMitarbeiterlisteCsvFileCommand(List<? extends Mitarbeiter> mitarbeiterList, File outputFile) {
        this.mitarbeiterList = mitarbeiterList;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() {

        Character separator = ';';

        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "8859_1"));

            // Header
            out.write("Nachname");
            out.write(separator);
            out.write("Vorname");
            out.write(separator);
            out.write("Strasse/Nr.");
            out.write(separator);
            out.write("PLZ");
            out.write(separator);
            out.write("Ort");
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
                out.write(mitarbeiter.getNachname());
                out.write(separator);
                out.write(mitarbeiter.getVorname());
                out.write(separator);
                if (mitarbeiter.getAdresse() != null) {
                    out.write(mitarbeiter.getAdresse().getStrHausnummer());
                }
                out.write(separator);
                if (mitarbeiter.getAdresse() != null) {
                    out.write(mitarbeiter.getAdresse().getPlz());
                }
                out.write(separator);
                if (mitarbeiter.getAdresse() != null) {
                    out.write(mitarbeiter.getAdresse().getOrt());
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
                    out.write(mitarbeiter.getEmail());
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
                    out.write(mitarbeiter.getVertretungsmoeglichkeiten());
                }
                out.write(separator);
                if (mitarbeiter.getBemerkungen() != null) {
                    out.write(mitarbeiter.getBemerkungen());
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
