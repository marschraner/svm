package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.utils.SvmProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;

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

    @Transient
    private boolean neusteZuoberst;

    public Dispensation() {
        Properties svmProperties = SvmProperties.getSvmProperties();
        neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
    }

    public Dispensation(Calendar dispensationsbeginn, Calendar dispensationsende, String voraussichtlicheDauer, String grund) {
        this();
        this.dispensationsbeginn = dispensationsbeginn;
        this.dispensationsende = dispensationsende;
        this.voraussichtlicheDauer = voraussichtlicheDauer;
        this.grund = grund;
    }

    public boolean isIdenticalWith(Dispensation otherDispensation) {
        return otherDispensation != null
                && ((dispensationsbeginn == null && otherDispensation.getDispensationsbeginn() == null) || (dispensationsbeginn != null && dispensationsbeginn.equals(otherDispensation.getDispensationsbeginn())))
                && ((dispensationsende == null && otherDispensation.getDispensationsende() == null) || (dispensationsende != null && dispensationsende.equals(otherDispensation.getDispensationsende())))
                && ((voraussichtlicheDauer == null && otherDispensation.getVoraussichtlicheDauer() == null) || (voraussichtlicheDauer != null && voraussichtlicheDauer.equals(otherDispensation.getVoraussichtlicheDauer())))
                && ((grund == null && otherDispensation.getGrund() == null) || (grund != null && grund.equals(otherDispensation.getGrund())));
    }

    public void copyAttributesFrom(Dispensation otherDispensation) {
        this.dispensationsbeginn = otherDispensation.getDispensationsbeginn();
        this.dispensationsende = otherDispensation.getDispensationsende();
        this.voraussichtlicheDauer = otherDispensation.getVoraussichtlicheDauer();
        this.grund = otherDispensation.getGrund();
    }

    @Override
    public String toString() {
        StringBuilder dispensationSb = new StringBuilder();
        if (dispensationsende == null) {
            dispensationSb.append("Seit ").append(String.format("%1$td.%1$tm.%1$tY", dispensationsbeginn));
        } else {
            dispensationSb.append(String.format("%1$td.%1$tm.%1$tY", dispensationsbeginn));
            dispensationSb.append(" - ").append(String.format("%1$td.%1$tm.%1$tY", dispensationsende));
        }
        if (voraussichtlicheDauer != null) {
            dispensationSb.append(", voraussichtliche Dauer: ").append(voraussichtlicheDauer);
        }
        dispensationSb.append(", Grund: ").append(grund);
        return dispensationSb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dispensation that = (Dispensation) o;

        if (!dispensationsbeginn.equals(that.dispensationsbeginn)) return false;
        if (dispensationsende != null ? !dispensationsende.equals(that.dispensationsende) : that.dispensationsende != null)
            return false;
        if (voraussichtlicheDauer != null ? !voraussichtlicheDauer.equals(that.voraussichtlicheDauer) : that.voraussichtlicheDauer != null)
            return false;
        return grund.equals(that.grund);

    }

    @Override
    public int hashCode() {
        int result = dispensationsbeginn.hashCode();
        result = 31 * result + (dispensationsende != null ? dispensationsende.hashCode() : 0);
        result = 31 * result + (voraussichtlicheDauer != null ? voraussichtlicheDauer.hashCode() : 0);
        result = 31 * result + grund.hashCode();
        return result;
    }

    @Override
    public int compareTo(Dispensation otherDispensation) {
        // absteigend nach Dispensationsbeginn und Dispensationsende sortieren, d.h. neuste Einträge zuoberst
        int result;
        if (neusteZuoberst) {
            result = otherDispensation.dispensationsbeginn.compareTo(dispensationsbeginn);
            if (result == 0) {
                if (dispensationsende != null && otherDispensation.dispensationsende != null) {
                    result = otherDispensation.dispensationsende.compareTo(dispensationsende);
                } else if (dispensationsende != null) {
                    result = 1;
                } else if (otherDispensation.dispensationsende != null) {
                    result = -1;
                }
            }
        } else {
            result = dispensationsbeginn.compareTo(otherDispensation.dispensationsbeginn);
            if (result == 0) {
                if (dispensationsende != null && otherDispensation.dispensationsende != null) {
                    result = dispensationsende.compareTo(otherDispensation.dispensationsende);
                } else if (dispensationsende != null) {
                    result = -1;
                } else if (otherDispensation.dispensationsende != null) {
                    result = 1;
                }
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
