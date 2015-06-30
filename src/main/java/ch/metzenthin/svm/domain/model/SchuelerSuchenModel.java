package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SchuelerSuchenModel extends Model {

    enum RolleSelected {
        SCHUELER,
        ELTERN,
        RECHNUNGSEMPFAENGER,
        ALLE
    }

    enum AnmeldestatusSelected {
        ANGEMELDET,
        ABGEMELDET,
        ALLE
    }

    enum DispensationSelected {
        DISPENSIERT,
        NICHT_DISPENSIERT,
        ALLE
    }

    enum GeschlechtSelected {
        WEIBLICH,
        MAENNLICH,
        ALLE
    }

    enum AnAbmeldungenSelected {
        ANMELDUNGEN,
        ABMELDUNGEN
    }

    String getNachname();
    String getVorname();
    String getStrasseHausnummer();
    String getPlz();
    String getOrt();
    String getFestnetz();
    String getNatel();
    String getEmail();
    Calendar getGeburtsdatum();
    RolleSelected getRolle();
    AnmeldestatusSelected getAnmeldestatus();
    DispensationSelected getDispensation();
    GeschlechtSelected getGeschlecht();
    Calendar getStichtag();
    Calendar getAnAbmeldemonat();
    AnAbmeldungenSelected getAnAbmeldungen();
    boolean isStammdatenBeruecksichtigen();
    boolean isKursBeruecksichtigen();
    boolean isCodesBeruecksichtigen();
    boolean isAnAbmeldestatistikBeruecksichtigen();

    SchuelerSuchenResult suchen();

    void setNachname(String nachname) throws SvmValidationException;
    void setVorname(String vorname) throws SvmValidationException;
    void setStrasseHausnummer(String strasseHausnummer) throws SvmValidationException;
    void setPlz(String plz) throws SvmValidationException;
    void setOrt(String ort) throws SvmValidationException;
    void setFestnetz(String festnetz) throws SvmValidationException;
    void setNatel(String natel) throws SvmValidationException;
    void setEmail(String email) throws SvmValidationException;
    void setGeburtsdatum(String geburtsdatum) throws SvmValidationException;
    void setGeburtsdatum(Calendar geburtsdatum);
    void setRolle(RolleSelected rolle);
    void setAnmeldestatus(AnmeldestatusSelected anmeldestatus);
    void setDispensation(DispensationSelected dispensation);
    void setGeschlecht(GeschlechtSelected geschlecht);
    void setStichtag(String stichtag) throws SvmValidationException;
    void setStichtag(Calendar stichtag);
    void setAnAbmeldemonat(String anAbmeldemonat) throws SvmValidationException;
    void setAnAbmeldemonat(Calendar anAbmeldemonat);
    void setAnAbmeldungen(AnAbmeldungenSelected anAbmeldungen);
    void setStammdatenBeruecksichtigen(boolean stammdatenBeruecksichtigen);
    void setKursBeruecksichtigen(boolean kursBeruecksichtigen);
    void setCodesBeruecksichtigen(boolean codesBeruecksichtigen);
    void setAnAbmeldestatistikBeruecksichtigen(boolean anAbmeldestatistikBeruecksichtigen);
}
