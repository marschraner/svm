package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.dataTypes.Anrede;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Lehrkraft")
@DiscriminatorValue("Lehrkraft")
public class Lehrkraft extends Person {

    @Column(name = "ahvnummer", nullable = false)
    private String ahvNummer;

    @Column(name = "vertretungsmoeglichkeiten", nullable = true)
    private String vertretungsmoeglichkeiten;

    @Column(name = "aktiv", nullable = false)
    private Boolean aktiv;

    public Lehrkraft() {
    }

    public Lehrkraft(Anrede anrede, String vorname, String nachname, Calendar geburstdatum, String festnetz, String natel, String email, String ahvNummer, String vertretungsmoeglichkeiten, Boolean aktiv) {
        super(anrede, vorname, nachname, geburstdatum, festnetz, natel, email);
        this.ahvNummer = ahvNummer;
        this.vertretungsmoeglichkeiten = vertretungsmoeglichkeiten;
        this.aktiv = aktiv;
    }

    public boolean isIdenticalWith(Lehrkraft otherLehrkraft) {
        return otherLehrkraft != null
                && getVorname().equals(otherLehrkraft.getVorname())
                && getNachname().equals(otherLehrkraft.getNachname())
                && ahvNummer.equals(otherLehrkraft.getAhvNummer());
    }

    public void copyFieldValuesFrom(Lehrkraft lehrkraftFrom) {
        super.copyFieldValuesFrom(lehrkraftFrom);
        ahvNummer = lehrkraftFrom.getAhvNummer();
        vertretungsmoeglichkeiten = lehrkraftFrom.getVertretungsmoeglichkeiten();
        aktiv = lehrkraftFrom.isAktiv();
    }

    @Override
    public String toString() {
        return getVorname() + " " + getNachname();
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
}
