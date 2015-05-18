package ch.metzenthin.svm.model.entities;

import ch.metzenthin.svm.dataTypes.Anrede;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Angehoeriger")
@DiscriminatorValue("Angehoeriger")
public class Angehoeriger extends Person {

    @Column(name = "beruf", nullable = true)
    private String beruf;

    @OneToMany(mappedBy = "vater")
    private Set<Schueler> kinderVater = new HashSet<>();

    @OneToMany(mappedBy = "mutter")
    private Set<Schueler> kinderMutter = new HashSet<>();

    @OneToMany(mappedBy = "rechnungsempfaenger")
    private Set<Schueler> schuelerRechnungsempfaenger = new HashSet<>();

    public Angehoeriger() {
    }

    public Angehoeriger(Anrede anrede, String vorname, String nachname, Calendar geburtsdatum, String natel, String email, String beruf) {
        super(anrede, vorname, nachname, geburtsdatum, natel, email);
        this.beruf = beruf;
    }

    public String getBeruf() {
        return beruf;
    }

    public void setBeruf(String beruf) {
        this.beruf = beruf;
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
}
