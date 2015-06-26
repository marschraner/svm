package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SchuelerSuchenModel extends Model {
    Schueler getSchueler();
    Angehoeriger getAngehoeriger();
    Adresse getAdresse();
    Anmeldung getAnmeldung();
    Calendar getStichtag();

    SchuelerSuchenResult suchen();

    void setNachname(String nachname);
    void setVorname(String vorname);
    void setStrasseHausnummer(String strasseHausnummer);
    void setPlz(String plz);
    void setOrt(String ort);
    void setFestnetz(String festnetz);
    void setNatel(String natel);
    void setEmail(String email);
    void setGeschlecht(Geschlecht geschlecht);
    void setGeburtsdatum(String geburtsdatum) throws SvmValidationException;
    void setGeburtsdatum(Calendar geburtsdatum);
    void setStichtag(String stichtag) throws SvmValidationException;
    void setStichtag(Calendar stichtag);
    void setAnmeldemonat(String anmeldemonat) throws SvmValidationException;
    void setAnmeldemonat(Calendar anmeldemonat);
    void setAbmeldemonat(String abmeldemonat) throws SvmValidationException;
    void setAbmeldemonat(Calendar abmeldedatum);
}
