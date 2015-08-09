package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Maerchen")
public class Maerchen implements Comparable<Maerchen> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maerchen_id")
    private Integer maerchenId;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Column(name = "schuljahr", nullable = false)
    private String schuljahr;

    @Column(name = "bezeichnung", nullable = false)
    private String bezeichnung;

    @OneToMany(mappedBy = "maerchen")
    private Set<Maercheneinteilung> maercheneinteilungen = new HashSet<>();

    public Maerchen() {
    }

    public Maerchen(String schuljahr, String bezeichnung) {
        this.schuljahr = schuljahr;
        this.bezeichnung = bezeichnung;
    }

    public boolean isIdenticalWith(Maerchen otherMaerchen) {
        return otherMaerchen != null
                && ((schuljahr == null && otherMaerchen.getSchuljahr() == null) || (schuljahr != null && schuljahr.equals(otherMaerchen.getSchuljahr())))
                && ((bezeichnung == null && otherMaerchen.getBezeichnung() == null) || (bezeichnung != null && bezeichnung.equals(otherMaerchen.getBezeichnung())));
    }

    public void copyAttributesFrom(Maerchen otherMaerchen) {
        this.schuljahr = otherMaerchen.getSchuljahr();
        this.bezeichnung = otherMaerchen.getBezeichnung();
    }

    @Override
    public String toString() {
        return bezeichnung + " (" + schuljahr + ")";
    }

    @Override
    public int compareTo(Maerchen otherMaerchen) {
        // absteigend nach Schuljahr sortieren, d.h. neuste Einträge zuoberst
        return otherMaerchen.schuljahr.compareTo(schuljahr);
    }

    public Integer getMaerchenId() {
        return maerchenId;
    }

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

    public Set<Maercheneinteilung> getMaercheneinteilungen() {
        return maercheneinteilungen;
    }
}
