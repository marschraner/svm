package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
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
    Schueler getSchuelerOrigin();
    Anmeldung getAnmeldung();

    void setGeschlecht(Geschlecht geschlecht) throws SvmRequiredException;
    void setAnmeldedatum(String anmeldedatum) throws SvmValidationException;
    void setAbmeldedatum(String abmeldedatum) throws SvmValidationException;
    void setBemerkungen(String bemerkungen) throws SvmValidationException;

    void setSchueler(Schueler schueler);
}
