package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.utils.SvmStringUtils;
import jakarta.persistence.*;
import java.util.*;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("java:S2160") // equals / hash definiert für Person
@Entity
@Table(name = "Mitarbeiter")
@DiscriminatorValue("Mitarbeiter")
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
  private Boolean aktiv;

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
      Boolean aktiv) {
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
    lehrkraft = mitarbeiterFrom.getLehrkraft();
    vertretungsmoeglichkeiten = mitarbeiterFrom.getVertretungsmoeglichkeiten();
    bemerkungen = mitarbeiterFrom.getBemerkungen();
    aktiv = mitarbeiterFrom.getAktiv();
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

  public String getAhvNummer() {
    return ahvNummer;
  }

  public void setAhvNummer(String ahvNummer) {
    this.ahvNummer = ahvNummer;
  }

  public String getIbanNummer() {
    return ibanNummer;
  }

  public void setIbanNummer(String ibanNummer) {
    this.ibanNummer = ibanNummer;
  }

  public boolean getLehrkraft() {
    return lehrkraft;
  }

  public void setLehrkraft(boolean lehrkraft) {
    this.lehrkraft = lehrkraft;
  }

  public String getVertretungsmoeglichkeiten() {
    return vertretungsmoeglichkeiten;
  }

  @Transient
  public String getVertretungsmoeglichkeitenLineBreaksReplacedBySemicolonOrPeriod() {
    return SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(vertretungsmoeglichkeiten);
  }

  @Transient
  public String getVertretungsmoeglichkeitenLineBreaksReplacedByCommaOrPeriod() {
    return SvmStringUtils.replaceLineBreaksByCommaOrPeriod(vertretungsmoeglichkeiten);
  }

  public void setVertretungsmoeglichkeiten(String vertretungsmoeglichkeiten) {
    this.vertretungsmoeglichkeiten = vertretungsmoeglichkeiten;
  }

  public String getBemerkungen() {
    return bemerkungen;
  }

  @Transient
  public String getBemerkungenLineBreaksReplacedBySemicolonOrPeriod() {
    return SvmStringUtils.replaceLineBreaksBySemicolonOrPeriod(bemerkungen);
  }

  @Transient
  public String getBemerkungenLineBreaksReplacedByCommaOrPeriod() {
    return SvmStringUtils.replaceLineBreaksByCommaOrPeriod(bemerkungen);
  }

  public void setBemerkungen(String bemerkungen) {
    this.bemerkungen = bemerkungen;
  }

  public Boolean getAktiv() {
    return aktiv;
  }

  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }

  public boolean isSelektiert() {
    return selektiert;
  }

  public void setSelektiert(boolean selektiert) {
    this.selektiert = selektiert;
  }

  public List<MitarbeiterCode> getMitarbeiterCodes() {
    return mitarbeiterCodes;
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

  public List<Kurs> getKurse() {
    return kurse;
  }
}
