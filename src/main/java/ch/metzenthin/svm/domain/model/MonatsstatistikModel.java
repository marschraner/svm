package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.SvmDbException;

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
    Calendar getMonatJahrInit();
    AnAbmeldungenDispensationenSelected getAnAbmeldungenDispensationen();
    AnAbmeldungenDispensationenSelected getAnAbmeldungenDispensationenInit();
    SchuelerSuchenResult suchen() throws SvmDbException;

    void setMonatJahr(String anAbmeldemonat) throws SvmValidationException;
    void setAnAbmeldungenDispensationen(AnAbmeldungenDispensationenSelected anAbmeldungen);
}
