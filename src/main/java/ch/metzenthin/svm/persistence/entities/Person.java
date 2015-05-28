package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.dataTypes.Anrede;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Person")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator")
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer personId;

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
    @Column(name = "geburtsdatum", nullable = true)
    private Calendar geburtsdatum;

    @Column(name = "natel", nullable = true)
    private String natel;

    @Column(name = "email", nullable = true)
    private String email;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "adresse_id", nullable = true)
    private Adresse adresse;

    public Person() {
    }

    public Person(Anrede anrede, String vorname, String nachname, Calendar geburtsdatum, String natel, String email) {
        this.anrede = anrede;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.natel = natel;
        this.email = email;
    }

    public boolean isIdenticalWith(Person otherPerson) {
        if (otherPerson == null) {
            return false;
        }
        return anrede.equals(otherPerson.getAnrede())
                && vorname.equals(otherPerson.getVorname())
                && nachname.equals(otherPerson.getNachname())
                && geburtsdatum.equals(otherPerson.getGeburtsdatum())
                && natel.equals(otherPerson.getNatel())
                && email.equals(otherPerson.getEmail())
                && adresse.isIdenticalWith(otherPerson.getAdresse());
    }

    public boolean isPartOf(Person otherPerson) {
        if (otherPerson == null) {
            return false;
        }
        return vorname.equals(otherPerson.getVorname())
                && nachname.equals(otherPerson.getNachname())
                && (geburtsdatum == null || geburtsdatum.equals(otherPerson.getGeburtsdatum()))
                && (natel == null || natel.trim().isEmpty() || natel.equals(otherPerson.getNatel()))
                && (email == null || email.trim().isEmpty() || email.equals(otherPerson.getEmail()))
                && (adresse == null || adresse.isPartOf(otherPerson.getAdresse()));
    }

    @Override
    public String toString() {
        StringBuilder personSb = new StringBuilder();
        if (anrede != Anrede.KEINE) {
            personSb.append(anrede + " ");
        }
        personSb.append(vorname);
        personSb.append(" " + nachname);
        if (geburtsdatum != null) {
            personSb.append(", " + String.format("%1$td.%1$tm.%1$tY", geburtsdatum));
        }
        if (adresse != null) {
            personSb.append(", " + adresse.toString());
        }
        if (natel != null) {
            personSb.append(", " + natel);
        }
        if (email != null) {
            personSb.append(", " + email);
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

    public void setNewAdresse(Adresse adresse) {
        adresse.getPersonen().add(this);
        this.adresse = adresse;
    }

    public void replaceAdresse(Adresse oldAdresse, Adresse newAdresse) {
        deleteAdresse(oldAdresse);
        setNewAdresse(newAdresse);
    }

    public void deleteAdresse(Adresse adresse) {
        adresse.getPersonen().remove(this);
    }
}
