package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Kursort")
public class Kursort implements Comparable<Kursort> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kursort_id")
    private Integer kursortId;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Column(name = "bezeichnung", nullable = false)
    private String bezeichnung;

    public Kursort() {
    }

    public Kursort(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public boolean isIdenticalWith(Kursort otherCode) {
        return otherCode != null
                && ((bezeichnung == null && otherCode.getBezeichnung() == null) || (bezeichnung != null && bezeichnung.equals(otherCode.getBezeichnung())));
    }

    public void copyAttributesFrom(Kursort otherCode) {
        this.bezeichnung = otherCode.getBezeichnung();
    }

    @Override
    public String toString() {
        if (bezeichnung.equals("alle")) {
            return "alle";
        }
        return bezeichnung;
    }

    @Override
    public int compareTo(Kursort otherKursort) {
        // Alphabetische Sortierung
        return bezeichnung.compareTo(otherKursort.bezeichnung);
    }

    public Integer getKursortId() {
        return kursortId;
    }

    public void setKursortId(Integer kursortId) {
        this.kursortId = kursortId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String kursort) {
        this.bezeichnung = kursort;
    }

}
