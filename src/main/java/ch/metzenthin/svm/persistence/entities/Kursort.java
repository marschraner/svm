package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.text.Collator;
import java.util.*;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Kursort")
public class Kursort implements Comparable<Kursort> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "kursort_id")
  private Integer kursortId;

  @SuppressWarnings("unused")
  @Version
  @Column(name = "last_updated")
  private Timestamp version;

  @Column(name = "bezeichnung", nullable = false)
  private String bezeichnung;

  @Column(name = "selektierbar", nullable = false)
  private Boolean selektierbar;

  @OneToMany(mappedBy = "kursort")
  private final List<Kurs> kurse = new ArrayList<>();

  public Kursort() {}

  public Kursort(String bezeichnung, Boolean selektierbar) {
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
    this.selektierbar = otherKursort.getSelektierbar();
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

  public Integer getKursortId() {
    return kursortId;
  }

  @SuppressWarnings("unused")
  public void setKursortId(Integer kursortId) {
    this.kursortId = kursortId;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String kursort) {
    this.bezeichnung = kursort;
  }

  public Boolean getSelektierbar() {
    return selektierbar;
  }

  public void setSelektierbar(Boolean selektierbar) {
    this.selektierbar = selektierbar;
  }

  public List<Kurs> getKurse() {
    return kurse;
  }
}
