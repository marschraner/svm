package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Anmeldung")
public class Anmeldung implements Comparable<Anmeldung> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anmeldung_id")
    private Integer anmeldungId;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Temporal(TemporalType.DATE)
    @Column(name = "anmeldedatum", nullable = false)
    private Calendar anmeldedatum;

    @Temporal(TemporalType.DATE)
    @Column(name = "abmeldedatum", nullable = true)
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
        anmeldungSb.append("Anmeldedatum: ").append(String.format("%1$td.%1$tm.%1$tY", anmeldedatum));
        if (abmeldedatum != null) {
            anmeldungSb.append(", Abmeldedatum: ").append(String.format("%1$td.%1$tm.%1$tY", abmeldedatum));
        }
        return anmeldungSb.toString();
    }

    @Override
    public int compareTo(Anmeldung otherAnmeldung) {
        // absteigend nach Anmeldedatum und Abmeldedatum sortieren, d.h. neuste Einträge zuoberst
        int result = -anmeldedatum.compareTo(otherAnmeldung.anmeldedatum);
        if (result == 0) {
            if (abmeldedatum != null && otherAnmeldung.abmeldedatum != null) {
                result = -abmeldedatum.compareTo(otherAnmeldung.abmeldedatum);
            } else if (abmeldedatum != null) {
                result = 1;
            } else if (otherAnmeldung.abmeldedatum != null) {
                result = -1;
            }
        }
        return result;
    }

    public Integer getAnmeldungId() {
        return anmeldungId;
    }

    public void setAnmeldungId(Integer anmeldungId) {
        this.anmeldungId = anmeldungId;
    }

    public Calendar getAnmeldedatum() {
        return anmeldedatum;
    }

    public void setAnmeldedatum(Calendar anmeldungbeginn) {
        this.anmeldedatum = anmeldungbeginn;
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
