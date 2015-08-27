package ch.metzenthin.svm.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="SemesterrechnungCode")
@DiscriminatorValue("Semesterrechnung")
public class SemesterrechnungCode extends Code {

    @OneToMany(mappedBy = "semesterrechnungCode")
    private Set<Semesterrechnung> semesterrechnungen = new HashSet<>();

    public SemesterrechnungCode() {
    }

    public SemesterrechnungCode(String kuerzel, String beschreibung, Boolean selektierbar) {
        super(kuerzel, beschreibung, selektierbar);
    }

    public Set<Semesterrechnung> getSemesterrechnungen() {
        return semesterrechnungen;
    }
}
