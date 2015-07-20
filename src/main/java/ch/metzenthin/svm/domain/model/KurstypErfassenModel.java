package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kurstyp;

/**
 * @author Martin Schraner
 */
public interface KurstypErfassenModel extends Model {
    void setKurstypOrigin(Kurstyp kurstypOrigin);

    String getBezeichnung();
    Kurstyp getKurstyp();

    void setBezeichnung(String bezeichnung) throws SvmValidationException;

    boolean checkKurstypBezeichnungBereitsInVerwendung(SvmModel svmModel);
    void speichern(SvmModel svmModel);
}
