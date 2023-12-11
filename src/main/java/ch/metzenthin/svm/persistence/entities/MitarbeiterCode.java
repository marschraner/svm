package ch.metzenthin.svm.persistence.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@Entity
@Table(name = "MitarbeiterCode")
@DiscriminatorValue("Mitarbeiter")
public class MitarbeiterCode extends Code {

    @ManyToMany(mappedBy = "mitarbeiterCodes")
    private final Set<Mitarbeiter> mitarbeiters = new HashSet<>();

    public MitarbeiterCode() {
    }

    public MitarbeiterCode(String kuerzel, String beschreibung, Boolean selektierbar) {
        super(kuerzel, beschreibung, selektierbar);
    }

    public Set<Mitarbeiter> getMitarbeiters() {
        return mitarbeiters;
    }

}
