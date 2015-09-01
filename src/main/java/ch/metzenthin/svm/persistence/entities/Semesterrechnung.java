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

    @Column(name = "ermaessigung_vorrechnung", nullable = true)
    private BigDecimal ermaessigungVorrechnung;

    @Column(name = "ermaessigungsgrund_vorrechnung", nullable = true)
    private String ermaessigungsgrundVorrechnung;

    @Column(name = "zuschlag_vorrechnung", nullable = true)
    private BigDecimal zuschlagVorrechnung;

    @Column(name = "zuschlagsgrund_vorrechnung", nullable = true)
    private String zuschlagsgrundVorrechnung;

    @Column(name = "anzahl_wochen_vorrechnung", nullable = true)
    private Integer anzahlWochenVorrechnung;

    @Column(name = "wochenbetrag_vorrechnung", nullable = true)
    private BigDecimal wochenbetragVorrechnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "rechnungsdatum_nachrechnung", nullable = true)
    private Calendar rechnungsdatumNachrechnung;

    @Column(name = "ermaessigung_nachrechnung", nullable = true)
    private BigDecimal ermaessigungNachrechnung;

    @Column(name = "ermaessigungsgrund_nachrechnung", nullable = true)
    private String ermaessigungsgrundNachrechnung;

    @Column(name = "zuschlag_nachrechnung", nullable = true)
    private BigDecimal zuschlagNachrechnung;

    @Column(name = "zuschlagsgrund_nachrechnung", nullable = true)
    private String zuschlagsgrundNachrechnung;

    @Column(name = "anzahl_wochen_nachrechnung", nullable = true)
    private Integer anzahlWochenNachrechnung;

    @Column(name = "wochenbetrag_nachrechnung", nullable = true)
    private BigDecimal wochenbetragNachrechnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum_zahlung_1", nullable = true)
    private Calendar datumZahlung1;

    @Column(name = "betrag_zahlung_1", nullable = true)
    private BigDecimal betragZahlung1;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum_zahlung_2", nullable = true)
    private Calendar datumZahlung2;

    @Column(name = "betrag_zahlung_2", nullable = true)
    private BigDecimal betragZahlung2;

    @Temporal(TemporalType.DATE)
    @Column(name = "datum_zahlung_3", nullable = true)
    private Calendar datumZahlung3;

    @Column(name = "betrag_zahlung_3", nullable = true)
    private BigDecimal betragZahlung3;

    @ManyToOne
    @JoinColumn(name = "code_id", nullable = true)
    private SemesterrechnungCode semesterrechnungCode;

    @Lob
    @Column(name = "bemerkungen", columnDefinition = "text", nullable = true)
    private String bemerkungen;

    public Semesterrechnung() {
    }

    public Semesterrechnung(Semester semester, Angehoeriger rechnungsempfaenger, Stipendium stipendium, Boolean gratiskinder, Calendar rechnungsdatumVorrechnung, BigDecimal ermaessigungVorrechnung, String ermaessigungsgrundVorrechnung, BigDecimal zuschlagVorrechnung, String zuschlagsgrundVorrechnung, Integer anzahlWochenVorrechnung, BigDecimal wochenbetragVorrechnung, Calendar rechnungsdatumNachrechnung, BigDecimal ermaessigungNachrechnung, String ermaessigungsgrundNachrechnung, BigDecimal zuschlagNachrechnung, String zuschlagsgrundNachrechnung, Integer anzahlWochenNachrechnung, BigDecimal wochenbetragNachrechnung, Calendar datumZahlung1, BigDecimal betragZahlung1, Calendar datumZahlung2, BigDecimal betragZahlung2, Calendar datumZahlung3, BigDecimal betragZahlung3, String bemerkungen) {
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
        this.rechnungsdatumNachrechnung = rechnungsdatumNachrechnung;
        this.ermaessigungNachrechnung = ermaessigungNachrechnung;
        this.ermaessigungsgrundNachrechnung = ermaessigungsgrundNachrechnung;
        this.zuschlagNachrechnung = zuschlagNachrechnung;
        this.zuschlagsgrundNachrechnung = zuschlagsgrundNachrechnung;
        this.anzahlWochenNachrechnung = anzahlWochenNachrechnung;
        this.wochenbetragNachrechnung = wochenbetragNachrechnung;
        this.datumZahlung1 = datumZahlung1;
        this.betragZahlung1 = betragZahlung1;
        this.datumZahlung2 = datumZahlung2;
        this.betragZahlung2 = betragZahlung2;
        this.datumZahlung3 = datumZahlung3;
        this.betragZahlung3 = betragZahlung3;
        this.bemerkungen = bemerkungen;
    }

    @Override
    public int compareTo(Semesterrechnung otherSemesterrechnung) {
        // aufsteigend nach Rechnungsempfänger und absteigend nach Semester sortieren
        int result = this.rechnungsempfaenger.compareTo(otherSemesterrechnung.rechnungsempfaenger);
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
        this.rechnungsdatumNachrechnung = otherSemesterrechnung.rechnungsdatumNachrechnung;
        this.ermaessigungNachrechnung = otherSemesterrechnung.ermaessigungNachrechnung;
        this.ermaessigungsgrundNachrechnung = otherSemesterrechnung.ermaessigungsgrundNachrechnung;
        this.zuschlagNachrechnung = otherSemesterrechnung.zuschlagNachrechnung;
        this.zuschlagsgrundNachrechnung = otherSemesterrechnung.zuschlagsgrundNachrechnung;
        this.anzahlWochenNachrechnung = otherSemesterrechnung.anzahlWochenNachrechnung;
        this.wochenbetragNachrechnung = otherSemesterrechnung.wochenbetragNachrechnung;
        this.datumZahlung1 = otherSemesterrechnung.datumZahlung1;
        this.betragZahlung1 = otherSemesterrechnung.betragZahlung1;
        this.datumZahlung2 = otherSemesterrechnung.datumZahlung2;
        this.betragZahlung2 = otherSemesterrechnung.betragZahlung2;
        this.datumZahlung3 = otherSemesterrechnung.datumZahlung3;
        this.betragZahlung3 = otherSemesterrechnung.betragZahlung3;
        this.bemerkungen = otherSemesterrechnung.getBemerkungen();
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

    public Calendar getDatumZahlung1() {
        return datumZahlung1;
    }

    public void setDatumZahlung1(Calendar datumZahlung1) {
        this.datumZahlung1 = datumZahlung1;
    }

    public BigDecimal getBetragZahlung1() {
        return betragZahlung1;
    }

    public void setBetragZahlung1(BigDecimal betragZahlung1) {
        this.betragZahlung1 = betragZahlung1;
    }

    public Calendar getDatumZahlung2() {
        return datumZahlung2;
    }

    public void setDatumZahlung2(Calendar datumZahlung2) {
        this.datumZahlung2 = datumZahlung2;
    }

    public BigDecimal getBetragZahlung2() {
        return betragZahlung2;
    }

    public void setBetragZahlung2(BigDecimal betragZahlung2) {
        this.betragZahlung2 = betragZahlung2;
    }

    public Calendar getDatumZahlung3() {
        return datumZahlung3;
    }

    public void setDatumZahlung3(Calendar datumZahlung3) {
        this.datumZahlung3 = datumZahlung3;
    }

    public BigDecimal getBetragZahlung3() {
        return betragZahlung3;
    }

    public void setBetragZahlung3(BigDecimal betragZahlung3) {
        this.betragZahlung3 = betragZahlung3;
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

    @Transient
    public BigDecimal getSchulgeldVorrechnung() {
        // Gratiskinder haben immer 0.00 als Schulgeld
        if (gratiskinder) {
            return new BigDecimal("0.00");
        }
        if (anzahlWochenVorrechnung == null || wochenbetragVorrechnung == null) {
            return null;
        }
        // Normale Rechnungen
        BigDecimal schulgeldVorrechnung = new BigDecimal(anzahlWochenVorrechnung).multiply(wochenbetragVorrechnung);
        // Stipendium
        if (stipendium != null && stipendium != Stipendium.KEINES) {
            schulgeldVorrechnung = schulgeldVorrechnung.multiply(new BigDecimal(stipendium.getFaktor()));
            schulgeldVorrechnung = schulgeldVorrechnung.setScale(1, BigDecimal.ROUND_HALF_EVEN);  // Runden auf 10 Rappen
            schulgeldVorrechnung = schulgeldVorrechnung.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
        // Zuschlag / Ermässigung
        if (zuschlagVorrechnung != null) {
            schulgeldVorrechnung = schulgeldVorrechnung.add(zuschlagVorrechnung);
        }
        if (ermaessigungVorrechnung != null) {
            schulgeldVorrechnung = schulgeldVorrechnung.subtract(ermaessigungVorrechnung);
        }
        return schulgeldVorrechnung;
    }

    @Transient
    public BigDecimal getSchulgeldNachrechnung() {
        // Gratiskinder haben immer 0.00 als Schulgeld
        if (gratiskinder) {
            return new BigDecimal("0.00");
        }
        if (anzahlWochenNachrechnung == null || wochenbetragNachrechnung == null) {
            return null;
        }
        // Normale Rechnungen
        BigDecimal schulgeldNachrechnung = new BigDecimal(anzahlWochenNachrechnung).multiply(wochenbetragNachrechnung);
        // Stipendium
        if (stipendium != null && stipendium != Stipendium.KEINES) {
            schulgeldNachrechnung = schulgeldNachrechnung.multiply(new BigDecimal(stipendium.getFaktor()));
            schulgeldNachrechnung = schulgeldNachrechnung.setScale(1, BigDecimal.ROUND_HALF_EVEN);  // Runden auf 10 Rappen
            schulgeldNachrechnung = schulgeldNachrechnung.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
        // Zuschlag / Ermässigung
        if (zuschlagNachrechnung != null) {
            schulgeldNachrechnung = schulgeldNachrechnung.add(zuschlagNachrechnung);
        }
        if (ermaessigungNachrechnung != null) {
            schulgeldNachrechnung = schulgeldNachrechnung.subtract(ermaessigungNachrechnung);
        }
        return schulgeldNachrechnung;
    }

    @Transient
    public BigDecimal getSchulgeldDifferenzNachrechnungVorrechnung() {
        if (getSchulgeldNachrechnung() != null && getSchulgeldVorrechnung() != null) {
            return getSchulgeldNachrechnung().subtract(getSchulgeldVorrechnung());
        }
        return null;
    }

    @Transient
    public BigDecimal getRestbetrag() {
        BigDecimal restbetrag = (getSchulgeldNachrechnung() == null ? getSchulgeldVorrechnung() : getSchulgeldNachrechnung());
        if (betragZahlung1 != null) {
            restbetrag = restbetrag.subtract(betragZahlung1);
        }
        if (betragZahlung2 != null) {
            restbetrag = restbetrag.subtract(betragZahlung2);
        }
        if (betragZahlung3 != null) {
            restbetrag = restbetrag.subtract(betragZahlung3);
        }
        return restbetrag;
    }
}
