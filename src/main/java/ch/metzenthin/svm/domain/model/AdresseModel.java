package ch.metzenthin.svm.domain.model;

/**
 * @author hans
 */
public interface AdresseModel extends Model {
    String getStrasse();
    Integer getHausnummer();
    Integer getPlz();
    String getOrt();
    String getFestnetz();

    void setStrasse(String strasse);
    void setHausnummer(Integer hausnummer);
    void setHausnummer(String hausnummer);
    void setPlz(Integer plz);
    void setPlz(String plz);
    void setOrt(String ort);
    void setFestnetz(String festnetz);
}
