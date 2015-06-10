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
        return otherAdresse != null
                && ((strasse == null && otherAdresse.getStrasse() == null) || (strasse != null && strasse.equals(otherAdresse.getStrasse())))
                && ((hausnummer == null && otherAdresse.getHausnummer() == null) || (hausnummer != null && hausnummer.equals(otherAdresse.getHausnummer())))
                && ((plz == null && otherAdresse.getPlz() == null) || (plz != null && plz.equals(otherAdresse.getPlz())))
                && ((ort == null && otherAdresse.getOrt() == null) || (ort != null && ort.equals(otherAdresse.getOrt())))
                && ((festnetz == null && otherAdresse.getFestnetz() == null) || (festnetz != null && festnetz.equals(otherAdresse.getFestnetz())));
    }

    public boolean isPartOf(Adresse otherAdresse) {
        return otherAdresse != null
                && (strasse == null || strasse.trim().isEmpty() || strasse.equals(otherAdresse.getStrasse()))
                && (hausnummer == null || hausnummer.trim().isEmpty() || hausnummer.equals(otherAdresse.getHausnummer()))
                && (plz == null || plz.trim().isEmpty() || plz.equals(otherAdresse.getPlz()))
                && (ort == null || ort.trim().isEmpty() || ort.equals(otherAdresse.getOrt()))
                && (festnetz == null || festnetz.trim().isEmpty() || festnetz.equals(otherAdresse.getFestnetz()));
    }

    public boolean isEmpty() {
        return (strasse == null || strasse.trim().isEmpty())
                && (hausnummer == null || hausnummer.trim().isEmpty())
                && (plz == null || plz.trim().isEmpty())
                && (ort == null || ort.trim().isEmpty())
                && (festnetz == null || festnetz.trim().isEmpty());
    }

    @Override
    public String toString() {
        StringBuilder adresseSb = new StringBuilder();
        if (strasse != null && !strasse.trim().isEmpty()) {
            adresseSb.append(", ").append(strasse);
        }
        if (hausnummer != null && !hausnummer.trim().isEmpty()) {
            adresseSb.append(" ").append(hausnummer);
        }
        if (plz != null && !plz.trim().isEmpty()) {
            adresseSb.append(", ").append(plz);
        }
        if (ort != null && !ort.trim().isEmpty()) {
            adresseSb.append(" ").append(ort);
        }
        if (festnetz != null && !festnetz.isEmpty()) {
            adresseSb.append(", ").append(festnetz);
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
