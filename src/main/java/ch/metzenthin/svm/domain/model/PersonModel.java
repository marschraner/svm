package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Adresse;

import java.util.Calendar;

/**
 * @author hans
 */
public interface PersonModel extends Model, AdresseModel {
    Anrede getAnrede();
    String getNachname();
    String getVorname();
    String getNatel();
    String getEmail();
    Calendar getGeburtsdatum();
    Adresse getAdresse();

    void setAnrede(Anrede anrede);
    void setNachname(String nachname);
    void setVorname(String vorname);
    void setNatel(String natel);
    void setEmail(String email);
    void setGeburtsdatum(String geburtsdatum);
    void setGeburtsdatum(Calendar geburtsdatum);
}
