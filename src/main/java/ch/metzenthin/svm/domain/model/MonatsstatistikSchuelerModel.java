package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface MonatsstatistikSchuelerModel extends Model {

    enum AnAbmeldungenDispensationenSelected {
        ANMELDUNGEN_KINDERTHEATER,
        ABMELDUNGEN_KINDERTHEATER,
        ANMELDUNGEN_KURSE,
        ABMELDUNGEN_KURSE,
        DISPENSATIONEN
    }

    String MONAT_JAHR_DATE_FORMAT_STRING = "MM.yyyy";

    Calendar getMonatJahr();

    AnAbmeldungenDispensationenSelected getAnAbmeldungenDispensationen();

    void setMonatJahr(String anAbmeldemonat) throws SvmValidationException;

    void setAnAbmeldungenDispensationen(AnAbmeldungenDispensationenSelected anAbmeldungen);

    SchuelerSuchenTableData suchen(SvmModel svmModel);
}
