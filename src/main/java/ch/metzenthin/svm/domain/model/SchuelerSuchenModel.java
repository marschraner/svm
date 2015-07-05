package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.PersonSuchen;

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

    PersonSuchen getPerson();
    String getGeburtsdatumSuchperiode();
    Calendar getGeburtsdatumSuchperiodeBeginn();
    Calendar getGeburtsdatumSuchperiodeEnde();
    String getGeburtsdatumSuchperiodeDateFormatString();
    RolleSelected getRolle();
    AnmeldestatusSelected getAnmeldestatus();
    DispensationSelected getDispensation();
    GeschlechtSelected getGeschlecht();
    Calendar getStichtag();
    SchuelerSuchenResult suchen();

    void setGeburtsdatumSuchperiode(String geburtsdatumSuchperiode) throws SvmValidationException;
    void setRolle(RolleSelected rolle);
    void setAnmeldestatus(AnmeldestatusSelected anmeldestatus);
    void setDispensation(DispensationSelected dispensation);
    void setGeschlecht(GeschlechtSelected geschlecht);
    void setStichtag(String stichtag) throws SvmValidationException;

    void invalidateGeburtsdatumSuchperiode();
}
