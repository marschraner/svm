package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.*;
import java.text.Collator;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Kursort")
@Setter
@Getter
public class Kursort extends AbstractEntity implements Comparable<Kursort> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "kursort_id")
  private Integer kursortId;

  @Column(name = "bezeichnung", nullable = false)
  private String bezeichnung;

  @Column(name = "selektierbar", nullable = false)
  private boolean selektierbar;

  @OneToMany(mappedBy = "kursort")
  private final List<Kurs> kurse = new ArrayList<>();

  public Kursort() {}

  public Kursort(String bezeichnung, boolean selektierbar) {
    this.bezeichnung = bezeichnung;
    this.selektierbar = selektierbar;
  }

  public boolean isIdenticalWith(Kursort otherCode) {
    return otherCode != null
        && ((bezeichnung == null && otherCode.getBezeichnung() == null)
            || (bezeichnung != null && bezeichnung.equals(otherCode.getBezeichnung())));
  }

  public void copyAttributesFrom(Kursort otherKursort) {
    this.bezeichnung = otherKursort.getBezeichnung();
    this.selektierbar = otherKursort.isSelektierbar();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Kursort kursort = (Kursort) o;
    return Objects.equals(bezeichnung, kursort.bezeichnung);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(bezeichnung);
  }

  @Override
  public int compareTo(Kursort otherKursort) {
    // Alphabetische Sortierung mit Berücksichtigung von Umlauten
    // http://50226.de/sortieren-mit-umlauten-in-java.html
    Collator collator = Collator.getInstance(Locale.GERMAN);
    collator.setStrength(Collator.SECONDARY); // a == A, a < Ä
    return collator.compare(bezeichnung, otherKursort.bezeichnung);
  }

  @Override
  public String toString() {
    if (bezeichnung.equals("alle")) {
      return "alle";
    }
    return bezeichnung;
  }
}
