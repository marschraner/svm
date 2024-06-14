package ch.metzenthin.svm.domain.commands;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class ExtractPdfRechnungenCommand implements Command {

    // https://stackoverflow.com/questions/40221977/pdfbox-split-pdf-in-multi-files-with-different-page-ranges-and-filenames
    // https://mvnrepository.com/artifact/org.apache.pdfbox/pdfbox


    public enum Result {
        DATEI_EXISTIERT_NICHT_ODER_NICHT_LESBAR,
        FEHLER_BEIM_EINLESEN,
        DATEI_LEER,
        PDF_DATEI_ERFOLGREICH_EXTRAHIERT
    }

    // input
    private final File pdfRechnungen;

    // output
    private Result result;
    private final List<File> pdfRechnung = new ArrayList<>();


    public ExtractPdfRechnungenCommand(File pdfRechnungen) {
        this.pdfRechnungen = pdfRechnungen;
    }

    @Override
    public void execute() {

        result = Result.PDF_DATEI_ERFOLGREICH_EXTRAHIERT;
    }

    public Result getResult() {
        return result;
    }

    public List<File> getPdfRechnung() {
        return pdfRechnung;
    }
}
