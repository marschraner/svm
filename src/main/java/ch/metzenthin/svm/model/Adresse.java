package ch.metzenthin.svm.model;

import javax.persistence.*;
import java.sql.Timestamp;

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

    @Column(name = "strasse")
    private String strasse;

    @Column(name = "hausnummer")
    private Integer hausnummer;

    @Column(name = "plz")
    private Integer plz;

    @Column(name = "ort")
    private String ort;

    @Column(name = "festnetz")
    private String festnetz;

    public Adresse() {
    }

    public Adresse(String strasse, int hausnummer, int plz, String ort, String festnetz) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.festnetz = festnetz;
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
}
