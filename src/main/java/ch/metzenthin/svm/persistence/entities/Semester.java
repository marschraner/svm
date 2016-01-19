package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.CalendarUtils.getNumberOfWeeksBetween;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Semester")
public class Semester implements Comparable<Semester> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Integer semesterId;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Column(name = "schuljahr", nullable = false)
    private String schuljahr;

    @Enumerated(EnumType.STRING)
    @Column(name = "semesterbezeichnung", nullable = false)
    private Semesterbezeichnung semesterbezeichnung;

    @Temporal(TemporalType.DATE)
    @Column(name = "semesterbeginn", nullable = false)
    private Calendar semesterbeginn;

    @Temporal(TemporalType.DATE)
    @Column(name = "semesterende", nullable = false)
    private Calendar semesterende;

    @OneToMany(mappedBy = "semester")
    private Set<Kurs> kurse = new HashSet<>();

    @OneToMany(mappedBy = "semester")
    private Set<Semesterrechnung> semesterrechnungen = new HashSet<>();

    public Semester() {
    }

    public Semester(String schuljahr, Semesterbezeichnung semesterbezeichnung, Calendar semesterbeginn, Calendar semesterende) {
        this.schuljahr = schuljahr;
        this.semesterbezeichnung = semesterbezeichnung;
        this.semesterbeginn = semesterbeginn;
        this.semesterende = semesterende;
    }

    public boolean isIdenticalWith(Semester otherSemester) {
        return otherSemester != null
                && ((schuljahr == null && otherSemester.getSchuljahr() == null) || (schuljahr != null && schuljahr.equals(otherSemester.getSchuljahr())))
                && ((semesterbezeichnung == null && otherSemester.getSemesterbezeichnung() == null) || (semesterbezeichnung != null && semesterbezeichnung.equals(otherSemester.getSemesterbezeichnung())))
                && ((semesterbeginn == null && otherSemester.getSemesterbeginn() == null) || (semesterbeginn != null && semesterbeginn.equals(otherSemester.getSemesterbeginn())))
                && ((semesterende == null && otherSemester.getSemesterende() == null) || (semesterende != null && semesterende.equals(otherSemester.getSemesterende())));
    }

    public void copyAttributesFrom(Semester otherSemester) {
        this.schuljahr = otherSemester.getSchuljahr();
        this.semesterbezeichnung = otherSemester.getSemesterbezeichnung();
        this.semesterbeginn = otherSemester.getSemesterbeginn();
        this.semesterende = otherSemester.getSemesterende();
    }

    @Override
    public String toString() {
        return schuljahr + ", " + semesterbezeichnung;
    }

    @Override
    public int compareTo(Semester otherSemester) {
        // absteigend nach Semesterbeginn und Semesterende sortieren, d.h. neuste Einträge zuoberst
        int result = otherSemester.semesterbeginn.compareTo(semesterbeginn);
        if (result == 0) {
            if (semesterende != null && otherSemester.semesterende != null) {
                result = otherSemester.semesterende.compareTo(semesterende);
            } else if (otherSemester.semesterende != null) {
                result = -1;
            } else if (semesterende != null) {
                result = 1;
            }
        }
        return result;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public String getSchuljahr() {
        return schuljahr;
    }

    public void setSchuljahr(String schuljahr) {
        this.schuljahr = schuljahr;
    }

    public Semesterbezeichnung getSemesterbezeichnung() {
        return semesterbezeichnung;
    }

    public void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung) {
        this.semesterbezeichnung = semesterbezeichnung;
    }

    public Calendar getSemesterbeginn() {
        return semesterbeginn;
    }

    public void setSemesterbeginn(Calendar dispensationbeginn) {
        this.semesterbeginn = dispensationbeginn;
    }

    public Calendar getSemesterende() {
        return semesterende;
    }

    public void setSemesterende(Calendar semesterende) {
        this.semesterende = semesterende;
    }

    @Transient
    public int getAnzahlSchulwochen() {
        int anzahlSchulwochen = getNumberOfWeeksBetween(semesterbeginn, semesterende);
        int anzFerienWochen;
        if (semesterbezeichnung == Semesterbezeichnung.ERSTES_SEMESTER) {
            anzFerienWochen = 4;
        } else {
            anzFerienWochen = 2;
        }
        return anzahlSchulwochen - anzFerienWochen;
    }

    public Set<Kurs> getKurse() {
        return kurse;
    }

    public Set<Semesterrechnung> getSemesterrechnungen() {
        return semesterrechnungen;
    }
}
