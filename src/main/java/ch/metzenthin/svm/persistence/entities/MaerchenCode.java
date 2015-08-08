package ch.metzenthin.svm.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="MaerchenCode")
@DiscriminatorValue("Maerchen")
public class MaerchenCode extends Code {

    public MaerchenCode() {
    }

    public MaerchenCode(String kuerzel, String beschreibung) {
        super(kuerzel, beschreibung);
    }

}
