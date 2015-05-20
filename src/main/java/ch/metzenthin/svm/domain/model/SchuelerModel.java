package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;

/**
 * @author hans
 */
public interface SchuelerModel extends PersonModel {
    Geschlecht getGeschlecht();
    Calendar getAnmeldedatum();
    Calendar getAbmeldedatum();
    Calendar getDispensationsbeginn();
    Calendar getDispensationsende();
    String getBemerkungen();
    Schueler getSchueler();

    void setGeschlecht(Geschlecht geschlecht);
    void setAnmeldedatum(String anmeldedatum);
    void setAnmeldedatum(Calendar anmeldedatum);
    void setAbmeldedatum(String abmeldedatum);
    void setAbmeldedatum(Calendar abmeldedatum);
    void setDispensationsbeginn(String dispensationsbeginn);
    void setDispensationsbeginn(Calendar dispensationsbeginn);
    void setDispensationsende(String dispensationsende);
    void setDispensationsende(Calendar dispensationsende);
    void setBemerkungen(String bemerkungen);

    void save();
}
