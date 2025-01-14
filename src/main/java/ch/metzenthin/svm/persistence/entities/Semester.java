package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.*;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.getNumberOfWeeksBetween;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "Semester")
public class Semester implements Comparable<Semester> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Integer semesterId;

    @SuppressWarnings("unused")
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

    @Temporal(TemporalType.DATE)
    @Column(name = "ferienbeginn1", nullable = false)
    private Calendar ferienbeginn1;

    @Temporal(TemporalType.DATE)
    @Column(name = "ferienende1", nullable = false)
    private Calendar ferienende1;

    @Temporal(TemporalType.DATE)
    @Column(name = "ferienbeginn2")
    private Calendar ferienbeginn2;

    @Temporal(TemporalType.DATE)
    @Column(name = "ferienende2")
    private Calendar ferienende2;

    @OneToMany(mappedBy = "semester")
    private final List<Kurs> kurse = new ArrayList<>();

    @OneToMany(mappedBy = "semester", cascade = CascadeType.REMOVE)
    private final List<Semesterrechnung> semesterrechnungen = new ArrayList<>();

    public Semester() {
    }

    @SuppressWarnings("java:S107")
    public Semester(String schuljahr, Semesterbezeichnung semesterbezeichnung, Calendar semesterbeginn, Calendar semesterende, Calendar ferienbeginn1, Calendar ferienende1, Calendar ferienbeginn2, Calendar ferienende2) {
        this.schuljahr = schuljahr;
        this.semesterbezeichnung = semesterbezeichnung;
        this.semesterbeginn = semesterbeginn;
        this.semesterende = semesterende;
        this.ferienbeginn1 = ferienbeginn1;
        this.ferienende1 = ferienende1;
        this.ferienbeginn2 = ferienbeginn2;
        this.ferienende2 = ferienende2;
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
        this.ferienbeginn1 = otherSemester.getFerienbeginn1();
        this.ferienende1 = otherSemester.getFerienende1();
        this.ferienbeginn2 = otherSemester.getFerienbeginn2();
        this.ferienende2 = otherSemester.getFerienende2();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Semester semester = (Semester) o;
        return Objects.equals(schuljahr, semester.schuljahr)
                && semesterbezeichnung == semester.semesterbezeichnung;
    }

    @Override
    public int hashCode() {
        return Objects.hash(schuljahr, semesterbezeichnung);
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

    @Override
    public String toString() {
        return schuljahr + ", " + semesterbezeichnung;
    }

    @Transient
    public boolean isAfter(Semester otherSemester) {
        // compareTo sortiert absteigend!
        return compareTo(otherSemester) < 0;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    @SuppressWarnings("unused")
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

    public void setSemesterbeginn(Calendar semesterbeginn) {
        this.semesterbeginn = semesterbeginn;
    }

    public Calendar getSemesterende() {
        return semesterende;
    }

    public void setSemesterende(Calendar semesterende) {
        this.semesterende = semesterende;
    }

    public Calendar getFerienbeginn1() {
        return ferienbeginn1;
    }

    public void setFerienbeginn1(Calendar ferienbeginn1) {
        this.ferienbeginn1 = ferienbeginn1;
    }

    public Calendar getFerienende1() {
        return ferienende1;
    }

    public void setFerienende1(Calendar ferienende1) {
        this.ferienende1 = ferienende1;
    }

    public Calendar getFerienbeginn2() {
        return ferienbeginn2;
    }

    public void setFerienbeginn2(Calendar ferienbeginn2) {
        this.ferienbeginn2 = ferienbeginn2;
    }

    public Calendar getFerienende2() {
        return ferienende2;
    }

    public void setFerienende2(Calendar ferienende2) {
        this.ferienende2 = ferienende2;
    }

    @Transient
    public int getAnzahlSchulwochen() {
        int anzahlSchulwochen = getNumberOfWeeksBetween(semesterbeginn, semesterende);
        int anzFerienWochen = 0;
        if (ferienbeginn1 != null && ferienende1 != null) {
            anzFerienWochen += getNumberOfWeeksBetween(ferienbeginn1, ferienende1);
        }
        if (ferienbeginn2 != null && ferienende2 != null) {
            anzFerienWochen += getNumberOfWeeksBetween(ferienbeginn2, ferienende2);
        }
        return anzahlSchulwochen - anzFerienWochen;
    }

    public List<Kurs> getKurse() {
        return kurse;
    }

    public List<Semesterrechnung> getSemesterrechnungen() {
        return semesterrechnungen;
    }

}
