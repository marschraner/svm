package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Code")
public class Code implements Comparable<Code> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Integer codeId;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Column(name = "kuerzel", nullable = false)
    private String kuerzel;

    @Column(name = "beschreibung", nullable = false)
    private String beschreibung;

    @ManyToMany(mappedBy = "codes")
    private Set<Schueler> schueler = new HashSet<>();

    public Code() {
    }

    public Code(String kuerzel, String beschreibung) {
        this.kuerzel = kuerzel;
        this.beschreibung = beschreibung;
    }

    public boolean isIdenticalWith(Code otherCode) {
        return otherCode != null
                && ((kuerzel == null && otherCode.getKuerzel() == null) || (kuerzel != null && kuerzel.equals(otherCode.getKuerzel())))
                && ((beschreibung == null && otherCode.getBeschreibung() == null) || (beschreibung != null && beschreibung.equals(otherCode.getBeschreibung())));
    }

    public void copyAttributesFrom(Code otherCode) {
        this.kuerzel = otherCode.getKuerzel();
        this.beschreibung = otherCode.getBeschreibung();
    }

    @Override
    public String toString() {
        return kuerzel + " (" + beschreibung + ")";
    }

    @Override
    public int compareTo(Code otherDispensation) {
        // aufsteigend nach Kuerzel sortieren, d.h. neuste Einträge zuoberst
        return kuerzel.compareTo(otherDispensation.kuerzel);
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Set<Schueler> getSchueler() {
        return schueler;
    }

}
