package ch.metzenthin.svm.model.entities;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Schueler")
@DiscriminatorValue("Schueler")
public class Schueler extends Person {

    @Enumerated(EnumType.STRING)
    @Column(name = "geschlecht", nullable = false)
    private Geschlecht geschlecht;

    @Temporal(TemporalType.DATE)
    @Column(name = "anmeldedatum", nullable = false)
    private Calendar anmeldedatum;

    @Temporal(TemporalType.DATE)
    @Column(name = "abmeldedatum", nullable = true)
    private Calendar abmeldedatum;

    @Temporal(TemporalType.DATE)
    @Column(name = "dispensationsbeginn", nullable = true)
    private Calendar dispensationbeginn;

    @Temporal(TemporalType.DATE)
    @Column(name = "dispensationsende", nullable = true)
    private Calendar dispensationsende;

    @Lob
    @Column(name = "bemerkungen", columnDefinition = "text", nullable = true)
    private String bemerkungen;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vater_id", nullable = true)
    private Angehoeriger vater;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "mutter_id", nullable = true)
    private Angehoeriger mutter;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rechnungsempfaenger_id", nullable = false)
    private Angehoeriger rechnungsempfaenger;

    public Schueler() {
    }

    public Schueler(String vorname, String nachname, Calendar geburtsdatum, String natel, String email, Geschlecht geschlecht, Calendar anmeldedatum, Calendar abmeldedatum, Calendar dispensationbeginn, Calendar dispensationsende, String bemerkungen) {
        super(Anrede.KEINE, vorname, nachname, geburtsdatum, natel, email);
        this.geschlecht = geschlecht;
        this.anmeldedatum = anmeldedatum;
        this.abmeldedatum = abmeldedatum;
        this.dispensationbeginn = dispensationbeginn;
        this.dispensationsende = dispensationsende;
        this.bemerkungen = bemerkungen;
    }

    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(Geschlecht geschlecht) {
        this.geschlecht = geschlecht;
    }

    public Calendar getAnmeldedatum() {
        return anmeldedatum;
    }

    public void setAnmeldedatum(Calendar anmeldedatum) {
        this.anmeldedatum = anmeldedatum;
    }

    public Calendar getAbmeldedatum() {
        return abmeldedatum;
    }

    public void setAbmeldedatum(Calendar abmeldedatum) {
        this.abmeldedatum = abmeldedatum;
    }

    public Calendar getDispensationbeginn() {
        return dispensationbeginn;
    }

    public void setDispensationbeginn(Calendar dispensationbeginn) {
        this.dispensationbeginn = dispensationbeginn;
    }

    public Calendar getDispensationsende() {
        return dispensationsende;
    }

    public void setDispensationsende(Calendar dispensationsende) {
        this.dispensationsende = dispensationsende;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    public Angehoeriger getVater() {
        return vater;
    }

    public void setVater(Angehoeriger vater) {
        vater.getKinderVater().add(this);
        this.vater = vater;
    }

    public Angehoeriger getMutter() {
        return mutter;
    }

    public void setMutter(Angehoeriger mutter) {
        mutter.getKinderMutter().add(this);
        this.mutter = mutter;
    }

    public Angehoeriger getRechnungsempfaenger() {
        return rechnungsempfaenger;
    }

    public void setRechnungsempfaenger(Angehoeriger rechnungsempfaenger) {
        rechnungsempfaenger.getSchuelerRechnungsempfaenger().add(this);
        this.rechnungsempfaenger = rechnungsempfaenger;
    }
}
