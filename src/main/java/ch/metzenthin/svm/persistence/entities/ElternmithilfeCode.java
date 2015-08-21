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
@Table(name="ElternmithilfeCode")
@DiscriminatorValue("Elternmithilfe")
public class ElternmithilfeCode extends Code {

    @OneToMany(mappedBy = "elternmithilfeCode")
    private Set<Maercheneinteilung> maercheneinteilungen = new HashSet<>();

    public ElternmithilfeCode() {
    }

    public ElternmithilfeCode(String kuerzel, String beschreibung, Boolean selektierbar) {
        super(kuerzel, beschreibung, selektierbar);
    }

    public Set<Maercheneinteilung> getMaercheneinteilungen() {
        return maercheneinteilungen;
    }
}
