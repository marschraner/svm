package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.text.Collator;
import java.util.*;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Kurstyp")
public class Kurstyp implements Comparable<Kurstyp> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kurstyp_id")
    private Integer kurstypId;

    @SuppressWarnings("unused")
    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Column(name = "bezeichnung", nullable = false)
    private String bezeichnung;

    @Column(name = "selektierbar", nullable = false)
    private Boolean selektierbar;

    @OneToMany(mappedBy = "kurstyp")
    private final List<Kurs> kurse = new ArrayList<>();

    public Kurstyp() {
    }

    public Kurstyp(String bezeichnung, Boolean selektierbar) {
        this.bezeichnung = bezeichnung;
        this.selektierbar = selektierbar;
    }

    public boolean isIdenticalWith(Kurstyp otherKurstyp) {
        return otherKurstyp != null
                && ((bezeichnung == null && otherKurstyp.getBezeichnung() == null) || (bezeichnung != null && bezeichnung.equals(otherKurstyp.getBezeichnung())));
    }

    public void copyAttributesFrom(Kurstyp otherKurstyp) {
        this.bezeichnung = otherKurstyp.getBezeichnung();
        this.selektierbar = otherKurstyp.getSelektierbar();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kurstyp kurstyp = (Kurstyp) o;
        return Objects.equals(bezeichnung, kurstyp.bezeichnung);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bezeichnung);
    }

    @Override
    public int compareTo(Kurstyp otherKurstyp) {
        // Sortierung nach Umlauten http://50226.de/sortieren-mit-umlauten-in-java.html
        Collator collator = Collator.getInstance(Locale.GERMAN);
        collator.setStrength(Collator.SECONDARY);// a == A, a < Ä
        return collator.compare(bezeichnung, otherKurstyp.bezeichnung);
    }

    @Override
    public String toString() {
        if (bezeichnung.equals("alle")) {
            return "alle";
        }
        return bezeichnung;
    }

    public Integer getKurstypId() {
        return kurstypId;
    }

    @SuppressWarnings("unused")
    public void setKurstypId(Integer kurstypId) {
        this.kurstypId = kurstypId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String kurstyp) {
        this.bezeichnung = kurstyp;
    }

    public Boolean getSelektierbar() {
        return selektierbar;
    }

    public void setSelektierbar(Boolean selektierbar) {
        this.selektierbar = selektierbar;
    }

    public List<Kurs> getKurse() {
        return kurse;
    }
}
