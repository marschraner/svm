package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

/**
 * @author hans
 */
public interface AdresseModel extends Model {

  String getStrasseHausnummer();

  String getPlz();

  String getOrt();

  String getFestnetz();

  void setStrasseHausnummer(String strasseHausnummer) throws SvmValidationException;

  void setPlz(String plz) throws SvmValidationException;

  void setOrt(String ort) throws SvmValidationException;

  void setFestnetz(String festnetz) throws SvmValidationException;
}
