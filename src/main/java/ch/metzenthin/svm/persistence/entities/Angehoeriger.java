package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.datatypes.Anrede;
import jakarta.persistence.*;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("java:S2160") // equals / hash definiert für Person
@Entity
@Table(name = "Angehoeriger")
@DiscriminatorValue("Angehoeriger")
@Getter
@Setter
public class Angehoeriger extends Person {

  @Column(name = "wuenscht_emails")
  private Boolean wuenschtEmails;

  @OneToMany(mappedBy = "vater")
  private final List<Schueler> kinderVater = new ArrayList<>();

  @OneToMany(mappedBy = "mutter")
  private final List<Schueler> kinderMutter = new ArrayList<>();

  @OneToMany(mappedBy = "rechnungsempfaenger")
  private final List<Schueler> schuelerRechnungsempfaenger = new ArrayList<>();

  @OneToMany(mappedBy = "rechnungsempfaenger")
  private final List<Semesterrechnung> semesterrechnungen = new ArrayList<>();

  public Angehoeriger() {}

  public Angehoeriger(
      Anrede anrede,
      String vorname,
      String nachname,
      String festnetz,
      String natel,
      String email,
      Boolean wuenschtEmails) {
    super(anrede, vorname, nachname, null, festnetz, natel, email);
    this.wuenschtEmails = wuenschtEmails;
  }

  public boolean isIdenticalWith(Angehoeriger otherAngehoeriger) {
    return (super.isIdenticalWith(otherAngehoeriger));
  }

  public boolean isPartOf(Angehoeriger otherAngehoeriger) {
    return (super.isPartOf(otherAngehoeriger));
  }

  public void copyFieldValuesFrom(Angehoeriger angehoerigerFrom) {
    super.copyAttributesFrom(angehoerigerFrom);
    wuenschtEmails = angehoerigerFrom.getWuenschtEmails();
  }

  public String toStringIncludingWuenschtEmailsIfWuenschtEmailsTrue() {
    String angehoerigerAsString = toString();
    if (wuenschtEmails != null && wuenschtEmails) {
      angehoerigerAsString = angehoerigerAsString + "  (wünscht E-Mails)";
    }
    return angehoerigerAsString;
  }

  public String toStringIncludingWuenschtKeineEmailsIfWuenschtEmailsFalse() {
    String angehoerigerAsString = toString();
    if (wuenschtEmails != null && !wuenschtEmails) {
      angehoerigerAsString = angehoerigerAsString + "  (wünscht keine E-Mails)";
    }
    return angehoerigerAsString;
  }

  @Transient
  public List<Schueler> getAngemeldeteSchuelerRechnungsempfaenger() {
    List<Schueler> angemeldeteSchuelerRechnungsempfaenger = new ArrayList<>();
    for (Schueler schueler : schuelerRechnungsempfaenger) {
      if (schueler.isAngemeldet() && !angemeldeteSchuelerRechnungsempfaenger.contains(schueler)) {
        angemeldeteSchuelerRechnungsempfaenger.add(schueler);
      }
    }
    Collections.sort(angemeldeteSchuelerRechnungsempfaenger);
    return angemeldeteSchuelerRechnungsempfaenger;
  }

  @Transient
  public List<Semesterrechnung> getSortedSemesterrechnungen() {
    List<Semesterrechnung> sortedSemesterrechnungen = new ArrayList<>(semesterrechnungen);
    Collections.sort(sortedSemesterrechnungen);
    return sortedSemesterrechnungen;
  }
}
