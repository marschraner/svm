package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.common.datatypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Martin Schraner
 */
abstract class SemesterrechnungModelImpl extends AbstractModel implements SemesterrechnungModel {

    protected Semesterrechnung semesterrechnung = new Semesterrechnung();
    protected SemesterrechnungCode semesterrechnungCode = new SemesterrechnungCode();

    static {
        SEMESTERRECHNUNG_CODE_ALLE.setKuerzel("");
        SEMESTERRECHNUNG_CODE_ALLE.setBeschreibung("");
        SEMESTERRECHNUNG_CODE_KEINER.setKuerzel("");
        SEMESTERRECHNUNG_CODE_KEINER.setBeschreibung("");
    }

    @Override
    public SemesterrechnungCode getSemesterrechnungCode() {
        return semesterrechnungCode;
    }

    @Override
    public void setSemesterrechnungCode(SemesterrechnungCode semesterrechnungCode) {
        if (semesterrechnungCode == SEMESTERRECHNUNG_CODE_KEINER) {
            semesterrechnungCode = null;
        }
        SemesterrechnungCode oldValue = this.semesterrechnungCode;
        this.semesterrechnungCode = semesterrechnungCode;
        firePropertyChange(Field.SEMESTERRECHNUNG_CODE, oldValue, this.semesterrechnungCode);
    }

    @Override
    public Stipendium getStipendium() {
        return semesterrechnung.getStipendium();
    }

    @Override
    public void setStipendium(Stipendium stipendium) {
        if (stipendium == Stipendium.KEINES) {
            stipendium = null;
        }
        Stipendium oldValue = semesterrechnung.getStipendium();
        semesterrechnung.setStipendium(stipendium);
        firePropertyChange(Field.STIPENDIUM, oldValue, semesterrechnung.getStipendium());
    }

    @Override
    public Boolean isGratiskinder() {
        return semesterrechnung.getGratiskinder();
    }

    @Override
    public void setGratiskinder(Boolean gratiskinder) {
        Boolean oldValue = semesterrechnung.getGratiskinder();
        semesterrechnung.setGratiskinder(gratiskinder);
        firePropertyChange(Field.GRATISKINDER, oldValue, semesterrechnung.getGratiskinder());
    }

