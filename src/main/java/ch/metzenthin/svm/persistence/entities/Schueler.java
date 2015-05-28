package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "schueler", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Dispensation> dispensationen = new HashSet<>();

    public Schueler() {
    }

    public Schueler(String vorname, String nachname, Calendar geburtsdatum, String natel, String email, Geschlecht geschlecht, Calendar anmeldedatum, Calendar abmeldedatum, String bemerkungen) {
        super(Anrede.KEINE, vorname, nachname, geburtsdatum, natel, email);
        this.geschlecht = geschlecht;
        this.anmeldedatum = anmeldedatum;
        this.abmeldedatum = abmeldedatum;
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

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    public Angehoeriger getVater() {
        return vater;
    }

    public void setNewVater(Angehoeriger vater) {
        vater.getKinderVater().add(this);
        this.vater = vater;
    }

    public void replaceVater(Angehoeriger oldVater, Angehoeriger newVater) {
        deleteVater(oldVater);
        setNewVater(newVater);
    }

    public void deleteVater(Angehoeriger vater) {
        vater.getKinderVater().remove(this);
    }

    public Angehoeriger getMutter() {
        return mutter;
    }

    public void setNewMutter(Angehoeriger mutter) {
        mutter.getKinderMutter().add(this);
        this.mutter = mutter;
    }

    public void replaceMutter(Angehoeriger oldMutter, Angehoeriger newMutter) {
        deleteMutter(oldMutter);
        setNewMutter(newMutter);
    }

    public void deleteMutter(Angehoeriger mutter) {
        mutter.getKinderMutter().remove(this);
    }

    public Angehoeriger getRechnungsempfaenger() {
        return rechnungsempfaenger;
    }

    public void setNewRechnungsempfaenger(Angehoeriger rechnungsempfaenger) {
        rechnungsempfaenger.getSchuelerRechnungsempfaenger().add(this);
        this.rechnungsempfaenger = rechnungsempfaenger;
    }

    public void replaceRechnungsempfaenger(Angehoeriger oldRechnungsempfaenger, Angehoeriger newRechnungsempfaenger) {
        deleteRechnungsempfaenger(oldRechnungsempfaenger);
        setNewRechnungsempfaenger(newRechnungsempfaenger);
    }

    public void deleteRechnungsempfaenger(Angehoeriger rechnungsempfaenger) {
        rechnungsempfaenger.getSchuelerRechnungsempfaenger().remove(this);
    }

    public void addDispensation(Dispensation dispensation) {
        dispensation.setSchueler(this);
        dispensationen.add(dispensation);
    }

    public void deleteDispensation(Dispensation dispensation) {
        dispensationen.remove(dispensation);
    }

    public Set<Dispensation> getDispensationen() {
        return dispensationen;
    }
}
