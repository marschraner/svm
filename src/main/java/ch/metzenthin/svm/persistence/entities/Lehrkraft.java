package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.dataTypes.Anrede;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Lehrkraft")
@DiscriminatorValue("Lehrkraft")
public class Lehrkraft extends Person implements Comparable<Lehrkraft> {

    @Column(name = "ahvnummer", nullable = false)
    private String ahvNummer;

    @Column(name = "vertretungsmoeglichkeiten", nullable = true)
    private String vertretungsmoeglichkeiten;

    @Column(name = "aktiv", nullable = false)
    private Boolean aktiv;

    @ManyToMany(mappedBy = "lehrkraefte")
    private Set<Kurs> kurse = new HashSet<>();

    public Lehrkraft() {
    }

    public Lehrkraft(Anrede anrede, String vorname, String nachname, Calendar geburtsdatum, String festnetz, String natel, String email, String ahvNummer, String vertretungsmoeglichkeiten, Boolean aktiv) {
        super(anrede, vorname, nachname, geburtsdatum, festnetz, natel, email);
        this.ahvNummer = ahvNummer;
        this.vertretungsmoeglichkeiten = vertretungsmoeglichkeiten;
        this.aktiv = aktiv;
    }

    public boolean isIdenticalWith(Lehrkraft otherLehrkraft) {
        return otherLehrkraft != null
                && getVorname().equals(otherLehrkraft.getVorname())
                && getNachname().equals(otherLehrkraft.getNachname())
                && getGeburtsdatum().equals(otherLehrkraft.getGeburtsdatum());
    }

    public void copyAttributesFrom(Lehrkraft lehrkraftFrom) {
        super.copyAttributesFrom(lehrkraftFrom);
        ahvNummer = lehrkraftFrom.getAhvNummer();
        vertretungsmoeglichkeiten = lehrkraftFrom.getVertretungsmoeglichkeiten();
        aktiv = lehrkraftFrom.isAktiv();
    }

    @Override
    public int compareTo(Lehrkraft otherLehrkraft) {
        // aufsteigend nach Nachname und Vorname sortieren, d.h. neuste Einträge zuoberst
        int result =  getNachname().compareTo(otherLehrkraft.getNachname());
        if (result == 0) {
            result = getVorname().compareTo(otherLehrkraft.getVorname());
        }
        return result;
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

    public Boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }

    public Set<Kurs> getKurse() {
        return kurse;
    }
}
