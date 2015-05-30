package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

/**
 * @author hans
 */
public interface AdresseModel extends Model {
    String getStrasse();
    String getHausnummer();
    String getPlz();
    String getOrt();
    String getFestnetz();

    void setStrasse(String strasse) throws SvmValidationException;
    void setHausnummer(String hausnummer);
    void setPlz(String plz) throws SvmValidationException;
    void setOrt(String ort) throws SvmValidationException;
    void setFestnetz(String festnetz);
}
