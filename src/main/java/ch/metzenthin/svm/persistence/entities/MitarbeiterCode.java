package ch.metzenthin.svm.persistence.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name="MitarbeiterCode")
@DiscriminatorValue("Mitarbeiter")
public class MitarbeiterCode extends Code {

    @ManyToMany(mappedBy = "mitarbeiterCodes")
    private Set<Mitarbeiter> mitarbeiters = new HashSet<>();

    public MitarbeiterCode() {
    }

    public MitarbeiterCode(String kuerzel, String beschreibung, Boolean selektierbar) {
        super(kuerzel, beschreibung, selektierbar);
    }

    public Set<Mitarbeiter> getMitarbeiters() {
        return mitarbeiters;
    }

}
