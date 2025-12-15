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
@Table(name = "Kurstyp")
@Setter
@Getter
public class Kurstyp extends AbstractEntity implements Comparable<Kurstyp> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "kurstyp_id")
  private Integer kurstypId;

  @Column(name = "bezeichnung", nullable = false)
  private String bezeichnung;

  @Column(name = "selektierbar", nullable = false)
  private boolean selektierbar;

  @OneToMany(mappedBy = "kurstyp")
  private final List<Kurs> kurse = new ArrayList<>();

  public Kurstyp() {}

  public Kurstyp(String bezeichnung, boolean selektierbar) {
    this.bezeichnung = bezeichnung;
    this.selektierbar = selektierbar;
  }

  public boolean isIdenticalWith(Kurstyp otherKurstyp) {
    return otherKurstyp != null
        && ((bezeichnung == null && otherKurstyp.getBezeichnung() == null)
            || (bezeichnung != null && bezeichnung.equals(otherKurstyp.getBezeichnung())));
  }

  public void copyAttributesFrom(Kurstyp otherKurstyp) {
    this.bezeichnung = otherKurstyp.getBezeichnung();
    this.selektierbar = otherKurstyp.isSelektierbar();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Kurstyp kurstyp = (Kurstyp) o;
    return Objects.equals(bezeichnung, kurstyp.bezeichnung);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(bezeichnung);
  }

  @Override
  public int compareTo(Kurstyp otherKurstyp) {
    // Sortierung nach Umlauten http://50226.de/sortieren-mit-umlauten-in-java.html
    Collator collator = Collator.getInstance(Locale.GERMAN);
    collator.setStrength(Collator.SECONDARY); // a == A, a < Ä
    return collator.compare(bezeichnung, otherKurstyp.bezeichnung);
  }

  @Override
  public String toString() {
    if (bezeichnung.equals("alle")) {
      return "alle";
    }
    return bezeichnung;
  }
}
