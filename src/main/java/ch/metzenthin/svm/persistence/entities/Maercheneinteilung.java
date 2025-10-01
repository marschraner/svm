package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.datatypes.Elternmithilfe;
import ch.metzenthin.svm.common.datatypes.Gruppe;
import ch.metzenthin.svm.common.utils.SvmProperties;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Maercheneinteilung")
@IdClass(MaercheneinteilungId.class)
@Setter
@Getter
public class Maercheneinteilung extends AbstractEntity implements Comparable<Maercheneinteilung> {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "person_id")
  private Schueler schueler;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "maerchen_id")
  private Maerchen maerchen;

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
  private boolean kuchenVorstellung1;

  @Column(name = "kuchen_vorstellung_2", nullable = false)
  private boolean kuchenVorstellung2;

  @Column(name = "kuchen_vorstellung_3", nullable = false)
  private boolean kuchenVorstellung3;

  @Column(name = "kuchen_vorstellung_4", nullable = false)
  private boolean kuchenVorstellung4;

  @Column(name = "kuchen_vorstellung_5", nullable = false)
  private boolean kuchenVorstellung5;

  @Column(name = "kuchen_vorstellung_6", nullable = false)
  private boolean kuchenVorstellung6;

  @Column(name = "kuchen_vorstellung_7", nullable = false)
  private boolean kuchenVorstellung7;

  @Column(name = "kuchen_vorstellung_8", nullable = false)
  private boolean kuchenVorstellung8;

  @Column(name = "kuchen_vorstellung_9", nullable = false)
  private boolean kuchenVorstellung9;

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
      boolean kuchenVorstellung1,
      boolean kuchenVorstellung2,
      boolean kuchenVorstellung3,
      boolean kuchenVorstellung4,
      boolean kuchenVorstellung5,
      boolean kuchenVorstellung6,
      boolean kuchenVorstellung7,
      boolean kuchenVorstellung8,
      boolean kuchenVorstellung9,
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
        && kuchenVorstellung1 == otherMaercheneinteilung.kuchenVorstellung1
        && kuchenVorstellung2 == otherMaercheneinteilung.kuchenVorstellung2
        && kuchenVorstellung3 == otherMaercheneinteilung.kuchenVorstellung3
        && kuchenVorstellung4 == otherMaercheneinteilung.kuchenVorstellung4
        && kuchenVorstellung5 == otherMaercheneinteilung.kuchenVorstellung5
        && kuchenVorstellung6 == otherMaercheneinteilung.kuchenVorstellung6
        && kuchenVorstellung7 == otherMaercheneinteilung.kuchenVorstellung7
        && kuchenVorstellung8 == otherMaercheneinteilung.kuchenVorstellung8
        && kuchenVorstellung9 == otherMaercheneinteilung.kuchenVorstellung9
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
    this.kuchenVorstellung1 = otherMaercheneinteilung.isKuchenVorstellung1();
    this.kuchenVorstellung2 = otherMaercheneinteilung.isKuchenVorstellung2();
    this.kuchenVorstellung3 = otherMaercheneinteilung.isKuchenVorstellung3();
    this.kuchenVorstellung4 = otherMaercheneinteilung.isKuchenVorstellung4();
    this.kuchenVorstellung5 = otherMaercheneinteilung.isKuchenVorstellung5();
    this.kuchenVorstellung6 = otherMaercheneinteilung.isKuchenVorstellung6();
    this.kuchenVorstellung7 = otherMaercheneinteilung.isKuchenVorstellung7();
    this.kuchenVorstellung8 = otherMaercheneinteilung.isKuchenVorstellung8();
    this.kuchenVorstellung9 = otherMaercheneinteilung.isKuchenVorstellung9();
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

  @SuppressWarnings("java:S3776")
  @Transient
  public String getKuchenVorstellungenAsString() {
    StringBuilder vorstellungenKuchenSb = new StringBuilder();
    if (kuchenVorstellung1) {
      vorstellungenKuchenSb.append("1, ");
    }
    if (kuchenVorstellung2) {
      vorstellungenKuchenSb.append("2, ");
    }
    if (kuchenVorstellung3) {
      vorstellungenKuchenSb.append("3, ");
    }
    if (kuchenVorstellung4) {
      vorstellungenKuchenSb.append("4, ");
    }
    if (kuchenVorstellung5) {
      vorstellungenKuchenSb.append("5, ");
    }
    if (kuchenVorstellung6) {
      vorstellungenKuchenSb.append("6, ");
    }
    if (kuchenVorstellung7) {
      vorstellungenKuchenSb.append("7, ");
    }
    if (kuchenVorstellung8) {
      vorstellungenKuchenSb.append("8, ");
    }
    if (kuchenVorstellung9) {
      vorstellungenKuchenSb.append("9, ");
    }
    if (!vorstellungenKuchenSb.isEmpty()) {
      // Letztes ", " löschen
      vorstellungenKuchenSb.setLength(vorstellungenKuchenSb.length() - 2);
    }
    return vorstellungenKuchenSb.toString();
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