    private final CalendarModelAttribute rechnungsdatumVorrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.RECHNUNGSDATUM_VORRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getRechnungsdatumVorrechnung();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setRechnungsdatumVorrechnung(value);
                }
            }
    );

    @Override
    public Calendar getRechnungsdatumVorrechnung() {
        return rechnungsdatumVorrechnungModelAttribute.getValue();
    }

    @Override
    public void setRechnungsdatumVorrechnung(String rechnungsdatumVorrechnung) throws SvmValidationException {
        rechnungsdatumVorrechnungModelAttribute.setNewValue(false, rechnungsdatumVorrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute ermaessigungVorrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.ERMAESSIGUNG_VORRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getErmaessigungVorrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setErmaessigungVorrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getErmaessigungVorrechnung() {
        return ermaessigungVorrechnungModelAttribute.getValue();
    }

    @Override
    public void setErmaessigungVorrechnung(String ermaessigungVorrechnung) throws SvmValidationException {
        ermaessigungVorrechnungModelAttribute.setNewValue(false, ermaessigungVorrechnung, isBulkUpdate());
    }

    private final StringModelAttribute ermaessigungsgrundVorrechnungModelAttribute = new StringModelAttribute(
            this,
            Field.ERMAESSIGUNGSGRUND_VORRECHNUNG, 5, 50,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return semesterrechnung.getErmaessigungsgrundVorrechnung();
                }

                @Override
                public void setValue(String value) {
                    semesterrechnung.setErmaessigungsgrundVorrechnung(value);
                }
            }
    );

    @Override
    public String getErmaessigungsgrundVorrechnung() {
        return ermaessigungsgrundVorrechnungModelAttribute.getValue();
    }

    @Override
    public void setErmaessigungsgrundVorrechnung(String ermaessigungsgrundVorrechnung) throws SvmValidationException {
        ermaessigungsgrundVorrechnungModelAttribute.setNewValue(false, ermaessigungsgrundVorrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute zuschlagVorrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.ZUSCHLAG_VORRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getZuschlagVorrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setZuschlagVorrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getZuschlagVorrechnung() {
        return zuschlagVorrechnungModelAttribute.getValue();
    }

    @Override
    public void setZuschlagVorrechnung(String zuschlagVorrechnung) throws SvmValidationException {
        zuschlagVorrechnungModelAttribute.setNewValue(false, zuschlagVorrechnung, isBulkUpdate());
    }

    private final StringModelAttribute zuschlagsgrundVorrechnungModelAttribute = new StringModelAttribute(
            this,
            Field.ZUSCHLAGSGRUND_VORRECHNUNG, 5, 50,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return semesterrechnung.getZuschlagsgrundVorrechnung();
                }

                @Override
                public void setValue(String value) {
                    semesterrechnung.setZuschlagsgrundVorrechnung(value);
                }
            }
    );

    @Override
    public String getZuschlagsgrundVorrechnung() {
        return zuschlagsgrundVorrechnungModelAttribute.getValue();
    }

    @Override
    public void setZuschlagsgrundVorrechnung(String zuschlagsgrundVorrechnung) throws SvmValidationException {
        zuschlagsgrundVorrechnungModelAttribute.setNewValue(false, zuschlagsgrundVorrechnung, isBulkUpdate());
    }

    private final IntegerModelAttribute anzahlWochenVorrechnungModelAttribute = new IntegerModelAttribute(
            this,
            Field.ANZAHL_WOCHEN_VORRECHNUNG, 1, 50,
            new AttributeAccessor<>() {
                @Override
                public Integer getValue() {
                    return semesterrechnung.getAnzahlWochenVorrechnung();
                }

                @Override
                public void setValue(Integer value) {
                    semesterrechnung.setAnzahlWochenVorrechnung(value);
                }
            }
    );

    @Override
    public Integer getAnzahlWochenVorrechnung() {
        return anzahlWochenVorrechnungModelAttribute.getValue();
    }

    @Override
    public void setAnzahlWochenVorrechnung(String anzahlWochenVorrechnung) throws SvmValidationException {
        anzahlWochenVorrechnungModelAttribute.setNewValue(false, anzahlWochenVorrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute wochenbetragVorrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.WOCHENBETRAG_VORRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getWochenbetragVorrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setWochenbetragVorrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getWochenbetragVorrechnung() {
        return wochenbetragVorrechnungModelAttribute.getValue();
    }

    @Override
    public void setWochenbetragVorrechnung(String wochenbetragVorrechnung) throws SvmValidationException {
        wochenbetragVorrechnungModelAttribute.setNewValue(false, wochenbetragVorrechnung, isBulkUpdate());
    }

    private final CalendarModelAttribute datumZahlung1VorrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.DATUM_ZAHLUNG_1_VORRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getDatumZahlung1Vorrechnung();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setDatumZahlung1Vorrechnung(value);
                }
            }
    );

    @Override
    public Calendar getDatumZahlung1Vorrechnung() {
        return datumZahlung1VorrechnungModelAttribute.getValue();
    }

    @Override
    public void setDatumZahlung1Vorrechnung(String datumZahlung1Vorrechnung) throws SvmValidationException {
        datumZahlung1VorrechnungModelAttribute.setNewValue(false, datumZahlung1Vorrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute betragZahlung1VorrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_ZAHLUNG_1_VORRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getBetragZahlung1Vorrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setBetragZahlung1Vorrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getBetragZahlung1Vorrechnung() {
        return betragZahlung1VorrechnungModelAttribute.getValue();
    }

    @Override
    public void setBetragZahlung1Vorrechnung(String betragZahlung1Vorrechnung) throws SvmValidationException {
        betragZahlung1VorrechnungModelAttribute.setNewValue(false, betragZahlung1Vorrechnung, isBulkUpdate());
    }

    private final CalendarModelAttribute datumZahlung2VorrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.DATUM_ZAHLUNG_2_VORRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 2), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getDatumZahlung2Vorrechnung();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setDatumZahlung2Vorrechnung(value);
                }
            }
    );

    @Override
    public Calendar getDatumZahlung2Vorrechnung() {
        return datumZahlung2VorrechnungModelAttribute.getValue();
    }

    @Override
    public void setDatumZahlung2Vorrechnung(String datumZahlung2Vorrechnung) throws SvmValidationException {
        datumZahlung2VorrechnungModelAttribute.setNewValue(false, datumZahlung2Vorrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute betragZahlung2VorrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_ZAHLUNG_2_VORRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getBetragZahlung2Vorrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setBetragZahlung2Vorrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getBetragZahlung2Vorrechnung() {
        return betragZahlung2VorrechnungModelAttribute.getValue();
    }

    @Override
    public void setBetragZahlung2Vorrechnung(String betragZahlung2Vorrechnung) throws SvmValidationException {
        betragZahlung2VorrechnungModelAttribute.setNewValue(false, betragZahlung2Vorrechnung, isBulkUpdate());
    }

    private final CalendarModelAttribute datumZahlung3VorrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.DATUM_ZAHLUNG_3_VORRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 3), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getDatumZahlung3Vorrechnung();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setDatumZahlung3Vorrechnung(value);
                }
            }
    );

    @Override
    public Calendar getDatumZahlung3Vorrechnung() {
        return datumZahlung3VorrechnungModelAttribute.getValue();
    }

    @Override
    public void setDatumZahlung3Vorrechnung(String datumZahlung3Vorrechnung) throws SvmValidationException {
        datumZahlung3VorrechnungModelAttribute.setNewValue(false, datumZahlung3Vorrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute betragZahlung3VorrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_ZAHLUNG_3_VORRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getBetragZahlung3Vorrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setBetragZahlung3Vorrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getBetragZahlung3Vorrechnung() {
        return betragZahlung3VorrechnungModelAttribute.getValue();
    }

    @Override
    public void setBetragZahlung3Vorrechnung(String betragZahlung3Vorrechnung) throws SvmValidationException {
        betragZahlung3VorrechnungModelAttribute.setNewValue(false, betragZahlung3Vorrechnung, isBulkUpdate());
    }

    private final CalendarModelAttribute rechnungsdatumNachrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.RECHNUNGSDATUM_NACHRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getRechnungsdatumNachrechnung();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setRechnungsdatumNachrechnung(value);
                }
            }
    );

    @Override
    public Calendar getRechnungsdatumNachrechnung() {
        return rechnungsdatumNachrechnungModelAttribute.getValue();
    }

    @Override
    public void setRechnungsdatumNachrechnung(String rechnungsdatumNachrechnung) throws SvmValidationException {
        rechnungsdatumNachrechnungModelAttribute.setNewValue(false, rechnungsdatumNachrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute ermaessigungNachrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.ERMAESSIGUNG_NACHRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getErmaessigungNachrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setErmaessigungNachrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getErmaessigungNachrechnung() {
        return ermaessigungNachrechnungModelAttribute.getValue();
    }

    @Override
    public void setErmaessigungNachrechnung(String ermaessigungNachrechnung) throws SvmValidationException {
        ermaessigungNachrechnungModelAttribute.setNewValue(false, ermaessigungNachrechnung, isBulkUpdate());
    }

    private final StringModelAttribute ermaessigungsgrundNachrechnungModelAttribute = new StringModelAttribute(
            this,
            Field.ERMAESSIGUNGSGRUND_NACHRECHNUNG, 5, 50,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return semesterrechnung.getErmaessigungsgrundNachrechnung();
                }

                @Override
                public void setValue(String value) {
                    semesterrechnung.setErmaessigungsgrundNachrechnung(value);
                }
            }
    );

    @Override
    public String getErmaessigungsgrundNachrechnung() {
        return ermaessigungsgrundNachrechnungModelAttribute.getValue();
    }

    @Override
    public void setErmaessigungsgrundNachrechnung(String ermaessigungsgrundNachrechnung) throws SvmValidationException {
        ermaessigungsgrundNachrechnungModelAttribute.setNewValue(false, ermaessigungsgrundNachrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute zuschlagNachrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.ZUSCHLAG_NACHRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getZuschlagNachrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setZuschlagNachrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getZuschlagNachrechnung() {
        return zuschlagNachrechnungModelAttribute.getValue();
    }

    @Override
    public void setZuschlagNachrechnung(String zuschlagNachrechnung) throws SvmValidationException {
        zuschlagNachrechnungModelAttribute.setNewValue(false, zuschlagNachrechnung, isBulkUpdate());
    }

    private final StringModelAttribute zuschlagsgrundNachrechnungModelAttribute = new StringModelAttribute(
            this,
            Field.ZUSCHLAGSGRUND_NACHRECHNUNG, 5, 50,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return semesterrechnung.getZuschlagsgrundNachrechnung();
                }

                @Override
                public void setValue(String value) {
                    semesterrechnung.setZuschlagsgrundNachrechnung(value);
                }
            }
    );

    @Override
    public String getZuschlagsgrundNachrechnung() {
        return zuschlagsgrundNachrechnungModelAttribute.getValue();
    }

    @Override
    public void setZuschlagsgrundNachrechnung(String zuschlagsgrundNachrechnung) throws SvmValidationException {
        zuschlagsgrundNachrechnungModelAttribute.setNewValue(false, zuschlagsgrundNachrechnung, isBulkUpdate());
    }

    private final IntegerModelAttribute anzahlWochenNachrechnungModelAttribute = new IntegerModelAttribute(
            this,
            Field.ANZAHL_WOCHEN_NACHRECHNUNG, 1, 50,
            new AttributeAccessor<>() {
                @Override
                public Integer getValue() {
                    return semesterrechnung.getAnzahlWochenNachrechnung();
                }

                @Override
                public void setValue(Integer value) {
                    semesterrechnung.setAnzahlWochenNachrechnung(value);
                }
            }
    );

    @Override
    public Integer getAnzahlWochenNachrechnung() {
        return anzahlWochenNachrechnungModelAttribute.getValue();
    }

    @Override
    public void setAnzahlWochenNachrechnung(String anzahlWochenNachrechnung) throws SvmValidationException {
        anzahlWochenNachrechnungModelAttribute.setNewValue(false, anzahlWochenNachrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute wochenbetragNachrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.WOCHENBETRAG_NACHRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getWochenbetragNachrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setWochenbetragNachrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getWochenbetragNachrechnung() {
        return wochenbetragNachrechnungModelAttribute.getValue();
    }

    @Override
    public void setWochenbetragNachrechnung(String wochenbetragNachrechnung) throws SvmValidationException {
        wochenbetragNachrechnungModelAttribute.setNewValue(false, wochenbetragNachrechnung, isBulkUpdate());
    }

    private final CalendarModelAttribute datumZahlung1NachrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.DATUM_ZAHLUNG_1_NACHRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getDatumZahlung1Nachrechnung();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setDatumZahlung1Nachrechnung(value);
                }
            }
    );

    @Override
    public Calendar getDatumZahlung1Nachrechnung() {
        return datumZahlung1NachrechnungModelAttribute.getValue();
    }

    @Override
    public void setDatumZahlung1Nachrechnung(String datumZahlung1Nachrechnung) throws SvmValidationException {
        datumZahlung1NachrechnungModelAttribute.setNewValue(false, datumZahlung1Nachrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute betragZahlung1NachrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getBetragZahlung1Nachrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setBetragZahlung1Nachrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getBetragZahlung1Nachrechnung() {
        return betragZahlung1NachrechnungModelAttribute.getValue();
    }

    @Override
    public void setBetragZahlung1Nachrechnung(String betragZahlung1Nachrechnung) throws SvmValidationException {
        betragZahlung1NachrechnungModelAttribute.setNewValue(false, betragZahlung1Nachrechnung, isBulkUpdate());
    }

    private final CalendarModelAttribute datumZahlung2NachrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.DATUM_ZAHLUNG_2_NACHRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 2), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getDatumZahlung2Nachrechnung();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setDatumZahlung2Nachrechnung(value);
                }
            }
    );

    @Override
    public Calendar getDatumZahlung2Nachrechnung() {
        return datumZahlung2NachrechnungModelAttribute.getValue();
    }

    @Override
    public void setDatumZahlung2Nachrechnung(String datumZahlung2Nachrechnung) throws SvmValidationException {
        datumZahlung2NachrechnungModelAttribute.setNewValue(false, datumZahlung2Nachrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute betragZahlung2NachrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getBetragZahlung2Nachrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setBetragZahlung2Nachrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getBetragZahlung2Nachrechnung() {
        return betragZahlung2NachrechnungModelAttribute.getValue();
    }

    @Override
    public void setBetragZahlung2Nachrechnung(String betragZahlung2Nachrechnung) throws SvmValidationException {
        betragZahlung2NachrechnungModelAttribute.setNewValue(false, betragZahlung2Nachrechnung, isBulkUpdate());
    }

    private final CalendarModelAttribute datumZahlung3NachrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.DATUM_ZAHLUNG_3_NACHRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 3), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getDatumZahlung3Nachrechnung();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setDatumZahlung3Nachrechnung(value);
                }
            }
    );

    @Override
    public Calendar getDatumZahlung3Nachrechnung() {
        return datumZahlung3NachrechnungModelAttribute.getValue();
    }

    @Override
    public void setDatumZahlung3Nachrechnung(String datumZahlung3Nachrechnung) throws SvmValidationException {
        datumZahlung3NachrechnungModelAttribute.setNewValue(false, datumZahlung3Nachrechnung, isBulkUpdate());
    }

    private final PreisModelAttribute betragZahlung3NachrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getBetragZahlung3Nachrechnung();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setBetragZahlung3Nachrechnung(value);
                }
            }
    );

    @Override
    public BigDecimal getBetragZahlung3Nachrechnung() {
        return betragZahlung3NachrechnungModelAttribute.getValue();
    }

    @Override
    public void setBetragZahlung3Nachrechnung(String betragZahlung3Nachrechnung) throws SvmValidationException {
        betragZahlung3NachrechnungModelAttribute.setNewValue(false, betragZahlung3Nachrechnung, isBulkUpdate());
    }

    private final StringModelAttribute bemerkungenModelAttribute = new StringModelAttribute(
            this,
            Field.BEMERKUNGEN, 0, 1000,
            new AttributeAccessor<>() {
                @Override
                public String getValue() {
                    return semesterrechnung.getBemerkungen();
                }

                @Override
                public void setValue(String value) {
                    semesterrechnung.setBemerkungen(value);
                }
            }
    );

    @Override
    public String getBemerkungen() {
        return bemerkungenModelAttribute.getValue();
    }

    @Override
    public void setBemerkungen(String bemerkungen) throws SvmValidationException {
        bemerkungenModelAttribute.setNewValue(false, bemerkungen, isBulkUpdate());
    }

    @Override
    public void invalidateRechnungsdatumVorrechnung() {
        rechnungsdatumVorrechnungModelAttribute.initValue(null);
    }

    @Override
    public void invalidateRechnungsdatumNachrechnung() {
        rechnungsdatumNachrechnungModelAttribute.initValue(null);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void invalidateAll() {
        setSemesterrechnungCode(null);
        setStipendium(null);
        rechnungsdatumVorrechnungModelAttribute.initValue(null);
        ermaessigungVorrechnungModelAttribute.initValue(null);
        ermaessigungsgrundVorrechnungModelAttribute.initValue(null);
        zuschlagVorrechnungModelAttribute.initValue(null);
        zuschlagsgrundVorrechnungModelAttribute.initValue(null);
        anzahlWochenVorrechnungModelAttribute.initValue(null);
        wochenbetragVorrechnungModelAttribute.initValue(null);
        datumZahlung1VorrechnungModelAttribute.initValue(null);
        betragZahlung1VorrechnungModelAttribute.initValue(null);
        datumZahlung2VorrechnungModelAttribute.initValue(null);
        betragZahlung2VorrechnungModelAttribute.initValue(null);
        datumZahlung3VorrechnungModelAttribute.initValue(null);
        betragZahlung3VorrechnungModelAttribute.initValue(null);
        rechnungsdatumNachrechnungModelAttribute.initValue(null);
        ermaessigungNachrechnungModelAttribute.initValue(null);
        ermaessigungsgrundNachrechnungModelAttribute.initValue(null);
        zuschlagNachrechnungModelAttribute.initValue(null);
        zuschlagsgrundNachrechnungModelAttribute.initValue(null);
        anzahlWochenNachrechnungModelAttribute.initValue(null);
        wochenbetragNachrechnungModelAttribute.initValue(null);
        datumZahlung1NachrechnungModelAttribute.initValue(null);
        betragZahlung1NachrechnungModelAttribute.initValue(null);
        datumZahlung2NachrechnungModelAttribute.initValue(null);
        betragZahlung2NachrechnungModelAttribute.initValue(null);
        datumZahlung3NachrechnungModelAttribute.initValue(null);
        betragZahlung3NachrechnungModelAttribute.initValue(null);
        bemerkungenModelAttribute.initValue(null);
    }
}
