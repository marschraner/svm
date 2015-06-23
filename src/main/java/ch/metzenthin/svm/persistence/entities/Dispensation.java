package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Dispensation")
public class Dispensation {

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
        dispensationSb.append("Dispensationsbeginn: ").append(String.format("%1$td.%1$tm.%1$tY", dispensationsbeginn));
        if (dispensationsende != null) {
            dispensationSb.append(", Dispensationsende: ").append(String.format("%1$td.%1$tm.%1$tY", dispensationsende));
        }
        if (grund != null) {
            dispensationSb.append(", Grund: ").append(grund);
        }
        return dispensationSb.toString();
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
