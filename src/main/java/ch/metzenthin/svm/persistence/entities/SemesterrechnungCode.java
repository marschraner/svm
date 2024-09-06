package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("java:S2160")  // equals / hash definiert für Code
@Entity
@Table(name = "SemesterrechnungCode")
@DiscriminatorValue("Semesterrechnung")
public class SemesterrechnungCode extends Code {

    @OneToMany(mappedBy = "semesterrechnungCode")
    private final Set<Semesterrechnung> semesterrechnungen = new HashSet<>();

    public SemesterrechnungCode() {
    }

    public SemesterrechnungCode(String kuerzel, String beschreibung, Boolean selektierbar) {
        super(kuerzel, beschreibung, selektierbar);
    }

    public Set<Semesterrechnung> getSemesterrechnungen() {
        return semesterrechnungen;
    }
}
