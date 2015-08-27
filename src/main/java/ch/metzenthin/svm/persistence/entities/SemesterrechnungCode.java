package ch.metzenthin.svm.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="SemesterrechnungCode")
@DiscriminatorValue("Semesterrechnung")
public class SemesterrechnungCode extends Code {

    public SemesterrechnungCode() {
    }

    public SemesterrechnungCode(String kuerzel, String beschreibung, Boolean selektierbar) {
        super(kuerzel, beschreibung, selektierbar);
    }

}
