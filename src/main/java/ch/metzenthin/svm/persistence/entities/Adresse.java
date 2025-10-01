package ch.metzenthin.svm.persistence.entities;

import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Adresse")
@Getter
@Setter
public class Adresse extends AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "adresse_id")
  private Integer adresseId;

  @Column(name = "strasse")
  private String strasse;

  @Column(name = "hausnummer")
  private String hausnummer;

  @Column(name = "plz", nullable = false)
  private String plz;

  @Column(name = "ort", nullable = false)
  private String ort;

  public Adresse() {}

  public Adresse(String strasse, String hausnummer, String plz, String ort) {
    this.strasse = strasse;
    this.hausnummer = hausnummer;
    this.plz = plz;
    this.ort = ort;
  }

  public boolean isIdenticalWith(Adresse otherAdresse) {
    return otherAdresse != null
        && ((strasse == null && otherAdresse.getStrasse() == null)
            || (strasse != null && strasse.equals(otherAdresse.getStrasse())))
        && ((hausnummer == null && otherAdresse.getHausnummer() == null)
            || (hausnummer != null && hausnummer.equals(otherAdresse.getHausnummer())))
        && ((plz == null && otherAdresse.getPlz() == null)
            || (plz != null && plz.equals(otherAdresse.getPlz())))
        && ((ort == null && otherAdresse.getOrt() == null)
            || (ort != null && ort.equals(otherAdresse.getOrt())));
  }

  public boolean isPartOf(Adresse otherAdresse) {
    return otherAdresse != null
        && (strasse == null
            || strasse.trim().isEmpty()
            || strasse.equals(otherAdresse.getStrasse()))
        && (hausnummer == null
            || hausnummer.trim().isEmpty()
            || hausnummer.equals(otherAdresse.getHausnummer()))
        && (plz == null || plz.trim().isEmpty() || plz.equals(otherAdresse.getPlz()))
        && (ort == null || ort.trim().isEmpty() || ort.equals(otherAdresse.getOrt()));
  }

  public boolean isEmpty() {
    return (strasse == null || strasse.trim().isEmpty())
        && (hausnummer == null || hausnummer.trim().isEmpty())
        && (plz == null || plz.trim().isEmpty())
        && (ort == null || ort.trim().isEmpty());
  }

  public void copyAttributesFrom(Adresse adresseFrom) {
    strasse = adresseFrom.getStrasse();
    hausnummer = adresseFrom.getHausnummer();
    plz = adresseFrom.getPlz();
    ort = adresseFrom.getOrt();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Adresse adresse = (Adresse) o;
    return Objects.equals(strasse, adresse.strasse)
        && Objects.equals(hausnummer, adresse.hausnummer)
        && Objects.equals(plz, adresse.plz)
        && Objects.equals(ort, adresse.ort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(strasse, hausnummer, plz, ort);
  }

  @Override
  public String toString() {
    StringBuilder adresseSb = new StringBuilder();
    if (strasse != null && !strasse.trim().isEmpty()) {
      adresseSb.append(", ").append(strasse);
    }
    if (hausnummer != null && !hausnummer.trim().isEmpty()) {
      adresseSb.append(" ").append(hausnummer);
    }
    if (plz != null && !plz.trim().isEmpty()) {
      adresseSb.append(", ").append(plz);
    }
    if (ort != null && !ort.trim().isEmpty()) {
      adresseSb.append(" ").append(ort);
    }
    return adresseSb.toString();
  }

  public String getStrasseHausnummer() {
    String strasseHausnummer = getStrasse();
    if (checkNotEmpty(strasse) && checkNotEmpty(getHausnummer())) {
      strasseHausnummer = strasseHausnummer + " " + getHausnummer();
    }
    return nullAsEmptyString(strasseHausnummer);
  }

  public String getStrHausnummer() {
    String strHausnummer = getStrasseHausnummer();
    if (checkNotEmpty(strHausnummer)) {
      strHausnummer = strHausnummer.replace("strasse", "str.");
      strHausnummer = strHausnummer.replace("Strasse", "Str.");
    }
    return nullAsEmptyString(strHausnummer);
  }

  @Transient
  public String getPlzOrt() {
    return plz + " " + ort;
  }
}
