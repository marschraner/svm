package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Stipendium;
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
    Calendar getRechnungsdatumNachrechnung();
    BigDecimal getErmaessigungNachrechnung();
    String getErmaessigungsgrundNachrechnung();
    BigDecimal getZuschlagNachrechnung();
    String getZuschlagsgrundNachrechnung();
    Integer getAnzahlWochenNachrechnung();
    BigDecimal getWochenbetragNachrechnung();
    Calendar getDatumZahlung1();
    BigDecimal getBetragZahlung1();
    Calendar getDatumZahlung2();
    BigDecimal getBetragZahlung2();
    Calendar getDatumZahlung3();
    BigDecimal getBetragZahlung3();
    String getBemerkungen();

    void setSemesterrechnungCode(SemesterrechnungCode semesterrechnungCode);
    void setStipendium(Stipendium stipendium);
    void setGratiskinder(Boolean gratiskind);
    void setRechnungsdatumVorrechnung(String rechnungsdatumVorrechnung) throws SvmValidationException;
    void setErmaessigungVorrechnung(String ermaessigungVorrechnung) throws SvmValidationException;
    void setErmaessigungsgrundVorrechnung(String ermaessigunsgrundVorrechnung) throws SvmValidationException;
    void setZuschlagVorrechnung(String zuschlagVorrechnung) throws SvmValidationException;
    void setZuschlagsgrundVorrechnung(String zuschlagsgrundVorrechnung) throws SvmValidationException;
    void setAnzahlWochenVorrechnung(String anzahlWochenVorrechnung) throws SvmValidationException;
    void setWochenbetragVorrechnung(String wochenbetragVorrechnung) throws SvmValidationException;
    void setRechnungsdatumNachrechnung(String rechnungsdatumNachrechnung) throws SvmValidationException;
    void setErmaessigungNachrechnung(String ermaessigungNachrechnung) throws SvmValidationException;
    void setErmaessigungsgrundNachrechnung(String ermaessigunsgrundNachrechnung) throws SvmValidationException;
    void setZuschlagNachrechnung(String zuschlagNachrechnung) throws SvmValidationException;
    void setZuschlagsgrundNachrechnung(String zuschlagsgrundNachrechnung) throws SvmValidationException;
    void setAnzahlWochenNachrechnung(String anzahlWochenNachrechnung) throws SvmValidationException;
    void setWochenbetragNachrechnung(String wochenbetragNachrechnung) throws SvmValidationException;
    void setDatumZahlung1(String datumZahlung1) throws SvmValidationException;
    void setBetragZahlung1(String betragZahlung1) throws SvmValidationException;
    void setDatumZahlung2(String datumZahlung2) throws SvmValidationException;
    void setBetragZahlung2(String betragZahlung2) throws SvmValidationException;
    void setDatumZahlung3(String datumZahlung3) throws SvmValidationException;
    void setBetragZahlung3(String betragZahlung3) throws SvmValidationException;
    void setBemerkungen(String bemerkungen) throws SvmValidationException;

    SemesterrechnungCode[] getSelectableSemesterrechnungCodes(SvmModel svmModel);
    Stipendium[] getSelectableStipendien();
    void invalidateRechnungsdatumVorrechnung();
    void invalidateRechnungsdatumNachrechnung();
    void invalidateAll();
}
