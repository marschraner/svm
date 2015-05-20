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

    @Column(name = "strasse", nullable = false)
    private String strasse;

    @Column(name = "hausnummer", nullable = true)
    private Integer hausnummer;

    @Column(name = "plz", nullable = false)
    private Integer plz;

    @Column(name = "ort", nullable = false)
    private String ort;

    @Column(name = "festnetz", nullable = true)
    private String festnetz;

    @OneToMany(mappedBy = "adresse")
    private Set<Person> personen = new HashSet<>();

    public Adresse() {
    }

    public Adresse(String strasse, Integer hausnummer, Integer plz, String ort, String festnetz) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.festnetz = festnetz;
    }

    public boolean isIdenticalWith(Adresse otherAdresse) {
        return strasse.equals(otherAdresse.getStrasse())
                && hausnummer.equals(otherAdresse.getHausnummer())
                && plz.equals(otherAdresse.getPlz())
                && ort.equals(otherAdresse.getOrt())
                && festnetz.equals(otherAdresse.getFestnetz());
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

    public Integer getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(Integer hausnummer) {
        this.hausnummer = hausnummer;
    }

    public Integer getPlz() {
        return plz;
    }

    public void setPlz(Integer plz) {
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
