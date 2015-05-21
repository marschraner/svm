package ch.metzenthin.svm.domain.model;

/**
 * @author hans
 */
public interface AdresseModel extends Model {
    String getStrasse();
    String getHausnummer();
    String getPlz();
    String getOrt();
    String getFestnetz();

    void setStrasse(String strasse);
    void setHausnummer(String hausnummer);
    void setPlz(String plz);
    void setOrt(String ort);
    void setFestnetz(String festnetz);
}
