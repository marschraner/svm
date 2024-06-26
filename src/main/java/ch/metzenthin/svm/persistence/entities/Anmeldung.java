package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Anmeldung")
public class Anmeldung implements Comparable<Anmeldung> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anmeldung_id")
    private Integer anmeldungId;

    @SuppressWarnings("unused")
    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Temporal(TemporalType.DATE)
    @Column(name = "anmeldedatum", nullable = false)
    private Calendar anmeldedatum;

    @Temporal(TemporalType.DATE)
    @Column(name = "abmeldedatum")
    private Calendar abmeldedatum;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "schueler_id", nullable = false)
    private Schueler schueler;

    public Anmeldung() {
    }

    public Anmeldung(Calendar anmeldedatum, Calendar abmeldedatum) {
        this.anmeldedatum = anmeldedatum;
        this.abmeldedatum = abmeldedatum;
    }

    @Override
    public String toString() {
        StringBuilder anmeldungSb = new StringBuilder();
        anmeldungSb.append(String.format("%1$td.%1$tm.%1$tY", anmeldedatum));
        if (abmeldedatum != null) {
            // Eine Anmeldung dauert bis und mit Abmeldedatum
            anmeldungSb.append(" - ").append(String.format("%1$td.%1$tm.%1$tY", abmeldedatum));
        }
        return anmeldungSb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Anmeldung anmeldung)) return false;

        return anmeldedatum.equals(anmeldung.anmeldedatum)
                && Objects.equals(abmeldedatum, anmeldung.abmeldedatum)
                && schueler.equals(anmeldung.schueler);
    }

    @Override
    public int hashCode() {
        int result = anmeldedatum.hashCode();
        result = 31 * result + (abmeldedatum != null ? abmeldedatum.hashCode() : 0);
        result = 31 * result + schueler.hashCode();
        return result;
    }

    @Override
    public int compareTo(Anmeldung otherAnmeldung) {
        // absteigend nach Anmeldedatum und Abmeldedatum sortieren, d.h. neuste Einträge zuoberst
        int result = otherAnmeldung.anmeldedatum.compareTo(anmeldedatum);
        if (result == 0) {
            if (abmeldedatum != null && otherAnmeldung.abmeldedatum != null) {
                result = otherAnmeldung.abmeldedatum.compareTo(abmeldedatum);
            } else if (abmeldedatum != null) {
                result = 1;
            } else if (otherAnmeldung.abmeldedatum != null) {
                result = -1;
            }
        }
        return result;
    }

    public void copyFieldValuesFrom(Anmeldung anmeldungFrom) {
        anmeldedatum = anmeldungFrom.getAnmeldedatum();
        abmeldedatum = anmeldungFrom.getAbmeldedatum();
    }

    public boolean isInPast() {
        return (abmeldedatum != null) && new GregorianCalendar().after(abmeldedatum);
    }

    public Integer getAnmeldungId() {
        return anmeldungId;
    }

    @SuppressWarnings("unused")
    public void setAnmeldungId(Integer anmeldungId) {
        this.anmeldungId = anmeldungId;
    }

    public Calendar getAnmeldedatum() {
        return anmeldedatum;
    }

    public void setAnmeldedatum(Calendar anmeldedatum) {
        this.anmeldedatum = anmeldedatum;
    }

    public Calendar getAbmeldedatum() {
        return abmeldedatum;
    }

    public void setAbmeldedatum(Calendar abmeldedatum) {
        this.abmeldedatum = abmeldedatum;
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public void setSchueler(Schueler schueler) {
        this.schueler = schueler;
    }

}
