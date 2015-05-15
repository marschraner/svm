package ch.metzenthin.svm.domain;

/**
 * @author hans
 */
public interface PersonModel extends Model, AdresseModel {
    String getNachname();
    String getVorname();

    void setNachname(String nachname);
    void setVorname(String vorname);
}
