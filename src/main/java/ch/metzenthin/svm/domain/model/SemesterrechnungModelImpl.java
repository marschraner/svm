package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Schuljahre;
import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Martin Schraner
 */
abstract class SemesterrechnungModelImpl extends AbstractModel implements SemesterrechnungModel  {

    protected Semesterrechnung semesterrechnung = new Semesterrechnung();
    protected SemesterrechnungCode semesterrechnungCode = new SemesterrechnungCode();

    SemesterrechnungModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

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

    private CalendarModelAttribute rechnungsdatumVorrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.RECHNUNGSDATUM_VORRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<Calendar>() {
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
            new AttributeAccessor<BigDecimal>() {
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
            new AttributeAccessor<String>() {
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
            new AttributeAccessor<BigDecimal>() {
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
            new AttributeAccessor<String>() {
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
            new AttributeAccessor<Integer>() {
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
            new AttributeAccessor<BigDecimal>() {
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

    private CalendarModelAttribute rechnungsdatumNachrechnungModelAttribute = new CalendarModelAttribute(
            this,
            Field.RECHNUNGSDATUM_NACHRECHNUNG, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<Calendar>() {
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
            new AttributeAccessor<BigDecimal>() {
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
            new AttributeAccessor<String>() {
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
            new AttributeAccessor<BigDecimal>() {
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
            new AttributeAccessor<String>() {
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
            new AttributeAccessor<Integer>() {
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
            new AttributeAccessor<BigDecimal>() {
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

    private CalendarModelAttribute datumZahlung1ModelAttribute = new CalendarModelAttribute(
            this,
            Field.DATUM_ZAHLUNG_1, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 31),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getDatumZahlung1();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setDatumZahlung1(value);
                }
            }
    );

    @Override
    public Calendar getDatumZahlung1() {
        return datumZahlung1ModelAttribute.getValue();
    }

    @Override
    public void setDatumZahlung1(String datumZahlung1) throws SvmValidationException {
        datumZahlung1ModelAttribute.setNewValue(false, datumZahlung1, isBulkUpdate());
    }

    private final PreisModelAttribute betragZahlung1ModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_ZAHLUNG_1, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getBetragZahlung1();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setBetragZahlung1(value);
                }
            }
    );

    @Override
    public BigDecimal getBetragZahlung1() {
        return betragZahlung1ModelAttribute.getValue();
    }

    @Override
    public void setBetragZahlung1(String betragZahlung1) throws SvmValidationException {
        betragZahlung1ModelAttribute.setNewValue(false, betragZahlung1, isBulkUpdate());
    }

    private CalendarModelAttribute datumZahlung2ModelAttribute = new CalendarModelAttribute(
            this,
            Field.DATUM_ZAHLUNG_2, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 2), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 32),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getDatumZahlung2();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setDatumZahlung2(value);
                }
            }
    );

    @Override
    public Calendar getDatumZahlung2() {
        return datumZahlung2ModelAttribute.getValue();
    }

    @Override
    public void setDatumZahlung2(String datumZahlung2) throws SvmValidationException {
        datumZahlung2ModelAttribute.setNewValue(false, datumZahlung2, isBulkUpdate());
    }

    private final PreisModelAttribute betragZahlung2ModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_ZAHLUNG_2, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getBetragZahlung2();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setBetragZahlung2(value);
                }
            }
    );

    @Override
    public BigDecimal getBetragZahlung2() {
        return betragZahlung2ModelAttribute.getValue();
    }

    @Override
    public void setBetragZahlung2(String betragZahlung2) throws SvmValidationException {
        betragZahlung2ModelAttribute.setNewValue(false, betragZahlung2, isBulkUpdate());
    }

    private CalendarModelAttribute datumZahlung3ModelAttribute = new CalendarModelAttribute(
            this,
            Field.DATUM_ZAHLUNG_3, new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 3), new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX, Calendar.DECEMBER, 33),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return semesterrechnung.getDatumZahlung3();
                }

                @Override
                public void setValue(Calendar value) {
                    semesterrechnung.setDatumZahlung3(value);
                }
            }
    );

    @Override
    public Calendar getDatumZahlung3() {
        return datumZahlung3ModelAttribute.getValue();
    }

    @Override
    public void setDatumZahlung3(String datumZahlung3) throws SvmValidationException {
        datumZahlung3ModelAttribute.setNewValue(false, datumZahlung3, isBulkUpdate());
    }

    private final PreisModelAttribute betragZahlung3ModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_ZAHLUNG_3, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return semesterrechnung.getBetragZahlung3();
                }

                @Override
                public void setValue(BigDecimal value) {
                    semesterrechnung.setBetragZahlung3(value);
                }
            }
    );

    @Override
    public BigDecimal getBetragZahlung3() {
        return betragZahlung3ModelAttribute.getValue();
    }

    @Override
    public void setBetragZahlung3(String betragZahlung3) throws SvmValidationException {
        betragZahlung3ModelAttribute.setNewValue(false, betragZahlung3, isBulkUpdate());
    }

    private final StringModelAttribute bemerkungenModelAttribute = new StringModelAttribute(
            this,
            Field.BEMERKUNGEN, 0, 1000,
            new AttributeAccessor<String>() {
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
        rechnungsdatumNachrechnungModelAttribute.initValue(null);
        ermaessigungNachrechnungModelAttribute.initValue(null);
        ermaessigungsgrundNachrechnungModelAttribute.initValue(null);
        zuschlagNachrechnungModelAttribute.initValue(null);
        zuschlagsgrundNachrechnungModelAttribute.initValue(null);
        anzahlWochenNachrechnungModelAttribute.initValue(null);
        wochenbetragNachrechnungModelAttribute.initValue(null);
        datumZahlung1ModelAttribute.initValue(null);
        betragZahlung1ModelAttribute.initValue(null);
        datumZahlung2ModelAttribute.initValue(null);
        betragZahlung2ModelAttribute.initValue(null);
        datumZahlung3ModelAttribute.initValue(null);
        betragZahlung3ModelAttribute.initValue(null);
        bemerkungenModelAttribute.initValue(null);
    }
}
