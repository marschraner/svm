package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Anrede;

import javax.persistence.*;
import java.util.*;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Angehoeriger")
@DiscriminatorValue("Angehoeriger")
public class Angehoeriger extends Person {

    @Column(name = "wuenscht_emails")
    private Boolean wuenschtEmails;

    @OneToMany(mappedBy = "vater")
    private final Set<Schueler> kinderVater = new HashSet<>();

    @OneToMany(mappedBy = "mutter")
    private final Set<Schueler> kinderMutter = new HashSet<>();

    @OneToMany(mappedBy = "rechnungsempfaenger")
    private final Set<Schueler> schuelerRechnungsempfaenger = new HashSet<>();

    @OneToMany(mappedBy = "rechnungsempfaenger")
    private final Set<Semesterrechnung> semesterrechnungen = new HashSet<>();

    public Angehoeriger() {
    }

    public Angehoeriger(Anrede anrede, String vorname, String nachname, String festnetz, String natel, String email, Boolean wuenschtEmails) {
        super(anrede, vorname, nachname, null, festnetz, natel, email);
        this.wuenschtEmails = wuenschtEmails;
    }

    public boolean isIdenticalWith(Angehoeriger otherAngehoeriger) {
        return (super.isIdenticalWith(otherAngehoeriger));
    }

    public boolean isPartOf(Angehoeriger otherAngehoeriger) {
        return (super.isPartOf(otherAngehoeriger));
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }

    public void copyFieldValuesFrom(Angehoeriger angehoerigerFrom) {
        super.copyAttributesFrom(angehoerigerFrom);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Boolean getWuenschtEmails() {
        return wuenschtEmails;
    }

    public void setWuenschtEmails(Boolean wuenschtEmails) {
        this.wuenschtEmails = wuenschtEmails;
    }

    public Set<Schueler> getKinderVater() {
        return kinderVater;
    }

    public Set<Schueler> getKinderMutter() {
        return kinderMutter;
    }

    public Set<Schueler> getSchuelerRechnungsempfaenger() {
        return schuelerRechnungsempfaenger;
    }

    @Transient
    public List<Schueler> getAngemeldeteSchuelerRechnungsempfaengerAsList() {
        List<Schueler> angemeldeteSchuelerRechnungsempfaengerAsList = new ArrayList<>();
        for (Schueler schueler : schuelerRechnungsempfaenger) {
            if (schueler.isAngemeldet()) {
                angemeldeteSchuelerRechnungsempfaengerAsList.add(schueler);
            }
        }
        Collections.sort(angemeldeteSchuelerRechnungsempfaengerAsList);
        return angemeldeteSchuelerRechnungsempfaengerAsList;
    }

    public Set<Semesterrechnung> getSemesterrechnungen() {
        return semesterrechnungen;
    }

    @Transient
    public List<Semesterrechnung> getSemesterrechnungenAsList() {
        List<Semesterrechnung> semesterrechnungenAsList = new ArrayList<>(semesterrechnungen);
        Collections.sort(semesterrechnungenAsList);
        return semesterrechnungenAsList;
    }
}
