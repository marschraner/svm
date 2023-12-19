package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.ui.componentmodel.MaerchensTableModel;

/**
 * @author Martin Schraner
 */
public interface MaerchenErfassenModel extends Model {

    String getSchuljahr();

    String getBezeichnung();

    Integer getAnzahlVorstellungen();

    Maerchen getMaerchen();

    void setSchuljahr(String schuljahr) throws SvmValidationException;

    void setBezeichnung(String maerchenbeginn) throws SvmValidationException;

    void setAnzahlVorstellungen(String anzahlVorstellungen) throws SvmValidationException;

    void setMaerchenOrigin(Maerchen maerchenOrigin);

    boolean checkMaerchenBereitsErfasst(SvmModel svmModel);

    String getNaechstesNochNichtErfasstesSchuljahrMaerchen(SvmModel svmModel);

    boolean checkIfMaerchenIsInPast();

    void speichern(SvmModel svmModel, MaerchensTableModel maerchensTableModel);
}
