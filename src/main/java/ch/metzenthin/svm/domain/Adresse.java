package ch.metzenthin.svm.domain;

/**
 * @author hans
 */
public interface Adresse {
    String getStrasse();
    String getPlz();
    String getOrt();

    void setStrasse(String text);
    void setPlz(String text);
    void setOrt(String text);
}
