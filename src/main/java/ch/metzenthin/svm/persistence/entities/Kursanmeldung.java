package ch.metzenthin.svm.persistence.entities;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import jakarta.persistence.*;
import java.util.Calendar;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Kursanmeldung")
@IdClass(KursanmeldungId.class)
@Getter
@Setter
public class Kursanmeldung extends AbstractEntity implements Comparable<Kursanmeldung> {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id")
  private Schueler schueler;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "kurs_id")
  private Kurs kurs;

  @Temporal(TemporalType.DATE)
  @Column(name = "anmeldedatum", nullable = false)
  private Calendar anmeldedatum;

  @Temporal(TemporalType.DATE)
  @Column(name = "abmeldedatum")
  private Calendar abmeldedatum;

  @Column(name = "bemerkungen")
  private String bemerkungen;

  public Kursanmeldung() {}

  public Kursanmeldung(
      Schueler schueler,
      Kurs kurs,
      Calendar anmeldedatum,
      Calendar abmeldedatum,
      String bemerkungen) {
    this.schueler = schueler;
    this.kurs = kurs;
    this.anmeldedatum = anmeldedatum;
    this.abmeldedatum = abmeldedatum;
    this.bemerkungen = bemerkungen;
  }

  public boolean isIdenticalWith(Kursanmeldung otherKursanmeldung) {
    return otherKursanmeldung != null
        && schueler.isIdenticalWith(otherKursanmeldung.getSchueler())
        && kurs.isIdenticalWith(otherKursanmeldung.getKurs());
  }

  public void copyAttributesFrom(Kursanmeldung otherKursanmeldung) {
    this.anmeldedatum = otherKursanmeldung.getAnmeldedatum();
    this.abmeldedatum = otherKursanmeldung.getAbmeldedatum();
    this.bemerkungen = otherKursanmeldung.getBemerkungen();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Kursanmeldung that = (Kursanmeldung) o;
    return Objects.equals(schueler, that.schueler)
        && Objects.equals(kurs, that.kurs)
        && Objects.equals(anmeldedatum, that.anmeldedatum)
        && Objects.equals(abmeldedatum, that.abmeldedatum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(schueler, kurs, anmeldedatum, abmeldedatum);
  }

  @Override
  public int compareTo(Kursanmeldung otherKursanmeldung) {
    // aufsteigend nach Schüler und Kursen sortieren
    int result = this.schueler.compareTo(otherKursanmeldung.schueler);
    if (result == 0) {
      result = this.kurs.compareTo(otherKursanmeldung.kurs);
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder kursanmeldungSb = new StringBuilder(kurs.toString());
    if (anmeldedatum != null || abmeldedatum != null || checkNotEmpty(bemerkungen)) {
      kursanmeldungSb.append("&nbsp &nbsp (");
      if (anmeldedatum != null) {
        kursanmeldungSb.append("Anmeldung: ").append(asString(anmeldedatum));
        if (abmeldedatum != null) {
          kursanmeldungSb.append(", ");
        } else if (checkNotEmpty(bemerkungen)) {
          kursanmeldungSb.append("; ");
        }
      }
      if (abmeldedatum != null) {
        kursanmeldungSb.append("Abmeldung: ").append(asString(abmeldedatum));
        if (checkNotEmpty(bemerkungen)) {
          kursanmeldungSb.append("; ");
        }
      }
      if (checkNotEmpty(bemerkungen)) {
        kursanmeldungSb.append(bemerkungen);
      }
      kursanmeldungSb.append(")");
    }
    return kursanmeldungSb.toString();
  }
}
