package ch.metzenthin.svm.model.entities;

import javax.persistence.*;
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

    @Temporal(TemporalType.DATE)
    @Column(name = "dispensationsbeginn", nullable = false)
    private Calendar dispensationbeginn;

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

    public Dispensation(Calendar dispensationbeginn, Calendar dispensationsende, String grund) {
        this.dispensationbeginn = dispensationbeginn;
        this.dispensationsende = dispensationsende;
        this.grund = grund;
    }

    public Integer getDispensationId() {
        return dispensationId;
    }

    public void setDispensationId(Integer dispensationId) {
        this.dispensationId = dispensationId;
    }

    public Calendar getDispensationbeginn() {
        return dispensationbeginn;
    }

    public void setDispensationbeginn(Calendar dispensationbeginn) {
        this.dispensationbeginn = dispensationbeginn;
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
