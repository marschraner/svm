package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface MonatsstatistikModel extends Model {

    enum AnAbmeldungenDispensationenSelected {
        ANMELDUNGEN,
        ABMELDUNGEN,
        DISPENSATIONEN
    }

    String MONAT_JAHR_DATE_FORMAT_STRING = "MM.yyyy";

    Calendar getMonatJahr();
    AnAbmeldungenDispensationenSelected getAnAbmeldungenDispensationen();
    SchuelerSuchenTableData suchen(SvmModel svmModel);

    void setMonatJahr(String anAbmeldemonat) throws SvmValidationException;
    void setAnAbmeldungenDispensationen(AnAbmeldungenDispensationenSelected anAbmeldungen);
}
