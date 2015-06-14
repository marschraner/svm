package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Adresse;

import java.util.Calendar;

/**
 * @author hans
 */
public interface PersonModel extends AdresseModel {
    Anrede getAnrede();
    String getNachname();
    String getVorname();
    String getNatel();
    String getEmail();
    Calendar getGeburtsdatum();
    boolean isAdresseRequired();
    Adresse getAdresse();
    boolean isEmpty();

    void setAnrede(Anrede anrede) throws SvmValidationException;
    void setNachname(String nachname) throws SvmValidationException;
    void setVorname(String vorname) throws SvmValidationException;
    void setNatel(String natel) throws SvmValidationException;
    void setEmail(String email) throws SvmValidationException;
    void setGeburtsdatum(String geburtsdatum) throws SvmValidationException;
    void setGeburtsdatum(Calendar geburtsdatum);
}
