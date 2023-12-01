package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.text.Collator;
import java.util.Locale;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Code")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator")
public abstract class Code implements Comparable<Code> {

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

    @Column(name = "selektierbar", nullable = false)
    private Boolean selektierbar;

    public Code() {
    }

    public Code(String kuerzel, String beschreibung, Boolean selektierbar) {
        this.kuerzel = kuerzel;
        this.beschreibung = beschreibung;
        this.selektierbar = selektierbar;
    }

    public boolean isIdenticalWith(Code otherCode) {
        return otherCode != null
                && ((kuerzel == null && otherCode.getKuerzel() == null) || (kuerzel != null && kuerzel.equals(otherCode.getKuerzel())))
                && ((beschreibung == null && otherCode.getBeschreibung() == null) || (beschreibung != null && beschreibung.equals(otherCode.getBeschreibung())));
    }

    public void copyAttributesFrom(Code otherCode) {
        this.kuerzel = otherCode.getKuerzel();
        this.beschreibung = otherCode.getBeschreibung();
        this.selektierbar = otherCode.getSelektierbar();
    }

    @Override
    public String toString() {
        if (kuerzel == null || kuerzel.equals("")) {
            return "";
        }
        return kuerzel + " (" + beschreibung + ")";
    }

    @Override
    public int compareTo(Code otherDispensation) {
        // Alphabetische Sortierung mit Berücksichtigung von Umlauten http://50226.de/sortieren-mit-umlauten-in-java.html
        Collator collator = Collator.getInstance(Locale.GERMAN);
        collator.setStrength(Collator.SECONDARY);// a == A, a < Ä
        return collator.compare(kuerzel, otherDispensation.kuerzel);
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

    public Boolean getSelektierbar() {
        return selektierbar;
    }

    public void setSelektierbar(Boolean selektierbar) {
        this.selektierbar = selektierbar;
    }
}
