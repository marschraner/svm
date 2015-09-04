package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Kursanmeldung")
@IdClass(KursanmeldungId.class)
public class Kursanmeldung implements Comparable<Kursanmeldung> {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Schueler schueler;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "kurs_id")
    private Kurs kurs;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Column(name = "abmeldung_per_ende_semester", nullable = false)
    private Boolean abmeldungPerEndeSemester;

    @Column(name = "bemerkungen", nullable = true)
    private String bemerkungen;

    public Kursanmeldung() {
    }

    public Kursanmeldung(Schueler schueler, Kurs kurs, Boolean abmeldungPerEndeSemester, String bemerkungen) {
        this.kurs = kurs;
        this.schueler = schueler;
        this.abmeldungPerEndeSemester = abmeldungPerEndeSemester;
        this.bemerkungen = bemerkungen;
    }

    @Override
    public int compareTo(Kursanmeldung otherKursanmeldung) {
        // aufsteigend nach Schüler und Kursen sortieren
        int result = this.schueler.compareTo(otherKursanmeldung.schueler);
        if (result == 0) {
            result = this.kurs.compareTo(otherKursanmeldung.kurs);
        }
        return result;
    }

    public boolean isIdenticalWith(Kursanmeldung otherKursanmeldung) {
        return otherKursanmeldung != null
                && schueler.isIdenticalWith(otherKursanmeldung.getSchueler())
                && kurs.isIdenticalWith(otherKursanmeldung.getKurs())
                && abmeldungPerEndeSemester.equals(otherKursanmeldung.abmeldungPerEndeSemester);
    }

    public void copyAttributesFrom(Kursanmeldung otherKursanmeldung) {
        this.abmeldungPerEndeSemester = otherKursanmeldung.getAbmeldungPerEndeSemester();
        this.bemerkungen = otherKursanmeldung.getBemerkungen();
    }

    @Override
    public String toString() {
        String kursanmeldungStr = kurs.toString();
        if (abmeldungPerEndeSemester || checkNotEmpty(bemerkungen)) {
            kursanmeldungStr = kursanmeldungStr + "&nbsp &nbsp &nbsp (";
            if (abmeldungPerEndeSemester) {
                kursanmeldungStr = kursanmeldungStr + "Abmeldung per Ende Semester";
                if (checkNotEmpty(bemerkungen)) {
                    kursanmeldungStr = kursanmeldungStr + "; ";
                }
            }
            if (checkNotEmpty(bemerkungen)) {
                kursanmeldungStr = kursanmeldungStr + bemerkungen;
            }
            kursanmeldungStr = kursanmeldungStr + ")";
        }
        return kursanmeldungStr;
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public void setSchueler(Schueler schueler) {
        this.schueler = schueler;
    }

    public Kurs getKurs() {
        return kurs;
    }

    public void setKurs(Kurs kurs) {
        this.kurs = kurs;
    }

    public Boolean getAbmeldungPerEndeSemester() {
        return abmeldungPerEndeSemester;
    }

    public void setAbmeldungPerEndeSemester(Boolean abmeldungPerEndeSemester) {
        this.abmeldungPerEndeSemester = abmeldungPerEndeSemester;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }
}
