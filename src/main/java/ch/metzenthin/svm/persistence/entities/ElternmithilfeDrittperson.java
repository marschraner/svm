package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.datatypes.Anrede;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "ElternmithilfeDrittperson")
@DiscriminatorValue("ElternmithilfeDrittperson")
public class ElternmithilfeDrittperson extends Person {

    public ElternmithilfeDrittperson() {
    }

    public ElternmithilfeDrittperson(Anrede anrede, String vorname, String nachname, String festnetz, String natel, String email) {
        super(anrede, vorname, nachname, null, festnetz, natel, email);
    }

    public boolean isIdenticalWith(ElternmithilfeDrittperson otherElternmithilfeDrittperson) {
        return (super.isIdenticalWith(otherElternmithilfeDrittperson));
    }

}
