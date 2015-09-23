package ch.metzenthin.svm.persistence.entities;

import ch.metzenthin.svm.common.dataTypes.Anrede;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="ElternmithilfeDrittperson")
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
