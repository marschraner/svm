package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SemesterrechnungModel extends Model {

  SemesterrechnungCode SEMESTERRECHNUNG_CODE_ALLE = new SemesterrechnungCode();

  SemesterrechnungCode SEMESTERRECHNUNG_CODE_KEINER = new SemesterrechnungCode();

  SemesterrechnungCode getSemesterrechnungCode();

  Stipendium getStipendium();

  Boolean isGratiskinder();

  Calendar getRechnungsdatumVorrechnung();

  BigDecimal getErmaessigungVorrechnung();

  String getErmaessigungsgrundVorrechnung();

  BigDecimal getZuschlagVorrechnung();

  String getZuschlagsgrundVorrechnung();

  Integer getAnzahlWochenVorrechnung();

  BigDecimal getWochenbetragVorrechnung();

  Calendar getDatumZahlung1Vorrechnung();

  BigDecimal getBetragZahlung1Vorrechnung();

  Calendar getDatumZahlung2Vorrechnung();

  BigDecimal getBetragZahlung2Vorrechnung();

  Calendar getDatumZahlung3Vorrechnung();

  BigDecimal getBetragZahlung3Vorrechnung();

  Calendar getRechnungsdatumNachrechnung();

  BigDecimal getErmaessigungNachrechnung();

  String getErmaessigungsgrundNachrechnung();

  BigDecimal getZuschlagNachrechnung();

  String getZuschlagsgrundNachrechnung();

  Integer getAnzahlWochenNachrechnung();

  BigDecimal getWochenbetragNachrechnung();

  Calendar getDatumZahlung1Nachrechnung();

  BigDecimal getBetragZahlung1Nachrechnung();

  Calendar getDatumZahlung2Nachrechnung();

  BigDecimal getBetragZahlung2Nachrechnung();

  Calendar getDatumZahlung3Nachrechnung();

  BigDecimal getBetragZahlung3Nachrechnung();

  String getBemerkungen();

  void setSemesterrechnungCode(SemesterrechnungCode semesterrechnungCode);

  void setStipendium(Stipendium stipendium);

  void setGratiskinder(Boolean gratiskind);

  void setRechnungsdatumVorrechnung(String rechnungsdatumVorrechnung) throws SvmValidationException;

  void setErmaessigungVorrechnung(String ermaessigungVorrechnung) throws SvmValidationException;

  void setErmaessigungsgrundVorrechnung(String ermaessigungsgrundVorrechnung)
      throws SvmValidationException;

  void setZuschlagVorrechnung(String zuschlagVorrechnung) throws SvmValidationException;

  void setZuschlagsgrundVorrechnung(String zuschlagsgrundVorrechnung) throws SvmValidationException;

  void setAnzahlWochenVorrechnung(String anzahlWochenVorrechnung) throws SvmValidationException;

  void setWochenbetragVorrechnung(String wochenbetragVorrechnung) throws SvmValidationException;

  void setDatumZahlung1Vorrechnung(String datumZahlung1Vorrechnung) throws SvmValidationException;

  void setBetragZahlung1Vorrechnung(String betragZahlung1Vorrechnung) throws SvmValidationException;

  void setDatumZahlung2Vorrechnung(String datumZahlung2Vorrechnung) throws SvmValidationException;

  void setBetragZahlung2Vorrechnung(String betragZahlung2Vorrechnung) throws SvmValidationException;

  void setDatumZahlung3Vorrechnung(String datumZahlung3Vorrechnung) throws SvmValidationException;

  void setBetragZahlung3Vorrechnung(String betragZahlung3Vorrechnung) throws SvmValidationException;

  void setRechnungsdatumNachrechnung(String rechnungsdatumNachrechnung)
      throws SvmValidationException;

  void setErmaessigungNachrechnung(String ermaessigungNachrechnung) throws SvmValidationException;

  void setErmaessigungsgrundNachrechnung(String ermaessigungsgrundNachrechnung)
      throws SvmValidationException;

  void setZuschlagNachrechnung(String zuschlagNachrechnung) throws SvmValidationException;

  void setZuschlagsgrundNachrechnung(String zuschlagsgrundNachrechnung)
      throws SvmValidationException;

  void setAnzahlWochenNachrechnung(String anzahlWochenNachrechnung) throws SvmValidationException;

  void setWochenbetragNachrechnung(String wochenbetragNachrechnung) throws SvmValidationException;

  void setDatumZahlung1Nachrechnung(String datumZahlung1Nachrechnung) throws SvmValidationException;

  void setBetragZahlung1Nachrechnung(String betragZahlung1Nachrechnung)
      throws SvmValidationException;

  void setDatumZahlung2Nachrechnung(String datumZahlung2Nachrechnung) throws SvmValidationException;

  void setBetragZahlung2Nachrechnung(String betragZahlung2Nachrechnung)
      throws SvmValidationException;

  void setDatumZahlung3Nachrechnung(String datumZahlung3Nachrechnung) throws SvmValidationException;

  void setBetragZahlung3Nachrechnung(String betragZahlung3Nachrechnung)
      throws SvmValidationException;

  void setBemerkungen(String bemerkungen) throws SvmValidationException;

  SemesterrechnungCode[] getSelectableSemesterrechnungCodes(SvmModel svmModel);

  Stipendium[] getSelectableStipendien();

  void invalidateRechnungsdatumVorrechnung();

  void invalidateRechnungsdatumNachrechnung();

  void invalidateAll();
}
