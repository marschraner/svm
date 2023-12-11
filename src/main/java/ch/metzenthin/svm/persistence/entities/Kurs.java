package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.common.utils.StringNumberComparator;
import ch.metzenthin.svm.common.utils.SvmProperties;
import jakarta.persistence.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.asString;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Kurs")
public class Kurs implements Comparable<Kurs> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kurs_id")
    private Integer kursId;

    @SuppressWarnings("unused")
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
    private final List<Mitarbeiter> lehrkraefte = new ArrayList<>();

    @Column(name = "bemerkungen")
    private String bemerkungen;

    @OneToMany(mappedBy = "kurs")
    private final Set<Kursanmeldung> kursanmeldungen = new HashSet<>();

    @Transient
    private final boolean neusteZuoberst;

    public Kurs() {
        Properties svmProperties = SvmProperties.getSvmProperties();
        neusteZuoberst = !svmProperties.getProperty(SvmProperties.KEY_NEUSTE_ZUOBERST).equals("false");
    }

    public Kurs(String altersbereich, String stufe, Wochentag wochentag, Time zeitBeginn, Time zeitEnde, String bemerkungen) {
        this();
        this.altersbereich = altersbereich;
        this.stufe = stufe;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.zeitEnde = zeitEnde;
        this.bemerkungen = bemerkungen;
    }

    @Override
    public String toString() {
        String lehrkraft1 = (lehrkraefte.isEmpty() ? "-" : lehrkraefte.get(0).toString());
        StringBuilder kursAsStr = new StringBuilder(kurstyp + " " + stufe + ", " + wochentag + " " + asString(zeitBeginn) + "-" + asString(zeitEnde) + " (" + lehrkraft1);
        for (int i = 1; i < lehrkraefte.size(); i++) {
            kursAsStr.append(" / ").append(lehrkraefte.get(i));
        }
        kursAsStr.append(")");
        return kursAsStr.toString();
    }

    public String toStringShort() {
        StringBuilder kursAsStr = new StringBuilder(wochentag + " " + asString(zeitBeginn) + "-" + asString(zeitEnde) + " (" + lehrkraefte.get(0).toStringShort());
        for (int i = 1; i < lehrkraefte.size(); i++) {
            kursAsStr.append("/").append(lehrkraefte.get(i).toStringShort());
        }
        kursAsStr.append(")");
        return kursAsStr.toString();
    }

    public String getLehrkraefteAsStr() {
        StringBuilder lehrkraefteAsStr = new StringBuilder(lehrkraefte.get(0).toString());
        for (int i = 1; i < lehrkraefte.size(); i++) {
            lehrkraefteAsStr.append(" / ").append(lehrkraefte.get(i).toString());
        }
        return lehrkraefteAsStr.toString();
    }

    public String getLehrkraefteShortAsStr() {
        StringBuilder lehrkraefteAsStr = new StringBuilder(lehrkraefte.get(0).toStringShort());
        for (int i = 1; i < lehrkraefte.size(); i++) {
            lehrkraefteAsStr.append(" / ").append(lehrkraefte.get(i).toStringShort());
        }
        return lehrkraefteAsStr.toString();
    }

    public boolean isIdenticalWith(Kurs otherKurs) {
        // Kurse identisch, falls Semester, Wochentag, Zeit und Mitarbeiter identisch
        List<Mitarbeiter> commonLehrkraefte = new ArrayList<>(lehrkraefte);
        // RetainAll: nur diejenigen Lehrkraefte in commonLehrkraefte behalten, die auch in otherKurs enthalten sind
        if (otherKurs != null) {
            commonLehrkraefte.retainAll(otherKurs.getLehrkraefte());
        }
        return otherKurs != null
                && semester.equals(otherKurs.semester)
                && wochentag.equals(otherKurs.wochentag)
                && zeitBeginn.equals(otherKurs.zeitBeginn)
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
        Comparator<String> stringNumberComparator = new StringNumberComparator();
        int result = (neusteZuoberst ? otherKurs.semester.getSemesterbeginn().compareTo(semester.getSemesterbeginn()) : semester.getSemesterbeginn().compareTo(otherKurs.semester.getSemesterbeginn()));
        if (result == 0) {
            result = kurstyp.compareTo(otherKurs.kurstyp);
            if (result == 0) {
                result = stringNumberComparator.compare(stufe, otherKurs.stufe);
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

    @SuppressWarnings("unused")
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

    public List<Mitarbeiter> getLehrkraefte() {
        return lehrkraefte;
    }

    public void addLehrkraft(Mitarbeiter mitarbeiter) {
        mitarbeiter.getKurse().add(this);
        lehrkraefte.add(mitarbeiter);
    }

    public void deleteLehrkraft(Mitarbeiter mitarbeiter) {
        mitarbeiter.getKurse().remove(this);
        lehrkraefte.remove(mitarbeiter);
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    public Set<Kursanmeldung> getKursanmeldungen() {
        return kursanmeldungen;
    }

    @Transient
    public int getKurslaenge() {
        return (int) ((zeitEnde.getTime() - zeitBeginn.getTime()) / 60000);
    }
}
