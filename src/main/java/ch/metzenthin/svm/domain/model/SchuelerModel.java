package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;

/**
 * @author hans
 */
public interface SchuelerModel extends PersonModel {
    Geschlecht getGeschlecht();
    Calendar getAnmeldedatum();
    Calendar getAbmeldedatum();
    String getBemerkungen();
    Schueler getSchueler();

    void setGeschlecht(Geschlecht geschlecht);
    void setAnmeldedatum(String anmeldedatum) throws SvmValidationException;
    void setAnmeldedatum(Calendar anmeldedatum);
    void setAbmeldedatum(String abmeldedatum) throws SvmValidationException;
    void setAbmeldedatum(Calendar abmeldedatum);
    void setBemerkungen(String bemerkungen);
}
