package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Dispensation")
public class Dispensation implements Comparable<Dispensation> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispensation_id")
    private Integer dispensationId;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Temporal(TemporalType.DATE)
    @Column(name = "dispensationsbeginn", nullable = false)
    private Calendar dispensationsbeginn;

    @Temporal(TemporalType.DATE)
    @Column(name = "dispensationsende", nullable = true)
    private Calendar dispensationsende;

    @Lob
    @Column(name = "voraussichtliche_dauer", columnDefinition = "text", nullable = true)
    private String voraussichtlicheDauer;

    @Lob
    @Column(name = "grund", columnDefinition = "text", nullable = false)
    private String grund;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "schueler_id", nullable = false)
    private Schueler schueler;

    public Dispensation() {
    }

    public Dispensation(Calendar dispensationsbeginn, Calendar dispensationsende, String grund) {
        this.dispensationsbeginn = dispensationsbeginn;
        this.dispensationsende = dispensationsende;
        this.grund = grund;
    }

    @Override
    public String toString() {
        StringBuilder dispensationSb = new StringBuilder();
        if (dispensationsende == null) {
            dispensationSb.append("Dispensiert seit ").append(String.format("%1$td.%1$tm.%1$tY", dispensationsbeginn));
        } else {
            dispensationSb.append("Dispensiert ").append(String.format("%1$td.%1$tm.%1$tY", dispensationsbeginn));
            dispensationSb.append(" - ").append(String.format("%1$td.%1$tm.%1$tY", dispensationsende));
        }
        if (voraussichtlicheDauer != null) {
            dispensationSb.append(". Voraussichtliche Dauer: ").append(voraussichtlicheDauer);
        }
        dispensationSb.append(". Grund: ").append(grund);
        return dispensationSb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dispensation)) return false;

        Dispensation that = (Dispensation) o;

        if (!dispensationsbeginn.equals(that.dispensationsbeginn)) return false;
        if (dispensationsende != null ? !dispensationsende.equals(that.dispensationsende) : that.dispensationsende != null)
            return false;
        if (!grund.equals(that.grund)) return false;
        return schueler.equals(that.schueler);

    }

    @Override
    public int hashCode() {
        int result = dispensationsbeginn.hashCode();
        result = 31 * result + (dispensationsende != null ? dispensationsende.hashCode() : 0);
        result = 31 * result + grund.hashCode();
        result = 31 * result + schueler.hashCode();
        return result;
    }

    @Override
    public int compareTo(Dispensation otherDispensation) {
        // absteigend nach Dispensationsbeginn und Dispensationsende sortieren, d.h. neuste Einträge zuoberst
        int result = otherDispensation.dispensationsbeginn.compareTo(dispensationsbeginn);
        if (result == 0) {
            if (dispensationsende != null && otherDispensation.dispensationsende != null) {
                result = otherDispensation.dispensationsende.compareTo(dispensationsende);
            } else if (dispensationsende != null) {
                result = 1;
            } else if (otherDispensation.dispensationsende != null) {
                result = -1;
            }
        }
        return result;
    }

    public Integer getDispensationId() {
        return dispensationId;
    }

    public void setDispensationId(Integer dispensationId) {
        this.dispensationId = dispensationId;
    }

    public Calendar getDispensationsbeginn() {
        return dispensationsbeginn;
    }

    public void setDispensationsbeginn(Calendar dispensationbeginn) {
        this.dispensationsbeginn = dispensationbeginn;
    }

    public Calendar getDispensationsende() {
        return dispensationsende;
    }

    public void setDispensationsende(Calendar dispensationsende) {
        this.dispensationsende = dispensationsende;
    }

    public String getVoraussichtlicheDauer() {
        return voraussichtlicheDauer;
    }

    public void setVoraussichtlicheDauer(String voraussichtlicheDauer) {
        this.voraussichtlicheDauer = voraussichtlicheDauer;
    }

    public String getGrund() {
        return grund;
    }

    public void setGrund(String grund) {
        this.grund = grund;
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public void setSchueler(Schueler schueler) {
        this.schueler = schueler;
    }
}
