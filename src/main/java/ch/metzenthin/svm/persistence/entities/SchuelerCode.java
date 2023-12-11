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
@Table(name = "SchuelerCode")
@DiscriminatorValue("Schueler")
public class SchuelerCode extends Code {

    @ManyToMany(mappedBy = "schuelerCodes")
    private final Set<Schueler> schueler = new HashSet<>();

    public SchuelerCode() {
    }

    public SchuelerCode(String kuerzel, String beschreibung, Boolean selektierbar) {
        super(kuerzel, beschreibung, selektierbar);
    }

    public Set<Schueler> getSchueler() {
        return schueler;
    }

}
