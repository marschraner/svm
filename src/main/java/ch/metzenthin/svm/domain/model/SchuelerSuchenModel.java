package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SchuelerSuchenModel extends PersonModel {

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
