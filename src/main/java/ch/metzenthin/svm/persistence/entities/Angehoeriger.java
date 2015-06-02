package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.dataTypes.Anrede;

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
    @OrderBy("nachname, vorname")
    private Set<Schueler> kinderVater = new HashSet<>();

    @OneToMany(mappedBy = "mutter")
    @OrderBy("nachname, vorname")
    private Set<Schueler> kinderMutter = new HashSet<>();

    @OneToMany(mappedBy = "rechnungsempfaenger")
    @OrderBy("nachname, vorname")
    private Set<Schueler> schuelerRechnungsempfaenger = new HashSet<>();

    public Angehoeriger() {
    }

    public Angehoeriger(Anrede anrede, String vorname, String nachname, String natel, String email) {
        super(anrede, vorname, nachname, null, natel, email);
    }

    public Set<Schueler> getKinderVater() {
        return kinderVater;
    }

    public boolean isIdenticalWith(Angehoeriger otherAngehoeriger) {
        return (super.isIdenticalWith(otherAngehoeriger));
    }

    public boolean isPartOf(Angehoeriger otherAngehoeriger) {
        return (super.isPartOf(otherAngehoeriger));
    }

    public Set<Schueler> getKinderMutter() {
        return kinderMutter;
    }

    public Set<Schueler> getSchuelerRechnungsempfaenger() {
        return schuelerRechnungsempfaenger;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
