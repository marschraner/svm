package ch.metzenthin.svm.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Adresse")
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adresse_id")
    private Integer adresseId;

    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Column(name = "strasse", nullable = true)
    private String strasse;

    @Column(name = "hausnummer", nullable = true)
    private String hausnummer;

    @Column(name = "plz", nullable = true)
    private String plz;

    @Column(name = "ort", nullable = true)
    private String ort;

    @Column(name = "festnetz", nullable = true)
    private String festnetz;

    @OneToMany(mappedBy = "adresse")
    private Set<Person> personen = new HashSet<>();

    public Adresse() {
    }

    public Adresse(String strasse, String hausnummer, String plz, String ort, String festnetz) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.festnetz = festnetz;
    }

    public boolean isIdenticalWith(Adresse otherAdresse) {
        if (otherAdresse == null) {
            return false;
        }
        return strasse.equals(otherAdresse.getStrasse())
                && hausnummer.equals(otherAdresse.getHausnummer())
                && plz.equals(otherAdresse.getPlz())
                && ort.equals(otherAdresse.getOrt())
                && festnetz.equals(otherAdresse.getFestnetz());
    }

    public boolean isPartOf(Adresse otherAdresse) {
        if (otherAdresse == null) {
            return false;
        }
        return strasse.equals(otherAdresse.getStrasse())
                && plz.equals(otherAdresse.getPlz())
                && ort.equals(otherAdresse.getOrt())
                && (hausnummer == null || hausnummer.trim().isEmpty() || hausnummer.equals(otherAdresse.getHausnummer()))
                && (festnetz == null || festnetz.trim().isEmpty() || festnetz.equals(otherAdresse.getFestnetz()));
    }

    @Override
    public String toString() {
        StringBuilder adresseSb = new StringBuilder();
        adresseSb.append(strasse);
        if (hausnummer != null && !hausnummer.trim().isEmpty()) {
            adresseSb.append(" " + hausnummer);
        }
        adresseSb.append(", " + plz);
        adresseSb.append(" " + ort);
        if (festnetz != null && !festnetz.isEmpty()) {
            adresseSb.append(", " + festnetz);
        }
        return adresseSb.toString();
    }

    public Integer getAdresseId() {
        return adresseId;
    }

    public void setAdresseId(Integer adresseId) {
        this.adresseId = adresseId;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getFestnetz() {
        return festnetz;
    }

    public void setFestnetz(String festnetz) {
        this.festnetz = festnetz;
    }

    public Set<Person> getPersonen() {
        return personen;
    }
}
