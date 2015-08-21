package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Kurstyp")
public class Kurstyp implements Comparable<Kurstyp> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kurstyp_id")
    private Integer kurstypId;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Column(name = "bezeichnung", nullable = false)
    private String bezeichnung;

    @Column(name = "selektierbar", nullable = false)
    private Boolean selektierbar;

    @OneToMany(mappedBy = "kurstyp")
    private Set<Kurs> kurse = new HashSet<>();

    public Kurstyp() {
    }

    public Kurstyp(String bezeichnung, Boolean selektierbar) {
        this.bezeichnung = bezeichnung;
        this.selektierbar = selektierbar;
    }

    public boolean isIdenticalWith(Kurstyp otherCode) {
        return otherCode != null
                && ((bezeichnung == null && otherCode.getBezeichnung() == null) || (bezeichnung != null && bezeichnung.equals(otherCode.getBezeichnung())));
    }

    public void copyAttributesFrom(Kurstyp otherKurstyp) {
        this.bezeichnung = otherKurstyp.getBezeichnung();
        this.selektierbar = otherKurstyp.getSelektierbar();
    }

    @Override
    public String toString() {
        if (bezeichnung.equals("alle")) {
            return "alle";
        }
        return bezeichnung;
    }

    @Override
    public int compareTo(Kurstyp otherDispensation) {
        // Alphabetische Sortierung
        return bezeichnung.compareTo(otherDispensation.bezeichnung);
    }

    public Integer getKurstypId() {
        return kurstypId;
    }

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

    public Set<Kurs> getKurse() {
        return kurse;
    }
}
