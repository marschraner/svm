package ch.metzenthin.svm.domain.model.entityfields;

import ch.metzenthin.svm.common.datatypes.Stipendium;
import ch.metzenthin.svm.domain.model.conversion.BigDecimalConverter;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.conversion.IntegerConverter;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dieser Record wurde generiert mit <ch.metzenthin.svm.RecordGenerator>. Bitte keine manuellen
 * Anpassungen!
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public record SemesterrechnungFields(
    Stipendium stipendium,
    boolean gratiskinder,
    String rechnungsdatumVorrechnung,
    String ermaessigungVorrechnung,
    String ermaessigungsgrundVorrechnung,
    String zuschlagVorrechnung,
    String zuschlagsgrundVorrechnung,
    String anzahlWochenVorrechnung,
    String wochenbetragVorrechnung,
    String datumZahlung1Vorrechnung,
    String betragZahlung1Vorrechnung,
    String datumZahlung2Vorrechnung,
    String betragZahlung2Vorrechnung,
    String datumZahlung3Vorrechnung,
    String betragZahlung3Vorrechnung,
    String rechnungsdatumNachrechnung,
    String ermaessigungNachrechnung,
    String ermaessigungsgrundNachrechnung,
    String zuschlagNachrechnung,
    String zuschlagsgrundNachrechnung,
    String anzahlWochenNachrechnung,
    String wochenbetragNachrechnung,
    String datumZahlung1Nachrechnung,
    String betragZahlung1Nachrechnung,
    String datumZahlung2Nachrechnung,
    String betragZahlung2Nachrechnung,
    String datumZahlung3Nachrechnung,
    String betragZahlung3Nachrechnung,
    String bemerkungen,
    boolean deleted) {

  public static SemesterrechnungFields of(Semesterrechnung entity) {
    if (entity == null) return null;

    return new SemesterrechnungFields(
        entity.getStipendium(),
        entity.isGratiskinder(),
        CalendarConverter.toString(entity.getRechnungsdatumVorrechnung()),
        BigDecimalConverter.toString(entity.getErmaessigungVorrechnung()),
        entity.getErmaessigungsgrundVorrechnung(),
        BigDecimalConverter.toString(entity.getZuschlagVorrechnung()),
        entity.getZuschlagsgrundVorrechnung(),
        IntegerConverter.toString(entity.getAnzahlWochenVorrechnung()),
        BigDecimalConverter.toString(entity.getWochenbetragVorrechnung()),
        CalendarConverter.toString(entity.getDatumZahlung1Vorrechnung()),
        BigDecimalConverter.toString(entity.getBetragZahlung1Vorrechnung()),
        CalendarConverter.toString(entity.getDatumZahlung2Vorrechnung()),
        BigDecimalConverter.toString(entity.getBetragZahlung2Vorrechnung()),
        CalendarConverter.toString(entity.getDatumZahlung3Vorrechnung()),
        BigDecimalConverter.toString(entity.getBetragZahlung3Vorrechnung()),
        CalendarConverter.toString(entity.getRechnungsdatumNachrechnung()),
        BigDecimalConverter.toString(entity.getErmaessigungNachrechnung()),
        entity.getErmaessigungsgrundNachrechnung(),
        BigDecimalConverter.toString(entity.getZuschlagNachrechnung()),
        entity.getZuschlagsgrundNachrechnung(),
        IntegerConverter.toString(entity.getAnzahlWochenNachrechnung()),
        BigDecimalConverter.toString(entity.getWochenbetragNachrechnung()),
        CalendarConverter.toString(entity.getDatumZahlung1Nachrechnung()),
        BigDecimalConverter.toString(entity.getBetragZahlung1Nachrechnung()),
        CalendarConverter.toString(entity.getDatumZahlung2Nachrechnung()),
        BigDecimalConverter.toString(entity.getBetragZahlung2Nachrechnung()),
        CalendarConverter.toString(entity.getDatumZahlung3Nachrechnung()),
        BigDecimalConverter.toString(entity.getBetragZahlung3Nachrechnung()),
        entity.getBemerkungen(),
        entity.isDeleted());
  }

  @SuppressWarnings("java:S3776")
  public ConvertedFieldsAndConversionResults<ConvertedSemesterrechnungFields> convert() {
    List<ConversionResult<?>> conversionErrors = new ArrayList<>();

    ConversionResult<Calendar> rechnungsdatumVorrechnung =
        CalendarConverter.convertToCalendar(
            "rechnungsdatumVorrechnung", rechnungsdatumVorrechnung());
    if (!rechnungsdatumVorrechnung.isValid()) conversionErrors.add(rechnungsdatumVorrechnung);
    ConversionResult<BigDecimal> ermaessigungVorrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "ermaessigungVorrechnung", ermaessigungVorrechnung());
    if (!ermaessigungVorrechnung.isValid()) conversionErrors.add(ermaessigungVorrechnung);
    ConversionResult<BigDecimal> zuschlagVorrechnung =
        BigDecimalConverter.convertToBigDecimal("zuschlagVorrechnung", zuschlagVorrechnung());
    if (!zuschlagVorrechnung.isValid()) conversionErrors.add(zuschlagVorrechnung);
    ConversionResult<Integer> anzahlWochenVorrechnung =
        IntegerConverter.convertToInteger("anzahlWochenVorrechnung", anzahlWochenVorrechnung());
    if (!anzahlWochenVorrechnung.isValid()) conversionErrors.add(anzahlWochenVorrechnung);
    ConversionResult<BigDecimal> wochenbetragVorrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "wochenbetragVorrechnung", wochenbetragVorrechnung());
    if (!wochenbetragVorrechnung.isValid()) conversionErrors.add(wochenbetragVorrechnung);
    ConversionResult<Calendar> datumZahlung1Vorrechnung =
        CalendarConverter.convertToCalendar("datumZahlung1Vorrechnung", datumZahlung1Vorrechnung());
    if (!datumZahlung1Vorrechnung.isValid()) conversionErrors.add(datumZahlung1Vorrechnung);
    ConversionResult<BigDecimal> betragZahlung1Vorrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "betragZahlung1Vorrechnung", betragZahlung1Vorrechnung());
    if (!betragZahlung1Vorrechnung.isValid()) conversionErrors.add(betragZahlung1Vorrechnung);
    ConversionResult<Calendar> datumZahlung2Vorrechnung =
        CalendarConverter.convertToCalendar("datumZahlung2Vorrechnung", datumZahlung2Vorrechnung());
    if (!datumZahlung2Vorrechnung.isValid()) conversionErrors.add(datumZahlung2Vorrechnung);
    ConversionResult<BigDecimal> betragZahlung2Vorrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "betragZahlung2Vorrechnung", betragZahlung2Vorrechnung());
    if (!betragZahlung2Vorrechnung.isValid()) conversionErrors.add(betragZahlung2Vorrechnung);
    ConversionResult<Calendar> datumZahlung3Vorrechnung =
        CalendarConverter.convertToCalendar("datumZahlung3Vorrechnung", datumZahlung3Vorrechnung());
    if (!datumZahlung3Vorrechnung.isValid()) conversionErrors.add(datumZahlung3Vorrechnung);
    ConversionResult<BigDecimal> betragZahlung3Vorrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "betragZahlung3Vorrechnung", betragZahlung3Vorrechnung());
    if (!betragZahlung3Vorrechnung.isValid()) conversionErrors.add(betragZahlung3Vorrechnung);
    ConversionResult<Calendar> rechnungsdatumNachrechnung =
        CalendarConverter.convertToCalendar(
            "rechnungsdatumNachrechnung", rechnungsdatumNachrechnung());
    if (!rechnungsdatumNachrechnung.isValid()) conversionErrors.add(rechnungsdatumNachrechnung);
    ConversionResult<BigDecimal> ermaessigungNachrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "ermaessigungNachrechnung", ermaessigungNachrechnung());
    if (!ermaessigungNachrechnung.isValid()) conversionErrors.add(ermaessigungNachrechnung);
    ConversionResult<BigDecimal> zuschlagNachrechnung =
        BigDecimalConverter.convertToBigDecimal("zuschlagNachrechnung", zuschlagNachrechnung());
    if (!zuschlagNachrechnung.isValid()) conversionErrors.add(zuschlagNachrechnung);
    ConversionResult<Integer> anzahlWochenNachrechnung =
        IntegerConverter.convertToInteger("anzahlWochenNachrechnung", anzahlWochenNachrechnung());
    if (!anzahlWochenNachrechnung.isValid()) conversionErrors.add(anzahlWochenNachrechnung);
    ConversionResult<BigDecimal> wochenbetragNachrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "wochenbetragNachrechnung", wochenbetragNachrechnung());
    if (!wochenbetragNachrechnung.isValid()) conversionErrors.add(wochenbetragNachrechnung);
    ConversionResult<Calendar> datumZahlung1Nachrechnung =
        CalendarConverter.convertToCalendar(
            "datumZahlung1Nachrechnung", datumZahlung1Nachrechnung());
    if (!datumZahlung1Nachrechnung.isValid()) conversionErrors.add(datumZahlung1Nachrechnung);
    ConversionResult<BigDecimal> betragZahlung1Nachrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "betragZahlung1Nachrechnung", betragZahlung1Nachrechnung());
    if (!betragZahlung1Nachrechnung.isValid()) conversionErrors.add(betragZahlung1Nachrechnung);
    ConversionResult<Calendar> datumZahlung2Nachrechnung =
        CalendarConverter.convertToCalendar(
            "datumZahlung2Nachrechnung", datumZahlung2Nachrechnung());
    if (!datumZahlung2Nachrechnung.isValid()) conversionErrors.add(datumZahlung2Nachrechnung);
    ConversionResult<BigDecimal> betragZahlung2Nachrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "betragZahlung2Nachrechnung", betragZahlung2Nachrechnung());
    if (!betragZahlung2Nachrechnung.isValid()) conversionErrors.add(betragZahlung2Nachrechnung);
    ConversionResult<Calendar> datumZahlung3Nachrechnung =
        CalendarConverter.convertToCalendar(
            "datumZahlung3Nachrechnung", datumZahlung3Nachrechnung());
    if (!datumZahlung3Nachrechnung.isValid()) conversionErrors.add(datumZahlung3Nachrechnung);
    ConversionResult<BigDecimal> betragZahlung3Nachrechnung =
        BigDecimalConverter.convertToBigDecimal(
            "betragZahlung3Nachrechnung", betragZahlung3Nachrechnung());
    if (!betragZahlung3Nachrechnung.isValid()) conversionErrors.add(betragZahlung3Nachrechnung);

    ConvertedSemesterrechnungFields convertedSemesterrechnungFields =
        new ConvertedSemesterrechnungFields(
            stipendium(),
            gratiskinder(),
            rechnungsdatumVorrechnung.convertedValue(),
            ermaessigungVorrechnung.convertedValue(),
            ermaessigungsgrundVorrechnung(),
            zuschlagVorrechnung.convertedValue(),
            zuschlagsgrundVorrechnung(),
            anzahlWochenVorrechnung.convertedValue(),
            wochenbetragVorrechnung.convertedValue(),
            datumZahlung1Vorrechnung.convertedValue(),
            betragZahlung1Vorrechnung.convertedValue(),
            datumZahlung2Vorrechnung.convertedValue(),
            betragZahlung2Vorrechnung.convertedValue(),
            datumZahlung3Vorrechnung.convertedValue(),
            betragZahlung3Vorrechnung.convertedValue(),
            rechnungsdatumNachrechnung.convertedValue(),
            ermaessigungNachrechnung.convertedValue(),
            ermaessigungsgrundNachrechnung(),
            zuschlagNachrechnung.convertedValue(),
            zuschlagsgrundNachrechnung(),
            anzahlWochenNachrechnung.convertedValue(),
            wochenbetragNachrechnung.convertedValue(),
            datumZahlung1Nachrechnung.convertedValue(),
            betragZahlung1Nachrechnung.convertedValue(),
            datumZahlung2Nachrechnung.convertedValue(),
            betragZahlung2Nachrechnung.convertedValue(),
            datumZahlung3Nachrechnung.convertedValue(),
            betragZahlung3Nachrechnung.convertedValue(),
            bemerkungen(),
            deleted());

    return new ConvertedFieldsAndConversionResults<>(
        convertedSemesterrechnungFields, conversionErrors);
  }
}
