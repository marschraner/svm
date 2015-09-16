package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Anrede;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Mitarbeiter")
@DiscriminatorValue("Mitarbeiter")
public class Mitarbeiter extends Person {

    @Column(name = "ahvnummer", nullable = true)
    private String ahvNummer;

    @Column(name = "vertretungsmoeglichkeiten", nullable = true)
    private String vertretungsmoeglichkeiten;

    @Column(name = "aktiv", nullable = false)
    private Boolean aktiv;

    @ManyToMany(mappedBy = "mitarbeiters")
    private Set<Kurs> kurse = new HashSet<>();

    public Mitarbeiter() {
    }

    public Mitarbeiter(Anrede anrede, String vorname, String nachname, Calendar geburtsdatum, String festnetz, String natel, String email, String ahvNummer, String vertretungsmoeglichkeiten, Boolean aktiv) {
        super(anrede, vorname, nachname, geburtsdatum, festnetz, natel, email);
        this.ahvNummer = ahvNummer;
        this.vertretungsmoeglichkeiten = vertretungsmoeglichkeiten;
        this.aktiv = aktiv;
    }

    public boolean isIdenticalWith(Mitarbeiter otherMitarbeiter) {
        return otherMitarbeiter != null
                && getVorname().equals(otherMitarbeiter.getVorname())
                && getNachname().equals(otherMitarbeiter.getNachname())
                && getGeburtsdatum().equals(otherMitarbeiter.getGeburtsdatum());
    }

    public void copyAttributesFrom(Mitarbeiter mitarbeiterFrom) {
        super.copyAttributesFrom(mitarbeiterFrom);
        ahvNummer = mitarbeiterFrom.getAhvNummer();
        vertretungsmoeglichkeiten = mitarbeiterFrom.getVertretungsmoeglichkeiten();
        aktiv = mitarbeiterFrom.getAktiv();
    }

    @Override
    public String toString() {
        if (getVorname() != null && getNachname() != null) {
            return getVorname() + " " + getNachname();
        } else {
            return "";
        }
    }

    public String toStringShort() {
        if (getVorname() != null && getNachname() != null) {
            return getVorname().substring(0, 1) + ". " + getNachname();
        } else {
            return "";
        }
    }

    public String getAhvNummer() {
        return ahvNummer;
    }

    public void setAhvNummer(String ahvNummer) {
        this.ahvNummer = ahvNummer;
    }

    public String getVertretungsmoeglichkeiten() {
        return vertretungsmoeglichkeiten;
    }

    public void setVertretungsmoeglichkeiten(String vertretungsmoeglichkeiten) {
        this.vertretungsmoeglichkeiten = vertretungsmoeglichkeiten;
    }

    public Boolean getAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }

    public Set<Kurs> getKurse() {
        return kurse;
    }
}
