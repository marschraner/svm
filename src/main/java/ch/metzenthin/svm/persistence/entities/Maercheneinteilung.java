package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.datatypes.Elternmithilfe;
import ch.metzenthin.svm.common.datatypes.Gruppe;
import ch.metzenthin.svm.common.utils.SvmProperties;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Maercheneinteilung")
@IdClass(MaercheneinteilungId.class)
public class Maercheneinteilung implements Comparable<Maercheneinteilung> {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id")
  private Schueler schueler;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "maerchen_id")
  private Maerchen maerchen;

  @SuppressWarnings("unused")
  @Version
  @Column(name = "last_updated")
  private Timestamp version;

  @Enumerated(EnumType.STRING)
  @Column(name = "gruppe", nullable = false)
  private Gruppe gruppe;

  @Column(name = "rolle_1", nullable = false)
  private String rolle1;

  @Column(name = "bilder_rolle_1")
  private String bilderRolle1;

  @Column(name = "rolle_2")
  private String rolle2;

  @Column(name = "bilder_rolle_2")
  private String bilderRolle2;

  @Column(name = "rolle_3")
  private String rolle3;

  @Column(name = "bilder_rolle_3")
  private String bilderRolle3;

  @Enumerated(EnumType.STRING)
  @JoinColumn(name = "elternmithilfe")
  private Elternmithilfe elternmithilfe;

  @ManyToOne
  @JoinColumn(name = "code_id")
  private ElternmithilfeCode elternmithilfeCode;

  @Column(name = "kuchen_vorstellung_1", nullable = false)
  private Boolean kuchenVorstellung1;

  @Column(name = "kuchen_vorstellung_2", nullable = false)
  private Boolean kuchenVorstellung2;

  @Column(name = "kuchen_vorstellung_3", nullable = false)
  private Boolean kuchenVorstellung3;

  @Column(name = "kuchen_vorstellung_4", nullable = false)
  private Boolean kuchenVorstellung4;

  @Column(name = "kuchen_vorstellung_5", nullable = false)
  private Boolean kuchenVorstellung5;

  @Column(name = "kuchen_vorstellung_6", nullable = false)
  private Boolean kuchenVorstellung6;

  @Column(name = "kuchen_vorstellung_7", nullable = false)
  private Boolean kuchenVorstellung7;

  @Column(name = "kuchen_vorstellung_8", nullable = false)
  private Boolean kuchenVorstellung8;

  @Column(name = "kuchen_vorstellung_9", nullable = false)
  private Boolean kuchenVorstellung9;

  @Column(name = "zusatzattribut")
  private String zusatzattribut;

  @Column(name = "bemerkungen")
  private String bemerkungen;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "drittperson_id")
  private ElternmithilfeDrittperson elternmithilfeDrittperson;

  @Transient private final boolean neusteZuoberst;

  public Maercheneinteilung() {
    Properties svmProperties = SvmProperties.getSvmProperties();
    neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
  }

  @SuppressWarnings("java:S107")
  public Maercheneinteilung(
      Schueler schueler,
      Maerchen maerchen,
      Gruppe gruppe,
      String rolle1,
      String bilderRolle1,
      String rolle2,
      String bilderRolle2,
      String rolle3,
      String bilderRolle3,
      Elternmithilfe elternmithilfe,
      Boolean kuchenVorstellung1,
      Boolean kuchenVorstellung2,
      Boolean kuchenVorstellung3,
      Boolean kuchenVorstellung4,
      Boolean kuchenVorstellung5,
      Boolean kuchenVorstellung6,
      Boolean kuchenVorstellung7,
      Boolean kuchenVorstellung8,
      Boolean kuchenVorstellung9,
      String zusatzattribut,
      String bemerkungen) {
    this();
    this.maerchen = maerchen;
    this.schueler = schueler;
    this.gruppe = gruppe;
    this.rolle1 = rolle1;
    this.bilderRolle1 = bilderRolle1;
    this.rolle2 = rolle2;
    this.bilderRolle2 = bilderRolle2;
    this.rolle3 = rolle3;
    this.bilderRolle3 = bilderRolle3;
    this.elternmithilfe = elternmithilfe;
    this.kuchenVorstellung1 = kuchenVorstellung1;
    this.kuchenVorstellung2 = kuchenVorstellung2;
    this.kuchenVorstellung3 = kuchenVorstellung3;
    this.kuchenVorstellung4 = kuchenVorstellung4;
    this.kuchenVorstellung5 = kuchenVorstellung5;
    this.kuchenVorstellung6 = kuchenVorstellung6;
    this.kuchenVorstellung7 = kuchenVorstellung7;
    this.kuchenVorstellung8 = kuchenVorstellung8;
    this.kuchenVorstellung9 = kuchenVorstellung9;
    this.zusatzattribut = zusatzattribut;
    this.bemerkungen = bemerkungen;
  }

  @SuppressWarnings("java:S3776")
  public boolean isIdenticalWith(Maercheneinteilung otherMaercheneinteilung) {
    return otherMaercheneinteilung != null
        && schueler.isIdenticalWith(otherMaercheneinteilung.getSchueler())
        && maerchen.isIdenticalWith(otherMaercheneinteilung.getMaerchen())
        && gruppe.equals(otherMaercheneinteilung.gruppe)
        && rolle1.equals(otherMaercheneinteilung.rolle1)
        && ((bilderRolle1 == null && otherMaercheneinteilung.bilderRolle1 == null)
            || (bilderRolle1 != null && bilderRolle1.equals(otherMaercheneinteilung.bilderRolle1)))
        && ((rolle2 == null && otherMaercheneinteilung.rolle2 == null)
            || (rolle2 != null && rolle2.equals(otherMaercheneinteilung.rolle2)))
        && ((bilderRolle2 == null && otherMaercheneinteilung.bilderRolle2 == null)
            || (bilderRolle2 != null && bilderRolle2.equals(otherMaercheneinteilung.bilderRolle2)))
        && ((rolle3 == null && otherMaercheneinteilung.rolle3 == null)
            || (rolle3 != null && rolle3.equals(otherMaercheneinteilung.rolle3)))
        && ((bilderRolle3 == null && otherMaercheneinteilung.bilderRolle3 == null)
            || (bilderRolle3 != null && bilderRolle3.equals(otherMaercheneinteilung.bilderRolle3)))
        && ((elternmithilfe == null && otherMaercheneinteilung.elternmithilfe == null)
            || (elternmithilfe != null
                && elternmithilfe.equals(otherMaercheneinteilung.elternmithilfe)))
        && ((elternmithilfeCode == null && otherMaercheneinteilung.elternmithilfeCode == null)
            || (elternmithilfeCode != null
                && elternmithilfeCode.isIdenticalWith(otherMaercheneinteilung.elternmithilfeCode)))
        && kuchenVorstellung1.equals(otherMaercheneinteilung.kuchenVorstellung1)
        && kuchenVorstellung2.equals(otherMaercheneinteilung.kuchenVorstellung2)
        && kuchenVorstellung3.equals(otherMaercheneinteilung.kuchenVorstellung3)
        && kuchenVorstellung4.equals(otherMaercheneinteilung.kuchenVorstellung4)
        && kuchenVorstellung5.equals(otherMaercheneinteilung.kuchenVorstellung5)
        && kuchenVorstellung6.equals(otherMaercheneinteilung.kuchenVorstellung6)
        && kuchenVorstellung7.equals(otherMaercheneinteilung.kuchenVorstellung7)
        && kuchenVorstellung8.equals(otherMaercheneinteilung.kuchenVorstellung8)
        && kuchenVorstellung9.equals(otherMaercheneinteilung.kuchenVorstellung9)
        && ((zusatzattribut == null && otherMaercheneinteilung.zusatzattribut == null)
            || (zusatzattribut != null
                && zusatzattribut.equals(otherMaercheneinteilung.zusatzattribut)))
        && ((elternmithilfeDrittperson == null
                && otherMaercheneinteilung.elternmithilfeDrittperson == null)
            || (elternmithilfeDrittperson != null
                && elternmithilfeDrittperson.isIdenticalWith(
                    otherMaercheneinteilung.elternmithilfeDrittperson)));
  }

  public void copyAttributesFrom(Maercheneinteilung otherMaercheneinteilung) {
    this.gruppe = otherMaercheneinteilung.getGruppe();
    this.rolle1 = otherMaercheneinteilung.getRolle1();
    this.bilderRolle1 = otherMaercheneinteilung.getBilderRolle1();
    this.rolle2 = otherMaercheneinteilung.getRolle2();
    this.bilderRolle2 = otherMaercheneinteilung.getBilderRolle2();
    this.rolle3 = otherMaercheneinteilung.getRolle3();
    this.bilderRolle3 = otherMaercheneinteilung.getBilderRolle3();
    this.elternmithilfe = otherMaercheneinteilung.getElternmithilfe();
    this.kuchenVorstellung1 = otherMaercheneinteilung.getKuchenVorstellung1();
    this.kuchenVorstellung2 = otherMaercheneinteilung.getKuchenVorstellung2();
    this.kuchenVorstellung3 = otherMaercheneinteilung.getKuchenVorstellung3();
    this.kuchenVorstellung4 = otherMaercheneinteilung.getKuchenVorstellung4();
    this.kuchenVorstellung5 = otherMaercheneinteilung.getKuchenVorstellung5();
    this.kuchenVorstellung6 = otherMaercheneinteilung.getKuchenVorstellung6();
    this.kuchenVorstellung7 = otherMaercheneinteilung.getKuchenVorstellung7();
    this.kuchenVorstellung8 = otherMaercheneinteilung.getKuchenVorstellung8();
    this.kuchenVorstellung9 = otherMaercheneinteilung.getKuchenVorstellung9();
    this.zusatzattribut = otherMaercheneinteilung.getZusatzattribut();
    this.bemerkungen = otherMaercheneinteilung.getBemerkungen();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Maercheneinteilung that = (Maercheneinteilung) o;
    return Objects.equals(schueler, that.schueler) && Objects.equals(maerchen, that.maerchen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(schueler, maerchen);
  }

  @Override
  public int compareTo(Maercheneinteilung otherMaercheneinteilung) {
    return (neusteZuoberst
        ? otherMaercheneinteilung.getMaerchen().getSchuljahr().compareTo(maerchen.getSchuljahr())
        : maerchen.getSchuljahr().compareTo(otherMaercheneinteilung.getMaerchen().getSchuljahr()));
  }

  public Schueler getSchueler() {
    return schueler;
  }

  public void setSchueler(Schueler schueler) {
    this.schueler = schueler;
  }

  public Maerchen getMaerchen() {
    return maerchen;
  }

  public void setMaerchen(Maerchen maerchen) {
    this.maerchen = maerchen;
  }

  public Gruppe getGruppe() {
    return gruppe;
  }

  public void setGruppe(Gruppe gruppe) {
    this.gruppe = gruppe;
  }

  public String getRolle1() {
    return rolle1;
  }

  public void setRolle1(String rolle1) {
    this.rolle1 = rolle1;
  }

  public String getBilderRolle1() {
    return bilderRolle1;
  }

  public void setBilderRolle1(String bilderRolle1) {
    this.bilderRolle1 = bilderRolle1;
  }

  public String getRolle2() {
    return rolle2;
  }

  public void setRolle2(String rolle2) {
    this.rolle2 = rolle2;
  }

  public String getBilderRolle2() {
    return bilderRolle2;
  }

  public void setBilderRolle2(String bilderRolle2) {
    this.bilderRolle2 = bilderRolle2;
  }

  public String getRolle3() {
    return rolle3;
  }

  public void setRolle3(String rolle3) {
    this.rolle3 = rolle3;
  }

  public String getBilderRolle3() {
    return bilderRolle3;
  }

  public void setBilderRolle3(String bilderRolle3) {
    this.bilderRolle3 = bilderRolle3;
  }

  public Elternmithilfe getElternmithilfe() {
    return elternmithilfe;
  }

  public void setElternmithilfe(Elternmithilfe elternmithilfe) {
    this.elternmithilfe = elternmithilfe;
  }

  public ElternmithilfeCode getElternmithilfeCode() {
    return elternmithilfeCode;
  }

  public void setElternmithilfeCode(ElternmithilfeCode elternmithilfeCode) {
    if (this.elternmithilfeCode != null) {
      deleteElternmithilfeCode(this.elternmithilfeCode);
    }
    if (elternmithilfeCode != null
        && !elternmithilfeCode.getMaercheneinteilungen().contains(this)) {
      elternmithilfeCode.getMaercheneinteilungen().add(this);
    }
    this.elternmithilfeCode = elternmithilfeCode;
  }

  public void deleteElternmithilfeCode(ElternmithilfeCode elternmithilfeCode) {
    elternmithilfeCode.getMaercheneinteilungen().remove(this);
    this.elternmithilfeCode = null;
  }

  public Boolean getKuchenVorstellung1() {
    return kuchenVorstellung1;
  }

  public void setKuchenVorstellung1(Boolean kuchenVorstellung1) {
    this.kuchenVorstellung1 = kuchenVorstellung1;
  }

  public Boolean getKuchenVorstellung2() {
    return kuchenVorstellung2;
  }

  public void setKuchenVorstellung2(Boolean kuchenVorstellung2) {
    this.kuchenVorstellung2 = kuchenVorstellung2;
  }

  public Boolean getKuchenVorstellung3() {
    return kuchenVorstellung3;
  }

  public void setKuchenVorstellung3(Boolean kuchenVorstellung3) {
    this.kuchenVorstellung3 = kuchenVorstellung3;
  }

  public Boolean getKuchenVorstellung4() {
    return kuchenVorstellung4;
  }

  public void setKuchenVorstellung4(Boolean kuchenVorstellung4) {
    this.kuchenVorstellung4 = kuchenVorstellung4;
  }

  public Boolean getKuchenVorstellung5() {
    return kuchenVorstellung5;
  }

  public void setKuchenVorstellung5(Boolean kuchenVorstellung5) {
    this.kuchenVorstellung5 = kuchenVorstellung5;
  }

  public Boolean getKuchenVorstellung6() {
    return kuchenVorstellung6;
  }

  public void setKuchenVorstellung6(Boolean kuchenVorstellung6) {
    this.kuchenVorstellung6 = kuchenVorstellung6;
  }

  public Boolean getKuchenVorstellung7() {
    return kuchenVorstellung7;
  }

  public void setKuchenVorstellung7(Boolean kuchenVorstellung7) {
    this.kuchenVorstellung7 = kuchenVorstellung7;
  }

  public Boolean getKuchenVorstellung8() {
    return kuchenVorstellung8;
  }

  public void setKuchenVorstellung8(Boolean kuchenVorstellung8) {
    this.kuchenVorstellung8 = kuchenVorstellung8;
  }

  public Boolean getKuchenVorstellung9() {
    return kuchenVorstellung9;
  }

  public void setKuchenVorstellung9(Boolean kuchenVorstellung9) {
    this.kuchenVorstellung9 = kuchenVorstellung9;
  }

  @SuppressWarnings("java:S3776")
  public String getKuchenVorstellungenAsString() {
    StringBuilder vorstellungenKuchenSb = new StringBuilder();
    if (kuchenVorstellung1 != null && kuchenVorstellung1) {
      vorstellungenKuchenSb.append("1, ");
    }
    if (kuchenVorstellung2 != null && kuchenVorstellung2) {
      vorstellungenKuchenSb.append("2, ");
    }
    if (kuchenVorstellung3 != null && kuchenVorstellung3) {
      vorstellungenKuchenSb.append("3, ");
    }
    if (kuchenVorstellung4 != null && kuchenVorstellung4) {
      vorstellungenKuchenSb.append("4, ");
    }
    if (kuchenVorstellung5 != null && kuchenVorstellung5) {
      vorstellungenKuchenSb.append("5, ");
    }
    if (kuchenVorstellung6 != null && kuchenVorstellung6) {
      vorstellungenKuchenSb.append("6, ");
    }
    if (kuchenVorstellung7 != null && kuchenVorstellung7) {
      vorstellungenKuchenSb.append("7, ");
    }
    if (kuchenVorstellung8 != null && kuchenVorstellung8) {
      vorstellungenKuchenSb.append("8, ");
    }
    if (kuchenVorstellung9 != null && kuchenVorstellung9) {
      vorstellungenKuchenSb.append("9, ");
    }
    if (!vorstellungenKuchenSb.isEmpty()) {
      // Letztes ", " löschen
      vorstellungenKuchenSb.setLength(vorstellungenKuchenSb.length() - 2);
    }
    return vorstellungenKuchenSb.toString();
  }

  public String getZusatzattribut() {
    return zusatzattribut;
  }

  public void setZusatzattribut(String zusatzattribut) {
    this.zusatzattribut = zusatzattribut;
  }

  public String getBemerkungen() {
    return bemerkungen;
  }

  public void setBemerkungen(String bemerkungen) {
    this.bemerkungen = bemerkungen;
  }

  public ElternmithilfeDrittperson getElternmithilfeDrittperson() {
    return elternmithilfeDrittperson;
  }

  public void setElternmithilfeDrittperson(ElternmithilfeDrittperson elternmithilfeDrittperson) {
    this.elternmithilfeDrittperson = elternmithilfeDrittperson;
  }

  @Transient
  public String getRolle1WithoutSorterCharacters() {
    // "x Hund 2", "x 3 Hund 2", "xx 3 Hund 2", "X Hund 2", "X 3 Hund 2" und "XX 3 Hund 2" durch
    // "Hund 2" ersetzen
    return rolle1.replaceFirst("^([A-Z]{1,2}|[a-z]{1,2})[ \\t]+\\d*[ \\t]*", "");
  }

  @Transient
  public String getRollenAllWithoutSorterCharacters() {
    StringBuilder rollen = new StringBuilder();
    rollen.append(getRolle1WithoutSorterCharacters());
    if (rolle2 != null && !rolle2.isEmpty()) {
      rollen.append(" / ");
      rollen.append(rolle2);
    }
    if (rolle3 != null && !rolle3.isEmpty()) {
      rollen.append(" / ");
      rollen.append(rolle3);
    }
    return rollen.toString();
  }
}
