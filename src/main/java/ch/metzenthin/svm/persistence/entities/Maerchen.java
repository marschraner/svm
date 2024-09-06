package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Maerchen")
public class Maerchen implements Comparable<Maerchen> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maerchen_id")
    private Integer maerchenId;

    @SuppressWarnings("unused")
    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Column(name = "schuljahr", nullable = false)
    private String schuljahr;

    @Column(name = "bezeichnung", nullable = false)
    private String bezeichnung;

    @Column(name = "anzahl_vorstellungen", nullable = false)
    private Integer anzahlVorstellungen;

    @OneToMany(mappedBy = "maerchen")
    private final Set<Maercheneinteilung> maercheneinteilungen = new HashSet<>();

    public Maerchen() {
    }

    public Maerchen(String schuljahr, String bezeichnung, Integer anzahlVorstellungen) {
        this.schuljahr = schuljahr;
        this.bezeichnung = bezeichnung;
        this.anzahlVorstellungen = anzahlVorstellungen;
    }

    public boolean isIdenticalWith(Maerchen otherMaerchen) {
        return otherMaerchen != null
                && ((schuljahr == null && otherMaerchen.getSchuljahr() == null) || (schuljahr != null && schuljahr.equals(otherMaerchen.getSchuljahr())))
                && ((bezeichnung == null && otherMaerchen.getBezeichnung() == null) || (bezeichnung != null && bezeichnung.equals(otherMaerchen.getBezeichnung())))
                && ((anzahlVorstellungen == null && otherMaerchen.anzahlVorstellungen == null) || (anzahlVorstellungen != null && anzahlVorstellungen.equals(otherMaerchen.anzahlVorstellungen)));
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

    public Integer getMaerchenId() {
        return maerchenId;
    }

    @SuppressWarnings("unused")
    public void setMaerchenId(Integer maerchenId) {
        this.maerchenId = maerchenId;
    }

    public String getSchuljahr() {
        return schuljahr;
    }

    public void setSchuljahr(String schuljahr) {
        this.schuljahr = schuljahr;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Integer getAnzahlVorstellungen() {
        return anzahlVorstellungen;
    }

    public void setAnzahlVorstellungen(Integer anzahlVorstellungen) {
        this.anzahlVorstellungen = anzahlVorstellungen;
    }

    public Set<Maercheneinteilung> getMaercheneinteilungen() {
        return maercheneinteilungen;
    }
}
