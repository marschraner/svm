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

    @OneToMany(mappedBy = "vater")
    private Set<Schueler> kinderVater = new HashSet<>();

    @OneToMany(mappedBy = "mutter")
    private Set<Schueler> kinderMutter = new HashSet<>();

    @OneToMany(mappedBy = "rechnungsempfaenger")
    private Set<Schueler> schuelerRechnungsempfaenger = new HashSet<>();

    @OneToMany(mappedBy = "rechnungsempfaenger")
    private Set<Semesterrechnung> semesterrechnungen = new HashSet<>();

    public Angehoeriger() {
    }

    public Angehoeriger(Anrede anrede, String vorname, String nachname, String festnetz, String natel, String email) {
        super(anrede, vorname, nachname, null, festnetz, natel, email);
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

    @Transient
    public String getSchuelerRechnungsempfaengerAsStr() {
        List<Schueler> schuelerRechnungsempfaengerList = new ArrayList<>(schuelerRechnungsempfaenger);
        // Bei Semesterrechnungen, bei denen der Rechnungsempfänger durch ein Ändern des Rechnungsempfängers des Kinds
        // seine Schüler verloren hat
        if (schuelerRechnungsempfaengerList.isEmpty()) {
            return "-";
        }
        Collections.sort(schuelerRechnungsempfaengerList);
        String schuelerStr = schuelerRechnungsempfaengerList.get(0).getNachname() + " " + schuelerRechnungsempfaengerList.get(0).getVorname();
        for (int i = 1; i < schuelerRechnungsempfaengerList.size(); i++) {
            schuelerStr = schuelerStr + ", " + schuelerRechnungsempfaengerList.get(i).getNachname() + " " + schuelerRechnungsempfaengerList.get(i).getVorname();
        }
        return schuelerStr;
    }
}
