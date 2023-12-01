package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.text.Collator;
import java.util.Calendar;
import java.util.Locale;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Person")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator")
public abstract class Person implements Comparable<Person>  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer personId;

    @SuppressWarnings("unused")
    @Version
    @Column(name = "last_updated")
    private Timestamp version;

    @Enumerated(EnumType.STRING)
    @Column(name = "anrede", nullable = false)
    private Anrede anrede;

    @Column(name = "vorname", nullable = false)
    private String vorname;

    @Column(name = "nachname", nullable = false)
    private String nachname;

    @Temporal(TemporalType.DATE)
    @Column(name = "geburtsdatum")
    private Calendar geburtsdatum;

    @Column(name = "festnetz")
    private String festnetz;

    @Column(name = "natel")
    private String natel;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "adresse_id")
    private Adresse adresse;

    public Person() {
    }

    public Person(Anrede anrede, String vorname, String nachname, Calendar geburtsdatum, String festnetz, String natel, String email) {
        this.anrede = anrede;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.festnetz = festnetz;
        this.natel = natel;
        this.email = email;
    }

    public boolean isIdenticalWith(Person otherPerson) {
        return otherPerson != null
                && anrede.equals(otherPerson.getAnrede())
                && vorname.equals(otherPerson.getVorname())
                && nachname.equals(otherPerson.getNachname())
                && ((geburtsdatum == null && otherPerson.getGeburtsdatum() == null) || (geburtsdatum != null && geburtsdatum.equals(otherPerson.getGeburtsdatum())))
                && ((festnetz == null && otherPerson.getFestnetz() == null) || (festnetz != null && festnetz.equals(otherPerson.getFestnetz())))
                && ((natel == null && otherPerson.getNatel() == null) || (natel != null && natel.equals(otherPerson.getNatel())))
                && ((email == null && otherPerson.getEmail() == null) || (email != null && email.equals(otherPerson.getEmail())))
                && ((adresse == null && otherPerson.getAdresse() == null) || (adresse != null && adresse.isIdenticalWith(otherPerson.getAdresse())));
    }

    public boolean isPartOf(Person otherPerson) {
        return otherPerson != null
                && vorname.equals(otherPerson.getVorname())
                && nachname.equals(otherPerson.getNachname())
                && (geburtsdatum == null || geburtsdatum.equals(otherPerson.getGeburtsdatum()))
                && (festnetz == null || festnetz.trim().isEmpty() || festnetz.equals(otherPerson.getFestnetz()))
                && (natel == null || natel.trim().isEmpty() || natel.equals(otherPerson.getNatel()))
                && (email == null || email.trim().isEmpty() || email.equals(otherPerson.getEmail()))
                && (adresse == null || adresse.isPartOf(otherPerson.getAdresse()));
    }

    public boolean isEmpty() {
        return (vorname == null || vorname.trim().isEmpty())
                && (nachname == null || nachname.trim().isEmpty())
                && geburtsdatum == null
                && (festnetz == null || festnetz.trim().isEmpty())
                && (natel == null || natel.trim().isEmpty())
                && (email ==  null || email.trim().isEmpty())
                && (adresse == null || adresse.isEmpty());
    }

    public void copyAttributesFrom(Person personFrom) {
        anrede = personFrom.getAnrede();
        vorname = personFrom.getVorname();
        nachname = personFrom.getNachname();
        geburtsdatum = personFrom.getGeburtsdatum();
        festnetz = personFrom.getFestnetz();
        natel = personFrom.getNatel();
        email = personFrom.getEmail();
    }

    @Override
    public int compareTo(Person otherPerson) {
        // Alphabetische Sortierung mit Berücksichtigung von Umlauten http://50226.de/sortieren-mit-umlauten-in-java.html
        Collator collator = Collator.getInstance(Locale.GERMAN);
        collator.setStrength(Collator.SECONDARY);// a == A, a < Ä
        int result = collator.compare(nachname, otherPerson.nachname);
        if (result == 0) {
            result = collator.compare(vorname, otherPerson.vorname);
            if (result == 0 && adresse != null && otherPerson.adresse != null) {
                result = collator.compare(adresse.getOrt(), otherPerson.adresse.getOrt());
                if (result == 0 && adresse.getStrasse() != null && otherPerson.adresse.getStrasse() != null) {
                    result = collator.compare(adresse.getStrasse(), otherPerson.adresse.getStrasse());
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder personSb = new StringBuilder();
        if (anrede != Anrede.KEINE) {
            personSb.append(anrede).append(" ");
        }
        personSb.append(vorname);
        personSb.append(" ").append(nachname);
        if (adresse != null && !adresse.getStrasseHausnummer().isEmpty()) {
            personSb.append(", ").append(adresse.getStrHausnummer());
        }
        if (adresse != null) {
            personSb.append(", ").append(adresse.getPlz()).append(" ").append(adresse.getOrt());
        }
        if (festnetz != null && !festnetz.isEmpty()) {
            personSb.append(", ").append(festnetz);
        }
        if (natel != null && !natel.trim().isEmpty()) {
            personSb.append(", ").append(natel);
        }
        if (email != null && !email.trim().isEmpty()) {
            personSb.append(", ").append(email);
        }
        return personSb.toString();
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Anrede getAnrede() {
        return anrede;
    }

    public void setAnrede(Anrede anrede) {
        this.anrede = anrede;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public Calendar getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Calendar geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getFestnetz() {
        return festnetz;
    }

    public void setFestnetz(String festnetz) {
        this.festnetz = festnetz;
    }

    public String getNatel() {
        return natel;
    }

    public void setNatel(String natel) {
        this.natel = natel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    @Transient
    public String getEmailToBeDisplayedInWord() {

        if (!checkNotEmpty(email)) {
            return "";
        }

        // Maximal zugelassene Zeichen für Anzeige
        int maxLength = 38;

        // emailAdresse enthält möglicherweise mehrere, durch Komma getrennte Email-Adressen
        String[] emailAdressenSplitted = email.split("[,;][ \\t]*");

        // Erste Email wird immer angezeigt
        StringBuilder emailToBeDisplayed = new StringBuilder(emailAdressenSplitted[0]);
        int length = emailAdressenSplitted[0].length();

        // Weitere werden nur angehängt, solange dadurch die Maximallänge nicht überschritten wird
        for (int i = 1; i < emailAdressenSplitted.length - 1; i++) {
            if (length + 2 + emailAdressenSplitted[i].length() <= maxLength) {
                emailToBeDisplayed.append(", ").append(emailAdressenSplitted[i]);
                length += 2 + emailAdressenSplitted[i].length();
            }
        }

        return emailToBeDisplayed.toString();
    }
}
