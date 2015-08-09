package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Elternteil;
import ch.metzenthin.svm.common.dataTypes.Gruppe;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Maercheneinteilung")
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

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Enumerated(EnumType.STRING)
    @Column(name = "gruppe", nullable = false)
    private Gruppe gruppe;

    @Column(name = "rolle_1", nullable = false)
    private String rolle1;

    @Column(name = "bilder_rolle_1", nullable = true)
    private String bilderRolle1;

    @Column(name = "rolle_2", nullable = true)
    private String rolle2;

    @Column(name = "bilder_rolle_2", nullable = true)
    private String bilderRolle2;

    @Column(name = "rolle_3", nullable = true)
    private String rolle3;

    @Column(name = "bilder_rolle_3", nullable = true)
    private String bilderRolle3;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "elternmithilfe", nullable = true)
    private Elternteil elternmithilfe;

    @ManyToOne
    @JoinColumn(name = "code_id", nullable = true)
    private ElternmithilfeCode elternmithilfeCode;

    @Column(name = "vorstellungen_kuchen", nullable = true)
    private String vorstellungenKuchen;

    @Column(name = "zusatzattribut", nullable = true)
    private String zusatzattribut;

    @Column(name = "bemerkungen", nullable = true)
    private String bemerkungen;

    public Maercheneinteilung() {
    }

    public Maercheneinteilung(Schueler schueler, Maerchen maerchen, Gruppe gruppe, String rolle1, String bilderRolle1, String rolle2, String bilderRolle2, String rolle3, String bilderRolle3, Elternteil elternmithilfe, String vorstellungenKuchen, String zusatzattribut, String bemerkungen) {
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
        this.vorstellungenKuchen = vorstellungenKuchen;
        this.zusatzattribut = zusatzattribut;
        this.bemerkungen = bemerkungen;
    }

    @Override
    public int compareTo(Maercheneinteilung otherMaercheneinteilung) {
        return otherMaercheneinteilung.getMaerchen().getSchuljahr().compareTo(this.getMaerchen().getSchuljahr());
    }

    public boolean isIdenticalWith(Maercheneinteilung otherMaercheneinteilung) {
        return otherMaercheneinteilung != null
                && schueler.isIdenticalWith(otherMaercheneinteilung.getSchueler())
                && maerchen.isIdenticalWith(otherMaercheneinteilung.getMaerchen())
                && gruppe.equals(otherMaercheneinteilung.gruppe)
                && rolle1.equals(otherMaercheneinteilung.rolle1)
                && ((bilderRolle1 == null && otherMaercheneinteilung.bilderRolle1 == null) || (bilderRolle1 != null && bilderRolle1.equals(otherMaercheneinteilung.bilderRolle1)))
                && ((rolle2 == null && otherMaercheneinteilung.rolle2 == null) || (rolle2 != null && rolle2.equals(otherMaercheneinteilung.rolle2)))
                && ((bilderRolle2 == null && otherMaercheneinteilung.bilderRolle2 == null) || (bilderRolle2 != null && bilderRolle2.equals(otherMaercheneinteilung.bilderRolle2)))
                && ((rolle3 == null && otherMaercheneinteilung.rolle3 == null) || (rolle3 != null && rolle3.equals(otherMaercheneinteilung.rolle3)))
                && ((bilderRolle3 == null && otherMaercheneinteilung.bilderRolle3 == null) || (bilderRolle3 != null && bilderRolle3.equals(otherMaercheneinteilung.bilderRolle3)))
                && ((elternmithilfe == null && otherMaercheneinteilung.elternmithilfe == null) || (elternmithilfe != null && elternmithilfe.equals(otherMaercheneinteilung.elternmithilfe)))
                && ((elternmithilfeCode == null && otherMaercheneinteilung.elternmithilfeCode == null) || (elternmithilfeCode != null && elternmithilfeCode.isIdenticalWith(otherMaercheneinteilung.elternmithilfeCode)))
                && ((vorstellungenKuchen == null && otherMaercheneinteilung.vorstellungenKuchen == null) || (vorstellungenKuchen != null && vorstellungenKuchen.equals(otherMaercheneinteilung.vorstellungenKuchen)))
                && ((zusatzattribut == null && otherMaercheneinteilung.zusatzattribut == null) || (zusatzattribut != null && zusatzattribut.equals(otherMaercheneinteilung.zusatzattribut)));
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
        this.vorstellungenKuchen = otherMaercheneinteilung.getVorstellungenKuchen();
        this.zusatzattribut = otherMaercheneinteilung.getZusatzattribut();
        this.bemerkungen = otherMaercheneinteilung.getBemerkungen();
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

    public Elternteil getElternmithilfe() {
        return elternmithilfe;
    }

    public void setElternmithilfe(Elternteil elternmithilfe) {
        this.elternmithilfe = elternmithilfe;
    }

    public ElternmithilfeCode getElternmithilfeCode() {
        return elternmithilfeCode;
    }

    public void setElternmithilfeCode(ElternmithilfeCode elternmithilfeCode) {
        if (this.elternmithilfeCode != null) {
            deleteElternmithilfeCode(this.elternmithilfeCode);
        }
        if (elternmithilfeCode != null) {
            elternmithilfeCode.getMaercheneinteilungen().add(this);
        }
        this.elternmithilfeCode = elternmithilfeCode;
    }

    public void deleteElternmithilfeCode(ElternmithilfeCode elternmithilfeCode) {
        elternmithilfeCode.getMaercheneinteilungen().remove(this);
        this.elternmithilfeCode = null;
    }

    public String getVorstellungenKuchen() {
        return vorstellungenKuchen;
    }

    public void setVorstellungenKuchen(String vorstellungenKuchen) {
        this.vorstellungenKuchen = vorstellungenKuchen;
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
}
