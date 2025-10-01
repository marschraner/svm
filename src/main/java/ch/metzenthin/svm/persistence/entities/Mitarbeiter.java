package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.utils.SvmStringUtils;
import jakarta.persistence.*;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("java:S2160") // equals / hash definiert für Person
@Entity
@Table(name = "Mitarbeiter")
@DiscriminatorValue("Mitarbeiter")
@Getter
@Setter
public class Mitarbeiter extends Person {

  @Column(name = "ahvnummer")
  private String ahvNummer;

  @Column(name = "ibannummer")
  private String ibanNummer;

  @Column(name = "lehrkraft", nullable = false)
  private boolean lehrkraft;

  @Column(name = "vertretungsmoeglichkeiten", columnDefinition = "text")
  private String vertretungsmoeglichkeiten;

  @Column(name = "bemerkungen", columnDefinition = "text")
  private String bemerkungen;

  @Column(name = "aktiv", nullable = false)
  private boolean aktiv;

  @Transient private boolean selektiert = true;

  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(
      name = "Mitarbeiter_MitarbeiterCode",
      joinColumns = {@JoinColumn(name = "person_id")},
      inverseJoinColumns = {@JoinColumn(name = "code_id")})
  private final List<MitarbeiterCode> mitarbeiterCodes = new ArrayList<>();

  @ManyToMany(mappedBy = "lehrkraefte")
  private final List<Kurs> kurse = new ArrayList<>();

  public Mitarbeiter() {}

  @SuppressWarnings("java:S107")
  public Mitarbeiter(
      Anrede anrede,
      String vorname,
      String nachname,
      Calendar geburtsdatum,
      String festnetz,
      String natel,
      String email,
      String ahvNummer,
      String ibanNummer,
      boolean lehrkraft,
      String vertretungsmoeglichkeiten,
      String bemerkungen,
      boolean aktiv) {
    super(anrede, vorname, nachname, geburtsdatum, festnetz, natel, email);
    this.ahvNummer = ahvNummer;
    this.ibanNummer = ibanNummer;
    this.lehrkraft = lehrkraft;
    this.vertretungsmoeglichkeiten = vertretungsmoeglichkeiten;
    this.bemerkungen = bemerkungen;
    this.aktiv = aktiv;
  }

  public boolean isIdenticalWith(Mitarbeiter otherMitarbeiter) {
    return otherMitarbeiter != null
        && getVorname().equals(otherMitarbeiter.getVorname())
        && getNachname().equals(otherMitarbeiter.getNachname())
        && ((getGeburtsdatum() == null && otherMitarbeiter.getGeburtsdatum() == null)
            || (getGeburtsdatum() != null
                && getGeburtsdatum().equals(otherMitarbeiter.getGeburtsdatum())));
  }

  public void copyAttributesFrom(Mitarbeiter mitarbeiterFrom) {
    super.copyAttributesFrom(mitarbeiterFrom);
    ahvNummer = mitarbeiterFrom.getAhvNummer();
    ibanNummer = mitarbeiterFrom.getIbanNummer();
    lehrkraft = mitarbeiterFrom.isLehrkraft();
    vertretungsmoeglichkeiten = mitarbeiterFrom.getVertretungsmoeglichkeiten();
    bemerkungen = mitarbeiterFrom.getBemerkungen();
    aktiv = mitarbeiterFrom.isAktiv();
  }

  @Override
  public String toString() {
    if (getVorname() != null && getNachname() != null) {
      return getVorname() + " " + getNachname();
    } else {
      return "";
    }
  }

  public String toStringShort() {
    if (getVorname() != null && getNachname() != null) {
      return getVorname().charAt(0) + ". " + getNachname();
    } else {
      return "";
    }
  }

  @Transient
  public String getVertretungsmoeglichkeitenLineBreaksReplacedBySemicolonOrPeriod() {
    return SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(vertretungsmoeglichkeiten);
  }

  @Transient
  public String getVertretungsmoeglichkeitenLineBreaksReplacedByCommaOrPeriod() {
    return SvmStringUtils.replaceLineBreaksByCommaOrPeriod(vertretungsmoeglichkeiten);
  }

  @Transient
  public String getBemerkungenLineBreaksReplacedBySemicolonOrPeriod() {
    return SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(bemerkungen);
  }

  @Transient
  public String getBemerkungenLineBreaksReplacedByCommaOrPeriod() {
    return SvmStringUtils.replaceLineBreaksByCommaOrPeriod(bemerkungen);
  }

  public List<MitarbeiterCode> getSortedMitarbeiterCodes() {
    List<MitarbeiterCode> sortedMitarbeitercodes = new ArrayList<>(mitarbeiterCodes);
    Collections.sort(sortedMitarbeitercodes);
    return sortedMitarbeitercodes;
  }

  public void addCode(MitarbeiterCode mitarbeiterCode) {
    if (!mitarbeiterCode.getMitarbeiters().contains(this)) {
      mitarbeiterCode.getMitarbeiters().add(this);
    }
    if (!mitarbeiterCodes.contains(mitarbeiterCode)) {
      mitarbeiterCodes.add(mitarbeiterCode);
    }
  }

  public void deleteCode(MitarbeiterCode mitarbeiterCode) {
    // mitarbeiterCode.getMitarbeiter().remove(this); führt zu StaleObjectStateException!
    // Stattdessen in MitarbeiterCodeDao.removeFromMitarbeiterAndUpdate() refresh(mitarbeiterCode)
    // ausführen!
    mitarbeiterCodes.remove(mitarbeiterCode);
  }

  @Transient
  public String getMitarbeiterCodesAsStr() {
    if (mitarbeiterCodes.isEmpty()) {
      return "";
    }
    StringBuilder mitarbeiterCodesAsStr =
        new StringBuilder(getSortedMitarbeiterCodes().get(0).getKuerzel());
    for (int i = 1; i < getSortedMitarbeiterCodes().size(); i++) {
      mitarbeiterCodesAsStr.append(", ").append(getSortedMitarbeiterCodes().get(i).getKuerzel());
    }
    return mitarbeiterCodesAsStr.toString();
  }
}
