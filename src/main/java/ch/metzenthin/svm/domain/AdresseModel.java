package ch.metzenthin.svm.domain;

/**
 * @author hans
 */
public interface AdresseModel extends Model {
    String getStrasse();
    Integer getPlz();
    String getOrt();

    void setStrasse(String strasse);
    void setPlz(Integer plz);
    void setPlz(String plz);
    void setOrt(String ort);
}
