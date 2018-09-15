package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.common.utils.SvmStringUtils;

import javax.persistence.*;
import java.util.*;

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

    @Transient
    private boolean selektiert = true;

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
    @JoinTable(name = "Schueler_SchuelerCode",
            joinColumns = {@JoinColumn(name = "person_id")},
            inverseJoinColumns = {@JoinColumn(name = "code_id")})
    private Set<SchuelerCode> schuelerCodes = new HashSet<>();

    @OneToMany(mappedBy = "schueler")
    private Set<Kursanmeldung> kursanmeldungen = new HashSet<>();

    @OneToMany(mappedBy = "schueler")
    private Set<Maercheneinteilung> maercheneinteilungen = new HashSet<>();

    public Schueler() {
    }

    public Schueler(String vorname, String nachname, Calendar geburtsdatum, String festnetz, String natel, String email, Geschlecht geschlecht, String bemerkungen) {
        super(Anrede.KEINE, vorname, nachname, geburtsdatum, festnetz, natel, email);
        this.geschlecht = geschlecht;
        this.bemerkungen = bemerkungen;
    }

    public boolean isEmpty() {
        return super.isEmpty()
                && (bemerkungen == null || bemerkungen.trim().isEmpty());
    }

    public void copyFieldValuesFrom(Schueler schuelerFrom) {
        super.copyAttributesFrom(schuelerFrom);
        geschlecht = schuelerFrom.getGeschlecht();
        bemerkungen = schuelerFrom.getBemerkungen();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String toStringForGuiWithAbgemeldetInfo() {
        String abgemeldetInfo = (isAbgemeldet() ? "&nbsp (abgemeldet)" : "");
        return super.toString() + abgemeldetInfo;
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

    @Transient
    public String getBemerkungenLineBreaksReplacedByHtmlBr() {
        return SvmStringUtils.replaceLineBreaksByHtmlBr(bemerkungen);
    }

    public void setBemerkungen(String bemerkungen) {
        this.bemerkungen = bemerkungen;
    }

    public boolean isSelektiert() {
        return selektiert;
    }

    public void setSelektiert(boolean selektiert) {
        this.selektiert = selektiert;
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
        Collections.sort(anmeldungen);
        return anmeldungen;
    }

    public List<Dispensation> getDispensationen() {
        Collections.sort(dispensationen);
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

    public Set<SchuelerCode> getSchuelerCodes() {
        return schuelerCodes;
    }

    public List<SchuelerCode> getSchuelerCodesAsList() {
        List<SchuelerCode> schuelerCodesAsList = new ArrayList<>(schuelerCodes);
        Collections.sort(schuelerCodesAsList);
        return schuelerCodesAsList;
    }

    public void addCode(SchuelerCode schuelerCode) {
        schuelerCode.getSchueler().add(this);
        schuelerCodes.add(schuelerCode);
    }

    public void deleteCode(SchuelerCode schuelerCode) {
        // schuelerCode.getSchueler().remove(this); führt zu StaleObjectStateException!
        // Stattdessen in SchuelerCodeDao.removeFromSchuelerAndUpdate() refresh(schuelerCode) ausführen!
        schuelerCodes.remove(schuelerCode);
    }

    @Transient
    public String getSchuelerCodesAsStr() {
        if (schuelerCodes.isEmpty()) {
            return "";
        }
        StringBuilder schuelerCodesAsStr = new StringBuilder(getSchuelerCodesAsList().get(0).getKuerzel());
        for (int i = 1; i < getSchuelerCodesAsList().size(); i++) {
            schuelerCodesAsStr.append(", ").append(getSchuelerCodesAsList().get(i).getKuerzel());
        }
        return schuelerCodesAsStr.toString();
    }

    public Set<Kursanmeldung> getKursanmeldungen() {
        return kursanmeldungen;
    }

    public List<Kursanmeldung> getKursanmeldungenAsList() {
        List<Kursanmeldung> kursanmeldungenAsList = new ArrayList<>(kursanmeldungen);
        Collections.sort(kursanmeldungenAsList);
        return kursanmeldungenAsList;
    }

    public Set<Maercheneinteilung> getMaercheneinteilungen() {
        return maercheneinteilungen;
    }

    public List<Maercheneinteilung> getMaercheneinteilungenAsList() {
        List<Maercheneinteilung> maercheneinteilungenAsList = new ArrayList<>(maercheneinteilungen);
        Collections.sort(maercheneinteilungenAsList);
        return maercheneinteilungenAsList;
    }

    @Transient
    public boolean isAngemeldet() {
        return getAnmeldungen().get(0).getAbmeldedatum() == null;
    }

    @Transient
    public boolean isAbgemeldet() {
        return !isAngemeldet();
    }

}
