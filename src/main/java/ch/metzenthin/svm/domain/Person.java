package ch.metzenthin.svm.domain;

/**
 * @author hans
 */
public interface Person extends Adresse {
    String getNachname();
    String getVorname();

    void setNachname(String text);
    void setVorname(String text);
}
