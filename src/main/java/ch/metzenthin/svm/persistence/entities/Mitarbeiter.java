package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Anrede;

import javax.persistence.*;
import java.util.*;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Mitarbeiter")
@DiscriminatorValue("Mitarbeiter")
public class Mitarbeiter extends Person {

    @Column(name = "ahvnummer", nullable = true)
    private String ahvNummer;
    
    @Column(name = "lehrkraft", nullable = false)
    private boolean lehrkraft;

    @Column(name = "vertretungsmoeglichkeiten", columnDefinition = "text", nullable = true)
    private String vertretungsmoeglichkeiten;

    @Column(name = "bemerkungen", columnDefinition = "text", nullable = true)
    private String bemerkungen;

    @Column(name = "aktiv", nullable = false)
    private Boolean aktiv;

    @Transient
    private boolean zuExportieren = true;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "Mitarbeiter_MitarbeiterCode",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "code_id")})
    private Set<MitarbeiterCode> mitarbeiterCodes = new HashSet<>();

    @ManyToMany(mappedBy = "lehrkraefte")
    private Set<Kurs> kurse = new HashSet<>();

    public Mitarbeiter() {
    }

    public Mitarbeiter(Anrede anrede, String vorname, String nachname, Calendar geburtsdatum, String festnetz, String natel, String email, String ahvNummer, boolean lehrkraft, String vertretungsmoeglichkeiten, String bemerkungen, Boolean aktiv) {
        super(anrede, vorname, nachname, geburtsdatum, festnetz, natel, email);
        this.ahvNummer = ahvNummer;
        this.lehrkraft = lehrkraft;
        this.vertretungsmoeglichkeiten = vertretungsmoeglichkeiten;
        this.bemerkungen = bemerkungen;
        this.aktiv = aktiv;
    }

    public boolean isIdenticalWith(Mitarbeiter otherMitarbeiter) {
        return otherMitarbeiter != null
                && getVorname().equals(otherMitarbeiter.getVorname())
                && getNachname().equals(otherMitarbeiter.getNachname())
                && ((getGeburtsdatum() == null && otherMitarbeiter.getGeburtsdatum() == null) || (getGeburtsdatum() != null && getGeburtsdatum().equals(otherMitarbeiter.getGeburtsdatum())));
    }

    public void copyAttributesFrom(Mitarbeiter mitarbeiterFrom) {
        super.copyAttributesFrom(mitarbeiterFrom);
        ahvNummer = mitarbeiterFrom.getAhvNummer();
        lehrkraft = mitarbeiterFrom.getLehrkraft();
        vertretungsmoeglichkeiten = mitarbeiterFrom.getVertretungsmoeglichkeiten();
        bemerkungen = mitarbeiterFrom.getBemerkungen();
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

    public boolean getLehrkraft() {
        return lehrkraft;
    }

    public void setLehrkraft(boolean lehrkraft) {
        this.lehrkraft = lehrkraft;
    }

    public String getVertretungsmoeglichkeiten() {
        return vertretungsmoeglichkeiten;
    }

    public void setVertretungsmoeglichkeiten(String vertretungsmoeglichkeiten) {
        this.vertretungsmoeglichkeiten = vertretungsmoeglichkeiten;
    }

    public String getBemerkungen() {
        return bemerkungen;
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    public Boolean getAktiv() {
        return aktiv;
    }

    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }

    public boolean isZuExportieren() {
        return zuExportieren;
    }

    public void setZuExportieren(boolean zuExportieren) {
        this.zuExportieren = zuExportieren;
    }

    public Set<MitarbeiterCode> getMitarbeiterCodes() {
        return mitarbeiterCodes;
    }

    public List<MitarbeiterCode> getMitarbeiterCodesAsList() {
        List<MitarbeiterCode> mitarbeitercodesAsList = new ArrayList<>(mitarbeiterCodes);
        Collections.sort(mitarbeitercodesAsList);
        return mitarbeitercodesAsList;
    }

    public void addCode(MitarbeiterCode mitarbeiterCode) {
        mitarbeiterCode.getMitarbeiters().add(this);
        mitarbeiterCodes.add(mitarbeiterCode);
    }

    public void deleteCode(MitarbeiterCode mitarbeiterCode) {
        // mitarbeiterCode.getMitarbeiter().remove(this); führt zu StaleObjectStateException!
        // Stattdessen in MitarbeiterCodeDao.removeFromMitarbeiterAndUpdate() refresh(mitarbeiterCode) ausführen!
        mitarbeiterCodes.remove(mitarbeiterCode);
    }

    @Transient
    public String getMitarbeiterCodesAsStr() {
        if (mitarbeiterCodes.isEmpty()) {
            return "";
        }
        StringBuilder mitarbeiterCodesAsStr = new StringBuilder(getMitarbeiterCodesAsList().get(0).getKuerzel());
        for (int i = 1; i < getMitarbeiterCodesAsList().size(); i++) {
            mitarbeiterCodesAsStr.append(", ").append(getMitarbeiterCodesAsList().get(i).getKuerzel());
        }
        return mitarbeiterCodesAsStr.toString();
    }

    public Set<Kurs> getKurse() {
        return kurse;
    }

}
