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

    @Column(name = "ermaessigung", nullable = true)
    private BigDecimal ermaessigung;

    @Column(name = "ermaessigungsgrund", nullable = true)
    private String ermaessigungsgrund;

    @Column(name = "zuschlag", nullable = true)
    private BigDecimal zuschlag;

    @Column(name = "zuschlagsgrund", nullable = true)
    private String zuschlagsgrund;

    @Enumerated(EnumType.STRING)
    @Column(name = "stipendium", nullable = true)
    private Stipendium stipendium;

    @Column(name = "gratiskinder", nullable = false)
    private Boolean gratiskinder;

    @Column(name = "anzahl_wochen", nullable = true)
    private Integer anzahlWochen;

    @Column(name = "wochenbetrag", nullable = true)
    private BigDecimal wochenbetrag;

    @Temporal(TemporalType.DATE)
    @Column(name = "rechnungsdatum", nullable = true)
    private Calendar rechnungsdatum;

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

    public Semesterrechnung(Semester semester, Angehoeriger rechnungsempfaenger, BigDecimal ermaessigung, String ermaessigungsgrund, BigDecimal zuschlag, String zuschlagsgrund, Stipendium stipendium, Boolean gratiskinder, int anzahlWochen, BigDecimal wochenbetrag, Calendar rechnungsdatum, Calendar datumZahlung1, BigDecimal betragZahlung1, Calendar datumZahlung2, BigDecimal betragZahlung2, Calendar datumZahlung3, BigDecimal betragZahlung3, String bemerkungen) {
        this.semester = semester;
        this.rechnungsempfaenger = rechnungsempfaenger;
        this.ermaessigung = ermaessigung;
        this.ermaessigungsgrund = ermaessigungsgrund;
        this.zuschlag = zuschlag;
        this.zuschlagsgrund = zuschlagsgrund;
        this.stipendium = stipendium;
        this.gratiskinder = gratiskinder;
        this.anzahlWochen = anzahlWochen;
        this.wochenbetrag = wochenbetrag;
        this.rechnungsdatum = rechnungsdatum;
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
        // aufsteigend nach Rechnungsempfänger sortieren
        return this.rechnungsempfaenger.compareTo(otherSemesterrechnung.rechnungsempfaenger);
    }

    public boolean isIdenticalWith(Semesterrechnung otherSemesterrechnung) {
        return otherSemesterrechnung != null
                && semester.isIdenticalWith(otherSemesterrechnung.getSemester())
                && rechnungsempfaenger.isIdenticalWith(otherSemesterrechnung.getRechnungsempfaenger());
    }

    public void copyAttributesFrom(Semesterrechnung otherSemesterrechnung) {
        this.ermaessigung = otherSemesterrechnung.ermaessigung;
        this.ermaessigungsgrund = otherSemesterrechnung.ermaessigungsgrund;
        this.zuschlag = otherSemesterrechnung.zuschlag;
        this.zuschlagsgrund = otherSemesterrechnung.zuschlagsgrund;
        this.stipendium = otherSemesterrechnung.stipendium;
        this.gratiskinder = otherSemesterrechnung.gratiskinder;
        this.anzahlWochen = otherSemesterrechnung.anzahlWochen;
        this.wochenbetrag = otherSemesterrechnung.wochenbetrag;
        this.rechnungsdatum = otherSemesterrechnung.rechnungsdatum;
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

    public BigDecimal getErmaessigung() {
        return ermaessigung;
    }

    public void setErmaessigung(BigDecimal ermaessigung) {
        this.ermaessigung = ermaessigung;
    }

    public String getErmaessigungsgrund() {
        return ermaessigungsgrund;
    }

    public void setErmaessigungsgrund(String ermaessigungsgrund) {
        this.ermaessigungsgrund = ermaessigungsgrund;
    }

    public BigDecimal getZuschlag() {
        return zuschlag;
    }

    public void setZuschlag(BigDecimal zuschlag) {
        this.zuschlag = zuschlag;
    }

    public String getZuschlagsgrund() {
        return zuschlagsgrund;
    }

    public void setZuschlagsgrund(String zuschlagsgrund) {
        this.zuschlagsgrund = zuschlagsgrund;
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

    public Integer getAnzahlWochen() {
        return anzahlWochen;
    }

    public void setAnzahlWochen(Integer anzahlWochen) {
        this.anzahlWochen = anzahlWochen;
    }

    public BigDecimal getWochenbetrag() {
        return wochenbetrag;
    }

    public void setWochenbetrag(BigDecimal wochenbetrag) {
        this.wochenbetrag = wochenbetrag;
    }

    public Calendar getRechnungsdatum() {
        return rechnungsdatum;
    }

    public void setRechnungsdatum(Calendar rechnungsdatum) {
        this.rechnungsdatum = rechnungsdatum;
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
    public BigDecimal getSchulgeld() {
        // Gratiskinder haben immer 0.00 als Schulgeld
        if (gratiskinder) {
            return new BigDecimal("0.00");
        }
        if (anzahlWochen == null || wochenbetrag == null) {
            return null;
        }
        // Normale Rechnungen
        BigDecimal schulgeld = new BigDecimal(anzahlWochen).multiply(wochenbetrag);
        // Stipendium
        if (stipendium != null && stipendium != Stipendium.KEINES) {
            schulgeld = schulgeld.multiply(new BigDecimal(stipendium.getFaktor()));
            schulgeld = schulgeld.setScale(1, BigDecimal.ROUND_HALF_EVEN);  // Runden auf 10 Rappen
            schulgeld = schulgeld.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
        // Zuschlag / Ermässigung
        if (zuschlag != null) {
            schulgeld = schulgeld.add(zuschlag);
        }
        if (ermaessigung != null) {
            schulgeld = schulgeld.subtract(ermaessigung);
        }
        return schulgeld;
    }

    @Transient
    public BigDecimal getRestbetrag() {
        BigDecimal restbetrag = getSchulgeld();
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
