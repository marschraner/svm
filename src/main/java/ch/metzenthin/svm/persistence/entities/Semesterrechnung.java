package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Stipendium;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Semesterrechnung")
@IdClass(SemesterrechnungId.class)
public class Semesterrechnung implements Comparable<Semesterrechnung> {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Angehoeriger rechnungsempfaenger;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Enumerated(EnumType.STRING)
    @Column(name = "stipendium", nullable = true)
    private Stipendium stipendium;

    @Column(name = "gratiskinder", nullable = false)
    private Boolean gratiskinder;

    @Temporal(TemporalType.DATE)
    @Column(name = "rechnungsdatum_vorrechnung", nullable = true)
    private Calendar rechnungsdatumVorrechnung;

    @Column(name = "ermaessigung_vorrechnung", nullable = false)
    private BigDecimal ermaessigungVorrechnung;

    @Column(name = "ermaessigungsgrund_vorrechnung", nullable = true)
    private String ermaessigungsgrundVorrechnung;

    @Column(name = "zuschlag_vorrechnung", nullable = false)
    private BigDecimal zuschlagVorrechnung;

    @Column(name = "zuschlagsgrund_vorrechnung", nullable = true)
    private String zuschlagsgrundVorrechnung;

    @Column(name = "anzahl_wochen_vorrechnung", nullable = false)
    private Integer anzahlWochenVorrechnung;

    @Column(name = "wochenbetrag_vorrechnung", nullable = false)
    private BigDecimal wochenbetragVorrechnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum_zahlung_1_vorrechnung", nullable = true)
    private Calendar datumZahlung1Vorrechnung;

    @Column(name = "betrag_zahlung_1_vorrechnung", nullable = true)
    private BigDecimal betragZahlung1Vorrechnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum_zahlung_2_vorrechnung", nullable = true)
    private Calendar datumZahlung2Vorrechnung;

    @Column(name = "betrag_zahlung_2_vorrechnung", nullable = true)
    private BigDecimal betragZahlung2Vorrechnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum_zahlung_3_vorrechnung", nullable = true)
    private Calendar datumZahlung3Vorrechnung;

