package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Anrede;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    public Set<Semesterrechnung> getSemesterrechnungen() {
        return semesterrechnungen;
    }
}
