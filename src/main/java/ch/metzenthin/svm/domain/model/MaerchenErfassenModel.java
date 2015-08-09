package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Maerchen;

/**
 * @author Martin Schraner
 */
public interface MaerchenErfassenModel extends Model {
    String getSchuljahr();
    String getBezeichnung();
    Maerchen getMaerchen();

    void setSchuljahr(String schuljahr) throws SvmValidationException;
    void setBezeichnung(String maerchenbeginn) throws SvmValidationException;
    void setMaerchenOrigin(Maerchen maerchenOrigin);

    boolean checkMaerchenBereitsErfasst(SvmModel svmModel);
    String getNaechstesNochNichtErfasstesSchuljahrMaerchen(SvmModel svmModel);
    void speichern(SvmModel svmModel);
}
