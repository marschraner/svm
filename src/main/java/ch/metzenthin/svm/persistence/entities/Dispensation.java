package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.utils.SvmProperties;
import jakarta.persistence.*;
import java.util.Calendar;
import java.util.Objects;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Dispensation")
@Setter
@Getter
public class Dispensation extends AbstractEntity implements Comparable<Dispensation> {

  private static final String DD_MM_YY_FORMAT = "%1$td.%1$tm.%1$tY";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "dispensation_id")
  private Integer dispensationId;

  @Temporal(TemporalType.DATE)
  @Column(name = "dispensationsbeginn", nullable = false)
  private Calendar dispensationsbeginn;

  @Temporal(TemporalType.DATE)
  @Column(name = "dispensationsende")
  private Calendar dispensationsende;

  @Lob
  @Column(name = "voraussichtliche_dauer", columnDefinition = "text")
  private String voraussichtlicheDauer;

  @Lob
  @Column(name = "grund", columnDefinition = "text", nullable = false)
  private String grund;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "schueler_id", nullable = false)
  private Schueler schueler;

  @Transient private final boolean neusteZuoberst;

  public Dispensation() {
    Properties svmProperties = SvmProperties.getSvmProperties();
    neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
  }

  public Dispensation(
      Calendar dispensationsbeginn,
      Calendar dispensationsende,
      String voraussichtlicheDauer,
      String grund) {
    this();
    this.dispensationsbeginn = dispensationsbeginn;
    this.dispensationsende = dispensationsende;
    this.voraussichtlicheDauer = voraussichtlicheDauer;
    this.grund = grund;
  }

  public boolean isIdenticalWith(Dispensation otherDispensation) {
    return otherDispensation != null
        && ((dispensationsbeginn == null && otherDispensation.getDispensationsbeginn() == null)
            || (dispensationsbeginn != null
                && dispensationsbeginn.equals(otherDispensation.getDispensationsbeginn())))
        && ((dispensationsende == null && otherDispensation.getDispensationsende() == null)
            || (dispensationsende != null
                && dispensationsende.equals(otherDispensation.getDispensationsende())))
        && ((voraussichtlicheDauer == null && otherDispensation.getVoraussichtlicheDauer() == null)
            || (voraussichtlicheDauer != null
                && voraussichtlicheDauer.equals(otherDispensation.getVoraussichtlicheDauer())))
        && ((grund == null && otherDispensation.getGrund() == null)
            || (grund != null && grund.equals(otherDispensation.getGrund())));
  }

  public void copyAttributesFrom(Dispensation otherDispensation) {
    this.dispensationsbeginn = otherDispensation.getDispensationsbeginn();
    this.dispensationsende = otherDispensation.getDispensationsende();
    this.voraussichtlicheDauer = otherDispensation.getVoraussichtlicheDauer();
    this.grund = otherDispensation.getGrund();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Dispensation that = (Dispensation) o;
    return Objects.equals(dispensationsbeginn, that.dispensationsbeginn)
        && Objects.equals(dispensationsende, that.dispensationsende)
        && Objects.equals(voraussichtlicheDauer, that.voraussichtlicheDauer)
        && Objects.equals(grund, that.grund)
        && Objects.equals(schueler, that.schueler);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        dispensationsbeginn, dispensationsende, voraussichtlicheDauer, grund, schueler);
  }

  @SuppressWarnings("java:S3776")
  @Override
  public int compareTo(Dispensation otherDispensation) {
    // absteigend nach Dispensationsbeginn und Dispensationsende sortieren, d.h. neuste Einträge
    // zuoberst
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

  @Override
  public String toString() {
    StringBuilder dispensationSb = new StringBuilder();
    if (dispensationsende == null) {
      dispensationSb.append("Seit ").append(String.format(DD_MM_YY_FORMAT, dispensationsbeginn));
    } else {
      dispensationSb.append(String.format(DD_MM_YY_FORMAT, dispensationsbeginn));
      dispensationSb.append(" - ").append(String.format(DD_MM_YY_FORMAT, dispensationsende));
    }
    if (voraussichtlicheDauer != null) {
      dispensationSb.append(", voraussichtliche Dauer: ").append(voraussichtlicheDauer);
    }
    dispensationSb.append(", Grund: ").append(grund);
    return dispensationSb.toString();
  }
}
