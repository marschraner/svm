package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.asString;
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

    @Temporal(TemporalType.DATE)
    @Column(name = "anmeldedatum", nullable = true)
    private Calendar anmeldedatum;

    @Temporal(TemporalType.DATE)
    @Column(name = "abmeldedatum", nullable = true)
    private Calendar abmeldedatum;

    @Column(name = "bemerkungen", nullable = true)
    private String bemerkungen;

    public Kursanmeldung() {
    }

    public Kursanmeldung(Schueler schueler, Kurs kurs, Calendar anmeldedatum, Calendar abmeldedatum, String bemerkungen) {
        this.kurs = kurs;
        this.schueler = schueler;
        this.anmeldedatum = anmeldedatum;
        this.abmeldedatum = abmeldedatum;
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
                && kurs.isIdenticalWith(otherKursanmeldung.getKurs());
    }

    public void copyAttributesFrom(Kursanmeldung otherKursanmeldung) {
        this.anmeldedatum = otherKursanmeldung.getAnmeldedatum();
        this.abmeldedatum = otherKursanmeldung.getAbmeldedatum();
        this.bemerkungen = otherKursanmeldung.getBemerkungen();
    }

    @Override
    public String toString() {
        StringBuilder kursanmeldungSb = new StringBuilder(kurs.toString());
        if (anmeldedatum != null || abmeldedatum != null || checkNotEmpty(bemerkungen)) {
            kursanmeldungSb.append("&nbsp &nbsp &nbsp (");
            if (anmeldedatum != null) {
                kursanmeldungSb.append("Anmeldung: ").append(asString(anmeldedatum));
                if (abmeldedatum != null) {
                    kursanmeldungSb.append(", ");
                } else if (checkNotEmpty(bemerkungen)) {
                    kursanmeldungSb.append("; ");
                }
            }
            if (abmeldedatum != null) {
                kursanmeldungSb.append("Abmeldung: ").append(asString(abmeldedatum));
                if (checkNotEmpty(bemerkungen)) {
                    kursanmeldungSb.append("; ");
                }
            }
            if (checkNotEmpty(bemerkungen)) {
                kursanmeldungSb.append(bemerkungen);
            }
            kursanmeldungSb.append(")");
        }
        return kursanmeldungSb.toString();
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

    public Calendar getAnmeldedatum() {
        return anmeldedatum;
    }

    public void setAnmeldedatum(Calendar anmeldedatum) {
        this.anmeldedatum = anmeldedatum;
    }

    public Calendar getAbmeldedatum() {
        return abmeldedatum;
    }

    public void setAbmeldedatum(Calendar abmeldedatum) {
        this.abmeldedatum = abmeldedatum;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }
}
