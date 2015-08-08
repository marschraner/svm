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
public class SchuelerCode implements Comparable<SchuelerCode> {

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

    @ManyToMany(mappedBy = "schuelerCodes")
    private Set<Schueler> schueler = new HashSet<>();

    public SchuelerCode() {
    }

    public SchuelerCode(String kuerzel, String beschreibung) {
        this.kuerzel = kuerzel;
        this.beschreibung = beschreibung;
    }

    public boolean isIdenticalWith(SchuelerCode otherSchuelerCode) {
        return otherSchuelerCode != null
                && ((kuerzel == null && otherSchuelerCode.getKuerzel() == null) || (kuerzel != null && kuerzel.equals(otherSchuelerCode.getKuerzel())))
                && ((beschreibung == null && otherSchuelerCode.getBeschreibung() == null) || (beschreibung != null && beschreibung.equals(otherSchuelerCode.getBeschreibung())));
    }

    public void copyAttributesFrom(SchuelerCode otherSchuelerCode) {
        this.kuerzel = otherSchuelerCode.getKuerzel();
        this.beschreibung = otherSchuelerCode.getBeschreibung();
    }

    @Override
    public String toString() {
        if (kuerzel.equals("")) {
            return "";
        }
        return kuerzel + " (" + beschreibung + ")";
    }

    @Override
    public int compareTo(SchuelerCode otherDispensation) {
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
