package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Anmeldung")
public class Anmeldung {

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

    public Integer getAnmeldungId() {
        return anmeldungId;
    }

    public void setAnmeldungId(Integer dispensationId) {
        this.anmeldungId = dispensationId;
    }

    public Calendar getAnmeldedatum() {
        return anmeldedatum;
    }

    public void setAnmeldedatum(Calendar dispensationbeginn) {
        this.anmeldedatum = dispensationbeginn;
    }

    public Calendar getAbmeldedatum() {
        return abmeldedatum;
    }

    public void setAbmeldedatum(Calendar dispensationsende) {
        this.abmeldedatum = dispensationsende;
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public void setSchueler(Schueler schueler) {
        this.schueler = schueler;
    }

}
