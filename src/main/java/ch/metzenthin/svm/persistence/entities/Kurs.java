package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.dataTypes.Wochentag;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;

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
    @OrderColumn
    @JoinTable(name = "Kurs_Lehrkraft",
            joinColumns = {@JoinColumn(name = "kurs_id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")})
    private List<Lehrkraft> lehrkraefte = new ArrayList<>();

    @Column(name = "bemerkungen", nullable = true)
    private String bemerkungen;

    @ManyToMany(mappedBy = "kurse")
    private Set<Schueler> schueler = new HashSet<>();

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
        StringBuilder kursAsStr = new StringBuilder(kurstyp + " " + stufe + ", " + wochentag + " " + asString(zeitBeginn) + "-" + asString(zeitEnde) + " (" + lehrkraefte.get(0));
        for (int i = 1; i < lehrkraefte.size(); i++) {
            kursAsStr.append("/").append(lehrkraefte.get(i));
        }
        kursAsStr.append(")");
        return kursAsStr.toString();
    }

    public boolean isIdenticalWith(Kurs otherKurs) {
        // Kurse identisch, falls Semester, Wochentag, Zeit und Lehrkräfte identisch
        List<Lehrkraft> commonLehrkraefte = new ArrayList<>(lehrkraefte);
        // RetainAll: nur diejenigen Lehrkräfte in commonLehrkraefte behalten, die auch in otherKurs enthalten sind
        if (otherKurs != null) {
            commonLehrkraefte.retainAll(otherKurs.getLehrkraefte());
        }
        return otherKurs != null
                && semester.equals(otherKurs.semester)
                && wochentag.equals(otherKurs.wochentag)
                && zeitBeginn.equals(otherKurs.zeitBeginn)
                && zeitEnde.equals(otherKurs.zeitEnde)
                && commonLehrkraefte.size() > 0;
    }

    public void copyAttributesFrom(Kurs otherKurs) {
        this.altersbereich = otherKurs.getAltersbereich();
        this.stufe = otherKurs.getStufe();
        this.wochentag = otherKurs.getWochentag();
        this.zeitBeginn = otherKurs.getZeitBeginn();
        this.zeitEnde = otherKurs.getZeitEnde();
        this.bemerkungen = otherKurs.getBemerkungen();
    }

    /**
     * Note: this class has a natural ordering that is inconsistent with equals.
     */
    @Override
    public int compareTo(Kurs otherKurs) {
        int result = otherKurs.semester.getSemesterbeginn().compareTo(semester.getSemesterbeginn());   // Semester: absteigend
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

    public Set<Schueler> getSchueler() {
        return schueler;
    }
}
