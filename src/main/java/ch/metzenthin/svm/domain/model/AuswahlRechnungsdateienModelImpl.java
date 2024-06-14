package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.RechnungSerienbrief;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.ReadRechnungenSerienbriefCsvCommand;

import java.io.File;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class AuswahlRechnungsdateienModelImpl extends AbstractModel implements AuswahlRechnungsdateienModel {

    private File serienbriefCsvDatei;
    private File ausSerienbriefGenerierteRechnungenPdfDatei;

    @Override
    public File getSerienbriefCsvDatei() {
        return serienbriefCsvDatei;
    }

    @Override
    public void setSerienbriefCsvDatei(File serienbriefCsvDatei) throws SvmValidationException {
        if (serienbriefCsvDatei == null) {
            throw new SvmValidationException(4001, "Es muss eine Datei ausgewählt werden!", Field.SERIENBRIEF_CSV_DATEI);
        }
        if (!serienbriefCsvDatei.getName().endsWith(".csv")) {
            throw new SvmValidationException(4002, "Es muss eine csv-Datei ausgewählt werden!", Field.SERIENBRIEF_CSV_DATEI);
        }
        File oldValue = this.serienbriefCsvDatei;
        this.serienbriefCsvDatei = serienbriefCsvDatei;
        firePropertyChange(Field.SERIENBRIEF_CSV_DATEI, oldValue, this.serienbriefCsvDatei);
    }

    @Override
    public File getAusSerienbriefGenerierteRechnungenPdfDatei() {
        return ausSerienbriefGenerierteRechnungenPdfDatei;
    }

    @Override
    public void setAusSerienbriefGenerierteRechnungenPdfDatei(File ausSerienbriefGenerierteRechnungenPdfDatei)
            throws SvmValidationException {
        if (ausSerienbriefGenerierteRechnungenPdfDatei == null) {
            throw new SvmValidationException(
                    4001, "Es muss eine Datei ausgewählt werden!", Field.AUS_SERIENBRIEF_GENERIERTE_RECHNUNGEN_PDF_DATEI);
        }
        if (!ausSerienbriefGenerierteRechnungenPdfDatei.getName().endsWith(".pdf")) {
            throw new SvmValidationException(
                    4003, "Es muss eine pdf-Datei ausgewählt werden!", Field.AUS_SERIENBRIEF_GENERIERTE_RECHNUNGEN_PDF_DATEI);
        }
        File oldValue = this.ausSerienbriefGenerierteRechnungenPdfDatei;
        this.ausSerienbriefGenerierteRechnungenPdfDatei = ausSerienbriefGenerierteRechnungenPdfDatei;
        firePropertyChange(Field.AUS_SERIENBRIEF_GENERIERTE_RECHNUNGEN_PDF_DATEI, oldValue,
                this.ausSerienbriefGenerierteRechnungenPdfDatei);
    }

    @Override
    public void rechnungsdateienEinlesen() {
        ReadRechnungenSerienbriefCsvCommand readRechnungenSerienbriefCsvCommand =
                new ReadRechnungenSerienbriefCsvCommand(serienbriefCsvDatei);
        readRechnungenSerienbriefCsvCommand.execute();
        if (readRechnungenSerienbriefCsvCommand.getResult() !=
                ReadRechnungenSerienbriefCsvCommand.Result.ERFOLGREICH_EINGELESEN) {
            //TODO Fehlerbehandlung
            System.out.println(readRechnungenSerienbriefCsvCommand.getResult());
            System.out.println(readRechnungenSerienbriefCsvCommand.getErrLine());
        }
        List<RechnungSerienbrief> rechnungenSerienbrief =
                readRechnungenSerienbriefCsvCommand.getRechnungenSerienbrief();



    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
