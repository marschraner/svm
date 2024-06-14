package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

import java.io.File;

/**
 * @author Martin Schraner
 */
public interface AuswahlRechnungsdateienModel extends Model {

    File getSerienbriefCsvDatei();

    void setSerienbriefCsvDatei(File serienbriefCsvDatei) throws SvmValidationException;

    File getAusSerienbriefGenerierteRechnungenPdfDatei();

    void setAusSerienbriefGenerierteRechnungenPdfDatei(File ausSerienbriefGenerierteRechnungenPdfDatei) throws SvmValidationException;

    void rechnungsdateienEinlesen();
}
