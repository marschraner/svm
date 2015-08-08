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
@Table(name="SchuelerCode")
@DiscriminatorValue("Schueler")
public class SchuelerCode extends Code {

    @ManyToMany(mappedBy = "schuelerCodes")
    private Set<Schueler> schueler = new HashSet<>();

    public SchuelerCode() {
    }

    public SchuelerCode(String kuerzel, String beschreibung) {
        super(kuerzel, beschreibung);
    }

    public Set<Schueler> getSchueler() {
        return schueler;
    }

}
