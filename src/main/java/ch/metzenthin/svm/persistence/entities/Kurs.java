package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.dataTypes.Wochentag;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Kurs")
public class Kurs implements Comparable<Kurs> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kurs_id")
    private Integer kursId;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "kurstyp_id", nullable = false)
    private Kurstyp kurstyp;

    @Column(name = "altersbereich", nullable = false)
    private String altersbereich;

    @Column(name = "stufe", nullable = false)
    private String stufe;

    @Enumerated(EnumType.STRING)
    @Column(name = "wochentag", nullable = false)
    private Wochentag wochentag;

    @Column(name = "zeit_beginn", nullable = false)
    private Time zeitBeginn;

    @Column(name = "zeit_ende", nullable = false)
    private Time zeitEnde;

    @ManyToOne
    @JoinColumn(name = "kursort_id", nullable = false)
    private Kursort kursort;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "Kurs_Lehrkraft",
            joinColumns = {@JoinColumn(name = "kurs_id")},
            inverseJoinColumns = {@JoinColumn(name = "lehrkraft_id")})
    @OrderBy("nachname ASC, vorname ASC")
    private List<Lehrkraft> lehrkraefte = new ArrayList<>();

    @Column(name = "bemerkungen", nullable = true)
    private String bemerkungen;

    public Kurs() {
    }

    public Kurs(String altersbereich, String stufe, Wochentag wochentag, Time zeitBeginn, Time zeitEnde, String bemerkungen) {
        this.altersbereich = altersbereich;
        this.stufe = stufe;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.zeitEnde = zeitEnde;
        this.bemerkungen = bemerkungen;
    }

    @Override
    public String toString() {
        String kursAsStr = semester + ", " + kurstyp + ", " + stufe + ", " + wochentag + " " + zeitBeginn + "-" + zeitEnde + ", Ort: " + kursort + ", Leitung: " + lehrkraefte.get(0);
        for (int i = 1; i < lehrkraefte.size(); i++) {
            kursAsStr = kursAsStr + "/" + lehrkraefte.get(i);
        }
        return kursAsStr;
    }

    public boolean isIdenticalWith(Kurs otherKurs) {
        return otherKurs != null
                && semester.equals(otherKurs.semester)
                && kurstyp.equals(otherKurs.kurstyp)
                && stufe.equals(otherKurs.stufe)
                && wochentag.equals(otherKurs.wochentag)
                && zeitBeginn.equals(otherKurs.zeitBeginn)
                && zeitEnde.equals(otherKurs.zeitEnde)
                && kursort.equals(otherKurs.kursort);
    }

    public void copyAttributesFrom(Kurs otherKurs) {
        this.altersbereich = otherKurs.getAltersbereich();
        this.stufe = otherKurs.getStufe();
        this.wochentag = otherKurs.getWochentag();
        this.zeitBeginn = otherKurs.getZeitBeginn();
        this.zeitEnde = otherKurs.getZeitEnde();
        this.bemerkungen = otherKurs.getBemerkungen();
    }

    @Override
    public int compareTo(Kurs otherKurs) {
        int result = semester.compareTo(otherKurs.semester);
        if (result == 0) {
            result = kurstyp.compareTo(otherKurs.kurstyp);
            if (result == 0) {
                result = stufe.compareTo(otherKurs.stufe);
                if (result == 0) {
                    result = wochentag.compareTo(otherKurs.wochentag);
                    if (result == 0) {
                        result = zeitBeginn.compareTo(otherKurs.zeitBeginn);
                        if (result == 0) {
                            result = zeitEnde.compareTo(otherKurs.zeitEnde);
                            if (result == 0) {
                                result = kursort.compareTo(otherKurs.kursort);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public Integer getKursId() {
        return kursId;
    }

    public void setKursId(Integer kursId) {
        this.kursId = kursId;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        if (this.semester != null) {
            deleteSemester(this.semester);
        }
        if (semester != null) {
            semester.getKurse().add(this);
        }
        this.semester = semester;
    }

    public void deleteSemester(Semester semester) {
        semester.getKurse().remove(this);
        this.semester = null;
    }

    public Kurstyp getKurstyp() {
        return kurstyp;
    }

    public void setKurstyp(Kurstyp kurstyp) {
        if (this.kurstyp != null) {
            deleteKurstyp(this.kurstyp);
        }
        if (kurstyp != null) {
            kurstyp.getKurse().add(this);
        }
        this.kurstyp = kurstyp;
    }

    public void deleteKurstyp(Kurstyp kurstyp) {
        kurstyp.getKurse().remove(this);
        this.kurstyp = null;
    }

    public String getAltersbereich() {
        return altersbereich;
    }

    public void setAltersbereich(String altersbereich) {
        this.altersbereich = altersbereich;
    }

    public String getStufe() {
        return stufe;
    }

    public void setStufe(String stufe) {
        this.stufe = stufe;
    }

    public Wochentag getWochentag() {
        return wochentag;
    }

    public void setWochentag(Wochentag wochentag) {
        this.wochentag = wochentag;
    }

    public Time getZeitBeginn() {
        return zeitBeginn;
    }

    public void setZeitBeginn(Time zeitBeginn) {
        this.zeitBeginn = zeitBeginn;
    }

    public Time getZeitEnde() {
        return zeitEnde;
    }

    public void setZeitEnde(Time zeitEnde) {
        this.zeitEnde = zeitEnde;
    }

    public Kursort getKursort() {
        return kursort;
    }

    public void setKursort(Kursort kursort) {
        if (this.kursort != null) {
            deleteKursort(this.kursort);
        }
        if (kursort != null) {
            kursort.getKurse().add(this);
        }
        this.kursort = kursort;
    }

    public void deleteKursort(Kursort kursort) {
        kursort.getKurse().remove(this);
        this.kursort = null;
    }

    public List<Lehrkraft> getLehrkraefte() {
        return lehrkraefte;
    }

    public void addLehrkraft(Lehrkraft lehrkraft) {
        lehrkraft.getKurse().add(this);
        lehrkraefte.add(lehrkraft);
        Collections.sort(lehrkraefte);
    }

    public void deleteLehrkraft(Lehrkraft lehrkraft) {
        lehrkraft.getKurse().remove(this);
        lehrkraefte.remove(lehrkraft);
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }
}
