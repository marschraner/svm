package ch.metzenthin.svm.model.entities;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Elternrolle;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="Angehoeriger")
@DiscriminatorValue("Angehoeriger")
public class Angehoeriger extends Person {

    @Enumerated(EnumType.STRING)
    @Column(name = "elternrolle", nullable = false)
    private Elternrolle elternrolle;

    @Column(name = "rechnungsempfaenger", nullable = false)
    private boolean rechnungsempfaenger;

    @Column(name = "beruf", nullable = true)
    private String beruf;

    public Angehoeriger() {
    }

    public Angehoeriger(Anrede anrede, String vorname, String nachname, Calendar geburtsdatum, String natel, String email, Elternrolle elternrolle, boolean rechnungsempfaenger, String beruf) {
        super(anrede, vorname, nachname, geburtsdatum, natel, email);
        this.elternrolle = elternrolle;
        this.rechnungsempfaenger = rechnungsempfaenger;
        this.beruf = beruf;
    }

    public Elternrolle getElternrolle() {
        return elternrolle;
    }

    public void setElternrolle(Elternrolle elternrolle) {
        this.elternrolle = elternrolle;
    }

    public boolean isRechnungsempfaenger() {
        return rechnungsempfaenger;
    }

    public void setRechnungsempfaenger(boolean rechnungsempfaenger) {
        this.rechnungsempfaenger = rechnungsempfaenger;
    }

    public String getBeruf() {
        return beruf;
    }

    public void setBeruf(String beruf) {
        this.beruf = beruf;
    }
}