    @Column(name = "betrag_zahlung_3_vorrechnung", nullable = true)
    private BigDecimal betragZahlung3Vorrechnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "rechnungsdatum_nachrechnung", nullable = true)
    private Calendar rechnungsdatumNachrechnung;

    @Column(name = "ermaessigung_nachrechnung", nullable = false)
    private BigDecimal ermaessigungNachrechnung;

    @Column(name = "ermaessigungsgrund_nachrechnung", nullable = true)
    private String ermaessigungsgrundNachrechnung;

    @Column(name = "zuschlag_nachrechnung", nullable = false)
    private BigDecimal zuschlagNachrechnung;

    @Column(name = "zuschlagsgrund_nachrechnung", nullable = true)
    private String zuschlagsgrundNachrechnung;

    @Column(name = "anzahl_wochen_nachrechnung", nullable = false)
    private Integer anzahlWochenNachrechnung;

    @Column(name = "wochenbetrag_nachrechnung", nullable = false)
    private BigDecimal wochenbetragNachrechnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum_zahlung_1_nachrechnung", nullable = true)
    private Calendar datumZahlung1Nachrechnung;

    @Column(name = "betrag_zahlung_1_nachrechnung", nullable = true)
    private BigDecimal betragZahlung1Nachrechnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum_zahlung_2_nachrechnung", nullable = true)
    private Calendar datumZahlung2Nachrechnung;

    @Column(name = "betrag_zahlung_2_nachrechnung", nullable = true)
    private BigDecimal betragZahlung2Nachrechnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum_zahlung_3_nachrechnung", nullable = true)
    private Calendar datumZahlung3Nachrechnung;

    @Column(name = "betrag_zahlung_3_nachrechnung", nullable = true)
    private BigDecimal betragZahlung3Nachrechnung;

    @Transient
    private boolean zuExportieren = true;

    @ManyToOne
    @JoinColumn(name = "code_id", nullable = true)
    private SemesterrechnungCode semesterrechnungCode;

    @Lob
    @Column(name = "bemerkungen", columnDefinition = "text", nullable = true)
    private String bemerkungen;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    public Semesterrechnung() {
    }

    public Semesterrechnung(Semester semester, Angehoeriger rechnungsempfaenger, Stipendium stipendium, Boolean gratiskinder,
                            Calendar rechnungsdatumVorrechnung, BigDecimal ermaessigungVorrechnung, String ermaessigungsgrundVorrechnung, BigDecimal zuschlagVorrechnung, String zuschlagsgrundVorrechnung, Integer anzahlWochenVorrechnung, BigDecimal wochenbetragVorrechnung, Calendar datumZahlung1Vorrechnung, BigDecimal betragZahlung1Vorrechnung, Calendar datumZahlung2Vorrechnung, BigDecimal betragZahlung2Vorrechnung, Calendar datumZahlung3Vorrechnung, BigDecimal betragZahlung3Vorrechnung,
                            Calendar rechnungsdatumNachrechnung, BigDecimal ermaessigungNachrechnung, String ermaessigungsgrundNachrechnung, BigDecimal zuschlagNachrechnung, String zuschlagsgrundNachrechnung, Integer anzahlWochenNachrechnung, BigDecimal wochenbetragNachrechnung, Calendar datumZahlung1Nachrechnung, BigDecimal betragZahlung1Nachrechnung, Calendar datumZahlung2Nachrechnung, BigDecimal betragZahlung2Nachrechnung, Calendar datumZahlung3Nachrechnung, BigDecimal betragZahlung3Nachrechnung, String bemerkungen, Boolean deleted) {
        this.semester = semester;
        this.rechnungsempfaenger = rechnungsempfaenger;
        this.stipendium = stipendium;
        this.gratiskinder = gratiskinder;
        this.rechnungsdatumVorrechnung = rechnungsdatumVorrechnung;
        this.ermaessigungVorrechnung = ermaessigungVorrechnung;
        this.ermaessigungsgrundVorrechnung = ermaessigungsgrundVorrechnung;
        this.zuschlagVorrechnung = zuschlagVorrechnung;
        this.zuschlagsgrundVorrechnung = zuschlagsgrundVorrechnung;
        this.anzahlWochenVorrechnung = anzahlWochenVorrechnung;
        this.wochenbetragVorrechnung = wochenbetragVorrechnung;
        this.datumZahlung1Vorrechnung = datumZahlung1Vorrechnung;
        this.betragZahlung1Vorrechnung = betragZahlung1Vorrechnung;
        this.datumZahlung2Vorrechnung = datumZahlung2Vorrechnung;
        this.betragZahlung2Vorrechnung = betragZahlung2Vorrechnung;
        this.datumZahlung3Vorrechnung = datumZahlung3Vorrechnung;
        this.betragZahlung3Vorrechnung = betragZahlung3Vorrechnung;
        this.rechnungsdatumNachrechnung = rechnungsdatumNachrechnung;
        this.ermaessigungNachrechnung = ermaessigungNachrechnung;
        this.ermaessigungsgrundNachrechnung = ermaessigungsgrundNachrechnung;
        this.zuschlagNachrechnung = zuschlagNachrechnung;
        this.zuschlagsgrundNachrechnung = zuschlagsgrundNachrechnung;
        this.anzahlWochenNachrechnung = anzahlWochenNachrechnung;
        this.wochenbetragNachrechnung = wochenbetragNachrechnung;
        this.datumZahlung1Nachrechnung = datumZahlung1Nachrechnung;
        this.betragZahlung1Nachrechnung = betragZahlung1Nachrechnung;
        this.datumZahlung2Nachrechnung = datumZahlung2Nachrechnung;
        this.betragZahlung2Nachrechnung = betragZahlung2Nachrechnung;
        this.datumZahlung3Nachrechnung = datumZahlung3Nachrechnung;
        this.betragZahlung3Nachrechnung = betragZahlung3Nachrechnung;
        this.bemerkungen = bemerkungen;
        this.deleted = deleted;
    }

    @Override
    public int compareTo(Semesterrechnung otherSemesterrechnung) {
        // aufsteigend nach Rechnungsempfänger-Schülern und absteigend nach Semester sortieren
        // Im Falle von Rechnungsempfängerwechseln kann ein Rechnungsempfänger keine Schüler haben
        Person person = (this.rechnungsempfaenger.getSchuelerRechnungsempfaenger().isEmpty() ? this.rechnungsempfaenger : this.rechnungsempfaenger.getSchuelerRechnungsempfaengerAsList().get(0));
        Person otherPerson = (otherSemesterrechnung.rechnungsempfaenger.getSchuelerRechnungsempfaenger().isEmpty() ? otherSemesterrechnung.rechnungsempfaenger : otherSemesterrechnung.rechnungsempfaenger.getSchuelerRechnungsempfaengerAsList().get(0));
        int result = person.compareTo(otherPerson);
        if (result == 0) {
            result = this.semester.compareTo(otherSemesterrechnung.semester);
        }
        return result;
    }

    public boolean isIdenticalWith(Semesterrechnung otherSemesterrechnung) {
        return otherSemesterrechnung != null
                && semester.isIdenticalWith(otherSemesterrechnung.getSemester())
                && rechnungsempfaenger.isIdenticalWith(otherSemesterrechnung.getRechnungsempfaenger());
    }

    public void copyAttributesFrom(Semesterrechnung otherSemesterrechnung) {
        this.stipendium = otherSemesterrechnung.stipendium;
        this.gratiskinder = otherSemesterrechnung.gratiskinder;
        this.rechnungsdatumVorrechnung = otherSemesterrechnung.rechnungsdatumVorrechnung;
        this.ermaessigungVorrechnung = otherSemesterrechnung.ermaessigungVorrechnung;
        this.ermaessigungsgrundVorrechnung = otherSemesterrechnung.ermaessigungsgrundVorrechnung;
        this.zuschlagVorrechnung = otherSemesterrechnung.zuschlagVorrechnung;
        this.zuschlagsgrundVorrechnung = otherSemesterrechnung.zuschlagsgrundVorrechnung;
        this.anzahlWochenVorrechnung = otherSemesterrechnung.anzahlWochenVorrechnung;
        this.wochenbetragVorrechnung = otherSemesterrechnung.wochenbetragVorrechnung;
        this.datumZahlung1Vorrechnung = otherSemesterrechnung.datumZahlung1Vorrechnung;
        this.betragZahlung1Vorrechnung = otherSemesterrechnung.betragZahlung1Vorrechnung;
        this.datumZahlung2Vorrechnung = otherSemesterrechnung.datumZahlung2Vorrechnung;
        this.betragZahlung2Vorrechnung = otherSemesterrechnung.betragZahlung2Vorrechnung;
        this.datumZahlung3Vorrechnung = otherSemesterrechnung.datumZahlung3Vorrechnung;
        this.betragZahlung3Vorrechnung = otherSemesterrechnung.betragZahlung3Vorrechnung;
        this.rechnungsdatumNachrechnung = otherSemesterrechnung.rechnungsdatumNachrechnung;
        this.ermaessigungNachrechnung = otherSemesterrechnung.ermaessigungNachrechnung;
        this.ermaessigungsgrundNachrechnung = otherSemesterrechnung.ermaessigungsgrundNachrechnung;
        this.zuschlagNachrechnung = otherSemesterrechnung.zuschlagNachrechnung;
        this.zuschlagsgrundNachrechnung = otherSemesterrechnung.zuschlagsgrundNachrechnung;
        this.anzahlWochenNachrechnung = otherSemesterrechnung.anzahlWochenNachrechnung;
        this.wochenbetragNachrechnung = otherSemesterrechnung.wochenbetragNachrechnung;
        this.datumZahlung1Nachrechnung = otherSemesterrechnung.datumZahlung1Nachrechnung;
        this.betragZahlung1Nachrechnung = otherSemesterrechnung.betragZahlung1Nachrechnung;
        this.datumZahlung2Nachrechnung = otherSemesterrechnung.datumZahlung2Nachrechnung;
        this.betragZahlung2Nachrechnung = otherSemesterrechnung.betragZahlung2Nachrechnung;
        this.datumZahlung3Nachrechnung = otherSemesterrechnung.datumZahlung3Nachrechnung;
        this.betragZahlung3Nachrechnung = otherSemesterrechnung.betragZahlung3Nachrechnung;
        this.bemerkungen = otherSemesterrechnung.getBemerkungen();
        this.deleted = otherSemesterrechnung.getDeleted();
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Angehoeriger getRechnungsempfaenger() {
        return rechnungsempfaenger;
    }

    public void setRechnungsempfaenger(Angehoeriger rechnungsempfaenger) {
        this.rechnungsempfaenger = rechnungsempfaenger;
    }

    public Stipendium getStipendium() {
        return stipendium;
    }

    public void setStipendium(Stipendium stipendium) {
        this.stipendium = stipendium;
    }

    public Boolean getGratiskinder() {
        return gratiskinder;
    }

    public void setGratiskinder(Boolean gratiskinder) {
        this.gratiskinder = gratiskinder;
    }

    public Calendar getRechnungsdatumVorrechnung() {
        return rechnungsdatumVorrechnung;
    }

    public void setRechnungsdatumVorrechnung(Calendar rechnungsdatumVorrechnung) {
        this.rechnungsdatumVorrechnung = rechnungsdatumVorrechnung;
    }

    public BigDecimal getErmaessigungVorrechnung() {
        return ermaessigungVorrechnung;
    }

    public void setErmaessigungVorrechnung(BigDecimal ermaessigungVorrechnung) {
        this.ermaessigungVorrechnung = ermaessigungVorrechnung;
    }

    public String getErmaessigungsgrundVorrechnung() {
        return ermaessigungsgrundVorrechnung;
    }

    public void setErmaessigungsgrundVorrechnung(String ermaessigungsgrundVorrechnung) {
        this.ermaessigungsgrundVorrechnung = ermaessigungsgrundVorrechnung;
    }

    public BigDecimal getZuschlagVorrechnung() {
        return zuschlagVorrechnung;
    }

    public void setZuschlagVorrechnung(BigDecimal zuschlagVorrechnung) {
        this.zuschlagVorrechnung = zuschlagVorrechnung;
    }

    public String getZuschlagsgrundVorrechnung() {
        return zuschlagsgrundVorrechnung;
    }

    public void setZuschlagsgrundVorrechnung(String zuschlagsgrundVorrechnung) {
        this.zuschlagsgrundVorrechnung = zuschlagsgrundVorrechnung;
    }

    public Integer getAnzahlWochenVorrechnung() {
        return anzahlWochenVorrechnung;
    }

    public void setAnzahlWochenVorrechnung(Integer anzahlWochenVorrechnung) {
        this.anzahlWochenVorrechnung = anzahlWochenVorrechnung;
    }

    public BigDecimal getWochenbetragVorrechnung() {
        return wochenbetragVorrechnung;
    }

    public void setWochenbetragVorrechnung(BigDecimal wochenbetragVorrechnung) {
        this.wochenbetragVorrechnung = wochenbetragVorrechnung;
    }

    public Calendar getDatumZahlung1Vorrechnung() {
        return datumZahlung1Vorrechnung;
    }

    public void setDatumZahlung1Vorrechnung(Calendar datumZahlung1Vorrechnung) {
        this.datumZahlung1Vorrechnung = datumZahlung1Vorrechnung;
    }

    public BigDecimal getBetragZahlung1Vorrechnung() {
        return betragZahlung1Vorrechnung;
    }

    public void setBetragZahlung1Vorrechnung(BigDecimal betragZahlung1Vorrechnung) {
        this.betragZahlung1Vorrechnung = betragZahlung1Vorrechnung;
    }

    public Calendar getDatumZahlung2Vorrechnung() {
        return datumZahlung2Vorrechnung;
    }

    public void setDatumZahlung2Vorrechnung(Calendar datumZahlung2Vorrechnung) {
        this.datumZahlung2Vorrechnung = datumZahlung2Vorrechnung;
    }

    public BigDecimal getBetragZahlung2Vorrechnung() {
        return betragZahlung2Vorrechnung;
    }

    public void setBetragZahlung2Vorrechnung(BigDecimal betragZahlung2Vorrechnung) {
        this.betragZahlung2Vorrechnung = betragZahlung2Vorrechnung;
    }

    public Calendar getDatumZahlung3Vorrechnung() {
        return datumZahlung3Vorrechnung;
    }

    public void setDatumZahlung3Vorrechnung(Calendar datumZahlung3Vorrechnung) {
        this.datumZahlung3Vorrechnung = datumZahlung3Vorrechnung;
    }

    public BigDecimal getBetragZahlung3Vorrechnung() {
        return betragZahlung3Vorrechnung;
    }

    public void setBetragZahlung3Vorrechnung(BigDecimal betragZahlung3Vorrechnung) {
        this.betragZahlung3Vorrechnung = betragZahlung3Vorrechnung;
    }

    public Calendar getRechnungsdatumNachrechnung() {
        return rechnungsdatumNachrechnung;
    }

    public void setRechnungsdatumNachrechnung(Calendar rechnungsdatumNachrechnung) {
        this.rechnungsdatumNachrechnung = rechnungsdatumNachrechnung;
    }

    public BigDecimal getErmaessigungNachrechnung() {
        return ermaessigungNachrechnung;
    }

    public void setErmaessigungNachrechnung(BigDecimal ermaessigungNachrechnung) {
        this.ermaessigungNachrechnung = ermaessigungNachrechnung;
    }

    public String getErmaessigungsgrundNachrechnung() {
        return ermaessigungsgrundNachrechnung;
    }

    public void setErmaessigungsgrundNachrechnung(String ermaessigungsgrundNachrechnung) {
        this.ermaessigungsgrundNachrechnung = ermaessigungsgrundNachrechnung;
    }

    public BigDecimal getZuschlagNachrechnung() {
        return zuschlagNachrechnung;
    }

    public void setZuschlagNachrechnung(BigDecimal zuschlagNachrechnung) {
        this.zuschlagNachrechnung = zuschlagNachrechnung;
    }

    public String getZuschlagsgrundNachrechnung() {
        return zuschlagsgrundNachrechnung;
    }

    public void setZuschlagsgrundNachrechnung(String zuschlagsgrundNachrechnung) {
        this.zuschlagsgrundNachrechnung = zuschlagsgrundNachrechnung;
    }

    public Integer getAnzahlWochenNachrechnung() {
        return anzahlWochenNachrechnung;
    }

    public void setAnzahlWochenNachrechnung(Integer anzahlWochenNachrechnung) {
        this.anzahlWochenNachrechnung = anzahlWochenNachrechnung;
    }

    public BigDecimal getWochenbetragNachrechnung() {
        return wochenbetragNachrechnung;
    }

    public void setWochenbetragNachrechnung(BigDecimal wochenbetragNachrechnung) {
        this.wochenbetragNachrechnung = wochenbetragNachrechnung;
    }

    public Calendar getDatumZahlung1Nachrechnung() {
        return datumZahlung1Nachrechnung;
    }

    public void setDatumZahlung1Nachrechnung(Calendar datumZahlung1Nachrechnung) {
        this.datumZahlung1Nachrechnung = datumZahlung1Nachrechnung;
    }

    public BigDecimal getBetragZahlung1Nachrechnung() {
        return betragZahlung1Nachrechnung;
    }

    public void setBetragZahlung1Nachrechnung(BigDecimal betragZahlung1Nachrechnung) {
        this.betragZahlung1Nachrechnung = betragZahlung1Nachrechnung;
    }

    public Calendar getDatumZahlung2Nachrechnung() {
        return datumZahlung2Nachrechnung;
    }

    public void setDatumZahlung2Nachrechnung(Calendar datumZahlung2Nachrechnung) {
        this.datumZahlung2Nachrechnung = datumZahlung2Nachrechnung;
    }

    public BigDecimal getBetragZahlung2Nachrechnung() {
        return betragZahlung2Nachrechnung;
    }

    public void setBetragZahlung2Nachrechnung(BigDecimal betragZahlung2Nachrechnung) {
        this.betragZahlung2Nachrechnung = betragZahlung2Nachrechnung;
    }

    public Calendar getDatumZahlung3Nachrechnung() {
        return datumZahlung3Nachrechnung;
    }

    public void setDatumZahlung3Nachrechnung(Calendar datumZahlung3Nachrechnung) {
        this.datumZahlung3Nachrechnung = datumZahlung3Nachrechnung;
    }

    public BigDecimal getBetragZahlung3Nachrechnung() {
        return betragZahlung3Nachrechnung;
    }

    public void setBetragZahlung3Nachrechnung(BigDecimal betragZahlung3Nachrechnung) {
        this.betragZahlung3Nachrechnung = betragZahlung3Nachrechnung;
    }

    public SemesterrechnungCode getSemesterrechnungCode() {
        return semesterrechnungCode;
    }

    public void setSemesterrechnungCode(SemesterrechnungCode semesterrechnungCode) {
        if (this.semesterrechnungCode != null) {
            deleteSemesterrechnungCode(this.semesterrechnungCode);
        }
        if (semesterrechnungCode != null) {
            semesterrechnungCode.getSemesterrechnungen().add(this);
        }
        this.semesterrechnungCode = semesterrechnungCode;
    }

    public void deleteSemesterrechnungCode(SemesterrechnungCode semesterrechnungCode) {
        semesterrechnungCode.getSemesterrechnungen().remove(this);
        this.semesterrechnungCode = null;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isZuExportieren() {
        return zuExportieren;
    }

    public void setZuExportieren(boolean zuExportieren) {
        this.zuExportieren = zuExportieren;
    }

    @Transient
    public BigDecimal getSchulgeldVorrechnung() {
        if (anzahlWochenVorrechnung == null || wochenbetragVorrechnung == null) {
            return null;
        }
        return new BigDecimal(anzahlWochenVorrechnung).multiply(wochenbetragVorrechnung);
    }

    @Transient
    public BigDecimal getErmaessigungStipendiumVorrechnung() {
        if (anzahlWochenVorrechnung == null || wochenbetragVorrechnung == null) {
            return null;
        }
        if (stipendium != null && stipendium != Stipendium.KEINES) {
            BigDecimal ermaessigungStipendiumVorrechnung = getSchulgeldVorrechnung();
            ermaessigungStipendiumVorrechnung = ermaessigungStipendiumVorrechnung.multiply(new BigDecimal(1- stipendium.getFaktor()));
            ermaessigungStipendiumVorrechnung = ermaessigungStipendiumVorrechnung.setScale(1, BigDecimal.ROUND_HALF_EVEN);  // Runden auf 10 Rappen
            return ermaessigungStipendiumVorrechnung.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        } else {
            return new BigDecimal("0.00");
        }
    }

    @Transient
    public BigDecimal getRechnungsbetragVorrechnung() {
        if (anzahlWochenVorrechnung == null || wochenbetragVorrechnung == null) {
            return null;
        }
        BigDecimal rechnungsbetragVorrechnung = getSchulgeldVorrechnung();
        // Stipendium
        if (stipendium != null && stipendium != Stipendium.KEINES) {
            rechnungsbetragVorrechnung = rechnungsbetragVorrechnung.subtract(getErmaessigungStipendiumVorrechnung());
        }
        // Gratiskinder
        if (gratiskinder != null && gratiskinder) {
            rechnungsbetragVorrechnung = new BigDecimal("0.00");
        }
        // Zuschlag / Ermässigung
        if (zuschlagVorrechnung != null) {
            rechnungsbetragVorrechnung = rechnungsbetragVorrechnung.add(zuschlagVorrechnung);
        }
        if (ermaessigungVorrechnung != null) {
            rechnungsbetragVorrechnung = rechnungsbetragVorrechnung.subtract(ermaessigungVorrechnung);
        }
        return rechnungsbetragVorrechnung;
    }

    @Transient
    public BigDecimal getRestbetragVorrechnung() {
        BigDecimal restbetragVorrechnung = null;
        BigDecimal rechnungsbetragVorrechnung = getRechnungsbetragVorrechnung();
        if (getRechnungsdatumVorrechnung() != null && rechnungsbetragVorrechnung != null) {
            restbetragVorrechnung = rechnungsbetragVorrechnung;
            if (betragZahlung1Vorrechnung != null) {
                restbetragVorrechnung = restbetragVorrechnung.subtract(betragZahlung1Vorrechnung);
            }
            if (betragZahlung2Vorrechnung != null) {
                restbetragVorrechnung = restbetragVorrechnung.subtract(betragZahlung2Vorrechnung);
            }
            if (betragZahlung3Vorrechnung != null) {
                restbetragVorrechnung = restbetragVorrechnung.subtract(betragZahlung3Vorrechnung);
            }
        }
        return restbetragVorrechnung;
    }


    @Transient
    public BigDecimal getSchulgeldNachrechnung() {
        if (anzahlWochenNachrechnung == null || wochenbetragNachrechnung == null) {
            return null;
        }
        return new BigDecimal(anzahlWochenNachrechnung).multiply(wochenbetragNachrechnung);
    }

    @Transient
    public BigDecimal getErmaessigungStipendiumNachrechnung() {
        if (anzahlWochenNachrechnung == null || wochenbetragNachrechnung == null) {
            return null;
        }
        if (stipendium != null && stipendium != Stipendium.KEINES) {
            BigDecimal ermaessigungStipendiumNachrechnung = getSchulgeldNachrechnung();
            ermaessigungStipendiumNachrechnung = ermaessigungStipendiumNachrechnung.multiply(new BigDecimal(1- stipendium.getFaktor()));
            ermaessigungStipendiumNachrechnung = ermaessigungStipendiumNachrechnung.setScale(1, BigDecimal.ROUND_HALF_EVEN);  // Runden auf 10 Rappen
            return ermaessigungStipendiumNachrechnung.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        } else {
            return new BigDecimal("0.00");
        }
    }

    @Transient
    public BigDecimal getRechnungsbetragNachrechnung() {
        if (anzahlWochenNachrechnung == null || wochenbetragNachrechnung == null) {
            return null;
        }
        BigDecimal rechnungsbetragNachrechnung = getSchulgeldNachrechnung();
        // Stipendium
        if (stipendium != null && stipendium != Stipendium.KEINES) {
            rechnungsbetragNachrechnung = rechnungsbetragNachrechnung.subtract(getErmaessigungStipendiumNachrechnung());
        }
        // Gratiskinder
        if (gratiskinder != null && gratiskinder) {
            rechnungsbetragNachrechnung = new BigDecimal("0.00");
        }
        // Zuschlag / Ermässigung
        if (zuschlagNachrechnung != null) {
            rechnungsbetragNachrechnung = rechnungsbetragNachrechnung.add(zuschlagNachrechnung);
        }
        if (ermaessigungNachrechnung != null) {
            rechnungsbetragNachrechnung = rechnungsbetragNachrechnung.subtract(ermaessigungNachrechnung);
        }
        return rechnungsbetragNachrechnung;
    }

    @Transient
    public BigDecimal getRestbetragNachrechnung() {
        BigDecimal restbetragNachrechnung = null;
        BigDecimal rechnungsbetragNachrechnung = getRechnungsbetragNachrechnung();
        if (getRechnungsdatumNachrechnung() != null && rechnungsbetragNachrechnung != null) {
            restbetragNachrechnung = rechnungsbetragNachrechnung;
            if (betragZahlung1Nachrechnung != null) {
                restbetragNachrechnung = restbetragNachrechnung.subtract(betragZahlung1Nachrechnung);
            }
            if (betragZahlung2Nachrechnung != null) {
                restbetragNachrechnung = restbetragNachrechnung.subtract(betragZahlung2Nachrechnung);
            }
            if (betragZahlung3Nachrechnung != null) {
                restbetragNachrechnung = restbetragNachrechnung.subtract(betragZahlung3Nachrechnung);
            }
        }
        return restbetragNachrechnung;
    }

    @Transient
    public BigDecimal getDifferenzSchulgeld() {
        boolean showVorrechnung = rechnungsdatumVorrechnung != null
                || (wochenbetragVorrechnung != null && wochenbetragVorrechnung.compareTo(BigDecimal.ZERO) != 0)
                || (ermaessigungVorrechnung != null && ermaessigungVorrechnung.compareTo(BigDecimal.ZERO) != 0)
                || (zuschlagVorrechnung != null && zuschlagVorrechnung.compareTo(BigDecimal.ZERO) != 0);
        if (showVorrechnung) {
            return getSchulgeldNachrechnung().subtract(getSchulgeldVorrechnung());
        } else {
            // für Rechnungen ohne Vorrechnung soll die Differenz des Schulgelds Null betragen
            return new BigDecimal("0.00");
        }
    }

    @Transient
    public boolean isNullrechnung() {
        return (ermaessigungVorrechnung == null || ermaessigungVorrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (zuschlagVorrechnung == null || zuschlagVorrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (wochenbetragVorrechnung == null || wochenbetragVorrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (betragZahlung1Vorrechnung == null || betragZahlung1Vorrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (betragZahlung2Vorrechnung == null || betragZahlung2Vorrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (betragZahlung3Vorrechnung == null || betragZahlung3Vorrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (ermaessigungNachrechnung == null || ermaessigungNachrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (zuschlagNachrechnung == null || zuschlagNachrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (wochenbetragNachrechnung == null || wochenbetragNachrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (betragZahlung1Nachrechnung == null || betragZahlung1Nachrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (betragZahlung2Nachrechnung == null || betragZahlung2Nachrechnung.compareTo(BigDecimal.ZERO) == 0) &&
                (betragZahlung3Nachrechnung == null || betragZahlung3Nachrechnung.compareTo(BigDecimal.ZERO) == 0);
    }

}
