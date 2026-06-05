package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.domain.model.conversion.IntegerConverter;
import jakarta.persistence.*;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Maerchen")
@Setter
@Getter
public class Maerchen extends AbstractEntity implements Comparable<Maerchen> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "maerchen_id")
  private Integer maerchenId;

  @Column(name = "schuljahr", nullable = false)
  private String schuljahr;

  @Column(name = "bezeichnung", nullable = false)
  private String bezeichnung;

  @Column(name = "anzahl_vorstellungen", nullable = false)
  private int anzahlVorstellungen;

  @OneToMany(mappedBy = "maerchen")
  private final List<Maercheneinteilung> maercheneinteilungen = new ArrayList<>();

  public Maerchen() {
    this.anzahlVorstellungen = IntegerConverter.VALUE_NOT_SET;
  }

  public Maerchen(String schuljahr, String bezeichnung, int anzahlVorstellungen) {
    this.schuljahr = schuljahr;
    this.bezeichnung = bezeichnung;
    this.anzahlVorstellungen = anzahlVorstellungen;
  }

  public boolean isIdenticalWith(Maerchen otherMaerchen) {
    return otherMaerchen != null
        && ((schuljahr == null && otherMaerchen.getSchuljahr() == null)
            || (schuljahr != null && schuljahr.equals(otherMaerchen.getSchuljahr())))
        && ((bezeichnung == null && otherMaerchen.getBezeichnung() == null)
            || (bezeichnung != null && bezeichnung.equals(otherMaerchen.getBezeichnung())))
        && (anzahlVorstellungen == otherMaerchen.anzahlVorstellungen);
  }

  public void copyAttributesFrom(Maerchen otherMaerchen) {
    this.schuljahr = otherMaerchen.getSchuljahr();
    this.bezeichnung = otherMaerchen.getBezeichnung();
    this.anzahlVorstellungen = otherMaerchen.getAnzahlVorstellungen();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Maerchen maerchen = (Maerchen) o;
    return Objects.equals(schuljahr, maerchen.schuljahr)
        && Objects.equals(bezeichnung, maerchen.bezeichnung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(schuljahr, bezeichnung);
  }

  @Override
  public int compareTo(Maerchen otherMaerchen) {
    // absteigend nach Schuljahr sortieren, d.h. neuste Einträge zuoberst
    return otherMaerchen.schuljahr.compareTo(schuljahr);
  }

  @Override
  public String toString() {
    return bezeichnung + " (" + schuljahr + ")";
  }
}
