package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.*;
import java.text.Collator;
import java.util.Locale;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Code")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator")
@Getter
@Setter
public abstract class Code extends AbstractEntity implements Comparable<Code> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "code_id")
  private Integer codeId;

  @Column(name = "kuerzel", nullable = false)
  private String kuerzel;

  @Column(name = "beschreibung", nullable = false)
  private String beschreibung;

  @Column(name = "selektierbar", nullable = false)
  private boolean selektierbar;

  protected Code() {}

  protected Code(String kuerzel, String beschreibung, boolean selektierbar) {
    this.kuerzel = kuerzel;
    this.beschreibung = beschreibung;
    this.selektierbar = selektierbar;
  }

  public boolean isIdenticalWith(Code otherCode) {
    return otherCode != null
        && ((kuerzel == null && otherCode.getKuerzel() == null)
            || (kuerzel != null && kuerzel.equals(otherCode.getKuerzel())))
        && ((beschreibung == null && otherCode.getBeschreibung() == null)
            || (beschreibung != null && beschreibung.equals(otherCode.getBeschreibung())));
  }

  public void copyAttributesFrom(Code otherCode) {
    this.kuerzel = otherCode.getKuerzel();
    this.beschreibung = otherCode.getBeschreibung();
    this.selektierbar = otherCode.isSelektierbar();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Code code = (Code) o;
    return Objects.equals(kuerzel, code.kuerzel) && Objects.equals(beschreibung, code.beschreibung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kuerzel, beschreibung);
  }

  @Override
  public int compareTo(Code otherDispensation) {
    // Alphabetische Sortierung mit Berücksichtigung von Umlauten
    // http://50226.de/sortieren-mit-umlauten-in-java.html
    Collator collator = Collator.getInstance(Locale.GERMAN);
    collator.setStrength(Collator.SECONDARY); // a == A, a < Ä
    return collator.compare(kuerzel, otherDispensation.kuerzel);
  }

  @Override
  public String toString() {
    if (kuerzel == null || kuerzel.isEmpty()) {
      return "";
    }
    return kuerzel + " (" + beschreibung + ")";
  }
}
