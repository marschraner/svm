package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Stipendium;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record ConvertedSemesterrechnungFields(
    Stipendium stipendium,
    boolean gratiskinder,
    Calendar rechnungsdatumVorrechnung,
    BigDecimal ermaessigungVorrechnung,
    String ermaessigungsgrundVorrechnung,
    BigDecimal zuschlagVorrechnung,
    String zuschlagsgrundVorrechnung,
    Integer anzahlWochenVorrechnung,
    BigDecimal wochenbetragVorrechnung,
    Calendar datumZahlung1Vorrechnung,
    BigDecimal betragZahlung1Vorrechnung,
    Calendar datumZahlung2Vorrechnung,
    BigDecimal betragZahlung2Vorrechnung,
    Calendar datumZahlung3Vorrechnung,
    BigDecimal betragZahlung3Vorrechnung,
    Calendar rechnungsdatumNachrechnung,
    BigDecimal ermaessigungNachrechnung,
    String ermaessigungsgrundNachrechnung,
    BigDecimal zuschlagNachrechnung,
    String zuschlagsgrundNachrechnung,
    Integer anzahlWochenNachrechnung,
    BigDecimal wochenbetragNachrechnung,
    Calendar datumZahlung1Nachrechnung,
    BigDecimal betragZahlung1Nachrechnung,
    Calendar datumZahlung2Nachrechnung,
    BigDecimal betragZahlung2Nachrechnung,
    Calendar datumZahlung3Nachrechnung,
    BigDecimal betragZahlung3Nachrechnung,
    String bemerkungen,
    boolean deleted) {

  public static ConvertedSemesterrechnungFields of(Semesterrechnung entity) {
    if (entity == null) return null;

    return new ConvertedSemesterrechnungFields(
        entity.getStipendium(),
        entity.isGratiskinder(),
        entity.getRechnungsdatumVorrechnung(),
        entity.getErmaessigungVorrechnung(),
        entity.getErmaessigungsgrundVorrechnung(),
        entity.getZuschlagVorrechnung(),
        entity.getZuschlagsgrundVorrechnung(),
        entity.getAnzahlWochenVorrechnung(),
        entity.getWochenbetragVorrechnung(),
        entity.getDatumZahlung1Vorrechnung(),
        entity.getBetragZahlung1Vorrechnung(),
        entity.getDatumZahlung2Vorrechnung(),
        entity.getBetragZahlung2Vorrechnung(),
        entity.getDatumZahlung3Vorrechnung(),
        entity.getBetragZahlung3Vorrechnung(),
        entity.getRechnungsdatumNachrechnung(),
        entity.getErmaessigungNachrechnung(),
        entity.getErmaessigungsgrundNachrechnung(),
        entity.getZuschlagNachrechnung(),
        entity.getZuschlagsgrundNachrechnung(),
        entity.getAnzahlWochenNachrechnung(),
        entity.getWochenbetragNachrechnung(),
        entity.getDatumZahlung1Nachrechnung(),
        entity.getBetragZahlung1Nachrechnung(),
        entity.getDatumZahlung2Nachrechnung(),
        entity.getBetragZahlung2Nachrechnung(),
        entity.getDatumZahlung3Nachrechnung(),
        entity.getBetragZahlung3Nachrechnung(),
        entity.getBemerkungen(),
        entity.isDeleted());
  }

  public void mergeIntoEntity(Semesterrechnung entity) {
    if (entity == null) return;

    entity.setStipendium(stipendium());
    entity.setGratiskinder(gratiskinder());
    entity.setRechnungsdatumVorrechnung(rechnungsdatumVorrechnung());
    entity.setErmaessigungVorrechnung(ermaessigungVorrechnung());
    entity.setErmaessigungsgrundVorrechnung(ermaessigungsgrundVorrechnung());
    entity.setZuschlagVorrechnung(zuschlagVorrechnung());
    entity.setZuschlagsgrundVorrechnung(zuschlagsgrundVorrechnung());
    entity.setAnzahlWochenVorrechnung(anzahlWochenVorrechnung());
    entity.setWochenbetragVorrechnung(wochenbetragVorrechnung());
    entity.setDatumZahlung1Vorrechnung(datumZahlung1Vorrechnung());
    entity.setBetragZahlung1Vorrechnung(betragZahlung1Vorrechnung());
    entity.setDatumZahlung2Vorrechnung(datumZahlung2Vorrechnung());
    entity.setBetragZahlung2Vorrechnung(betragZahlung2Vorrechnung());
    entity.setDatumZahlung3Vorrechnung(datumZahlung3Vorrechnung());
    entity.setBetragZahlung3Vorrechnung(betragZahlung3Vorrechnung());
    entity.setRechnungsdatumNachrechnung(rechnungsdatumNachrechnung());
    entity.setErmaessigungNachrechnung(ermaessigungNachrechnung());
    entity.setErmaessigungsgrundNachrechnung(ermaessigungsgrundNachrechnung());
    entity.setZuschlagNachrechnung(zuschlagNachrechnung());
    entity.setZuschlagsgrundNachrechnung(zuschlagsgrundNachrechnung());
    entity.setAnzahlWochenNachrechnung(anzahlWochenNachrechnung());
    entity.setWochenbetragNachrechnung(wochenbetragNachrechnung());
    entity.setDatumZahlung1Nachrechnung(datumZahlung1Nachrechnung());
    entity.setBetragZahlung1Nachrechnung(betragZahlung1Nachrechnung());
    entity.setDatumZahlung2Nachrechnung(datumZahlung2Nachrechnung());
    entity.setBetragZahlung2Nachrechnung(betragZahlung2Nachrechnung());
    entity.setDatumZahlung3Nachrechnung(datumZahlung3Nachrechnung());
    entity.setBetragZahlung3Nachrechnung(betragZahlung3Nachrechnung());
    entity.setBemerkungen(bemerkungen());
    entity.setDeleted(deleted());
  }
}
