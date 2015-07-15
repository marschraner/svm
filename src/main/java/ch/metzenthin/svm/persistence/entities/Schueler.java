package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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
    @OrderBy("anmeldedatum DESC , abmeldedatum DESC")
    private List<Anmeldung> anmeldungen = new ArrayList<>();

    @OneToMany(mappedBy = "schueler", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dispensationsbeginn DESC, dispensationsende DESC")
    private List<Dispensation> dispensationen = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "Schueler_Code",
            joinColumns = {@JoinColumn(name = "schueler_id")},
            inverseJoinColumns = {@JoinColumn(name = "code_id")})
    @OrderBy("kuerzel ASC")
    private List<Code> codes = new ArrayList<>();

    public Schueler() {
    }

    public Schueler(String vorname, String nachname, Calendar geburtsdatum, String festnetz, String natel, String email, Geschlecht geschlecht, String bemerkungen) {
        super(Anrede.KEINE, vorname, nachname, geburtsdatum, festnetz, natel, email);
        this.geschlecht = geschlecht;
        this.bemerkungen = bemerkungen;
    }

    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(Geschlecht geschlecht) {
        this.geschlecht = geschlecht;
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
        if (this.vater != null) {
            deleteVater(this.vater);
        }
        if (vater != null) {
            vater.getKinderVater().add(this);
        }
        this.vater = vater;
    }

    public void deleteVater(Angehoeriger vater) {
        vater.getKinderVater().remove(this);
        this.vater = null;
    }

    public Angehoeriger getMutter() {
        return mutter;
    }

    public void setMutter(Angehoeriger mutter) {
        if (this.mutter != null) {
            deleteMutter(this.mutter);
        }
        if (mutter != null) {
            mutter.getKinderMutter().add(this);
        }
        this.mutter = mutter;
    }

    public void deleteMutter(Angehoeriger mutter) {
        mutter.getKinderMutter().remove(this);
        this.mutter = null;
    }

    public Angehoeriger getRechnungsempfaenger() {
        return rechnungsempfaenger;
    }

    public void setRechnungsempfaenger(Angehoeriger rechnungsempfaenger) {
        if (this.rechnungsempfaenger != null) {
            deleteRechnungsempfaenger(this.rechnungsempfaenger);
        }
        if (rechnungsempfaenger != null) {
            rechnungsempfaenger.getSchuelerRechnungsempfaenger().add(this);
        }
        this.rechnungsempfaenger = rechnungsempfaenger;
    }

    public void deleteRechnungsempfaenger(Angehoeriger rechnungsempfaenger) {
        rechnungsempfaenger.getSchuelerRechnungsempfaenger().remove(this);
        this.rechnungsempfaenger = null;
    }

    public void addAnmeldung(Anmeldung anmeldung) {
        anmeldung.setSchueler(this);
        anmeldungen.add(anmeldung);
        Collections.sort(anmeldungen);
    }

    public List<Anmeldung> getAnmeldungen() {
        return anmeldungen;
    }

    public List<Dispensation> getDispensationen() {
        return dispensationen;
    }

    public void deleteAnmeldung(Anmeldung anmeldung) {
        anmeldungen.remove(anmeldung);
    }

    public void addDispensation(Dispensation dispensation) {
        dispensation.setSchueler(this);
        dispensationen.add(dispensation);
        Collections.sort(dispensationen);
    }

    public void deleteDispensation(Dispensation dispensation) {
        dispensationen.remove(dispensation);
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void addCode(Code code) {
        code.getSchueler().add(this);
        codes.add(code);
        Collections.sort(codes);
    }

    public void deleteCode(Code code) {
        code.getSchueler().remove(this);
        codes.remove(code);
    }

    public boolean isEmpty() {
        return super.isEmpty()
                && (bemerkungen == null || bemerkungen.trim().isEmpty());
    }

    public void copyFieldValuesFrom(Schueler schuelerFrom) {
        super.copyFieldValuesFrom(schuelerFrom);
        geschlecht = schuelerFrom.getGeschlecht();
        bemerkungen = schuelerFrom.getBemerkungen();
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
