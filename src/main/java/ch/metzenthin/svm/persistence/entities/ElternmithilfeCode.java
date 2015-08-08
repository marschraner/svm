package ch.metzenthin.svm.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="ElternmithilfeCode")
@DiscriminatorValue("Elternmithilfe")
public class ElternmithilfeCode extends Code {

    public ElternmithilfeCode() {
    }

    public ElternmithilfeCode(String kuerzel, String beschreibung) {
        super(kuerzel, beschreibung);
    }

}
