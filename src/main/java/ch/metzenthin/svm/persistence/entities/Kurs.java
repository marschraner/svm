package ch.metzenthin.svm.persistence.entities;

import static ch.metzenthin.svm.common.utils.Converter.asString;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.common.utils.StringNumberComparator;
import ch.metzenthin.svm.common.utils.SvmProperties;
import jakarta.persistence.*;
import java.sql.Time;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Kurs")
@Setter
@Getter
public class Kurs extends AbstractEntity implements Comparable<Kurs> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "kurs_id")
  private Integer kursId;

  @ManyToOne
  @JoinColumn(name = "semester_id", nullable = false)
  private Semester semester;

  @ManyToOne
  @JoinColumn(name = "kurstyp_id", nullable = false)
  private Kurstyp kurstyp;

  @Column(name = "altersbereich", nullable = false)
  private String altersbereich;

  @Column(name = "stufe", nullable = false)
  private String stufe;

  @Enumerated(EnumType.STRING)
  @Column(name = "wochentag", nullable = false)
  private Wochentag wochentag;

  @Column(name = "zeit_beginn", nullable = false)
  private Time zeitBeginn;

  @Column(name = "zeit_ende", nullable = false)
  private Time zeitEnde;

  @ManyToOne
  @JoinColumn(name = "kursort_id", nullable = false)
  private Kursort kursort;

  @Column(name = "bemerkungen")
  private String bemerkungen;

  @OneToMany(mappedBy = "kurs")
  private final List<Kursanmeldung> kursanmeldungen = new ArrayList<>();

  @Transient private final boolean neusteZuoberst;

  public Kurs() {
    Properties svmProperties = SvmProperties.getSvmProperties();
    neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
  }

  public Kurs(
      String altersbereich,
      String stufe,
      Wochentag wochentag,
      Time zeitBeginn,
      Time zeitEnde,
      String bemerkungen) {
    this();
    this.altersbereich = altersbereich;
    this.stufe = stufe;
    this.wochentag = wochentag;
    this.zeitBeginn = zeitBeginn;
    this.zeitEnde = zeitEnde;
    this.bemerkungen = bemerkungen;
  }

  @Override
  public String toString() {
    return kurstyp
        + " "
        + stufe
        + ", "
        + wochentag
        + " "
        + asString(zeitBeginn)
        + "-"
        + asString(zeitEnde);
  }

  public String toStringShort(List<Mitarbeiter> lehrkraefte) {
    StringBuilder kursAsStr =
        new StringBuilder(
            wochentag
                + " "
                + asString(zeitBeginn)
                + "-"
                + asString(zeitEnde)
                + " ("
                + lehrkraefte.get(0).toStringShort());
    for (int i = 1; i < lehrkraefte.size(); i++) {
      kursAsStr.append("/").append(lehrkraefte.get(i).toStringShort());
    }
    kursAsStr.append(")");
    return kursAsStr.toString();
  }

  public boolean isIdenticalWith(
      Kurs otherKurs, List<Mitarbeiter> lehrkraefteKurs, List<Mitarbeiter> lehrkraefteOtherKurs) {
    // Kurse identisch, falls Semester, Wochentag, Zeit und Mitarbeiter identisch
    List<Mitarbeiter> commonLehrkraefte = new ArrayList<>(lehrkraefteKurs);
    // RetainAll: nur diejenigen Lehrkraefte in commonLehrkraefte behalten, die auch in otherKurs
    // enthalten sind
    if (otherKurs != null) {
      commonLehrkraefte.retainAll(lehrkraefteOtherKurs);
    }
    return otherKurs != null
        && semester.equals(otherKurs.semester)
        && wochentag.equals(otherKurs.wochentag)
        && zeitBeginn.equals(otherKurs.zeitBeginn)
        && !commonLehrkraefte.isEmpty();
  }

  public void copyAttributesFrom(Kurs otherKurs) {
    this.altersbereich = otherKurs.getAltersbereich();
    this.stufe = otherKurs.getStufe();
    this.wochentag = otherKurs.getWochentag();
    this.zeitBeginn = otherKurs.getZeitBeginn();
    this.zeitEnde = otherKurs.getZeitEnde();
    this.bemerkungen = otherKurs.getBemerkungen();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Kurs kurs = (Kurs) o;
    return Objects.equals(semester, kurs.semester)
        && Objects.equals(kurstyp, kurs.kurstyp)
        && Objects.equals(altersbereich, kurs.altersbereich)
        && Objects.equals(stufe, kurs.stufe)
        && wochentag == kurs.wochentag
        && Objects.equals(zeitBeginn, kurs.zeitBeginn)
        && Objects.equals(zeitEnde, kurs.zeitEnde)
        && Objects.equals(kursort, kurs.kursort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        semester, kurstyp, altersbereich, stufe, wochentag, zeitBeginn, zeitEnde, kursort);
  }

  /** Note: this class has a natural ordering that is inconsistent with equals. */
  @SuppressWarnings("java:S3776")
  @Override
  public int compareTo(Kurs otherKurs) {
    Comparator<String> stringNumberComparator = new StringNumberComparator();
    int result =
        (neusteZuoberst
            ? otherKurs.semester.getSemesterbeginn().compareTo(semester.getSemesterbeginn())
            : semester.getSemesterbeginn().compareTo(otherKurs.semester.getSemesterbeginn()));
    if (result == 0) {
      result = kurstyp.compareTo(otherKurs.kurstyp);
      if (result == 0) {
        result = stringNumberComparator.compare(stufe, otherKurs.stufe);
        if (result == 0) {
          result = wochentag.compareTo(otherKurs.wochentag);
          if (result == 0) {
            result = zeitBeginn.compareTo(otherKurs.zeitBeginn);
            if (result == 0) {
              result = zeitEnde.compareTo(otherKurs.zeitEnde);
              if (result == 0) {
                result = kursort.compareTo(otherKurs.kursort);
              }
            }
          }
        }
      }
    }
    return result;
  }

  @Transient
  public int getKurslaenge() {
    return (int) ((zeitEnde.getTime() - zeitBeginn.getTime()) / 60000);
  }
}
