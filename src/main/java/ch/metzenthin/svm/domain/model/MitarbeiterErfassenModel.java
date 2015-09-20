package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

import java.util.List;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public interface MitarbeiterErfassenModel extends PersonModel {
    String getAhvNummer();
    Boolean isLehrkraft();
    String getVertretungsmoeglichkeiten();
    String getBemerkungen();
    Boolean isAktiv();
    Mitarbeiter getMitarbeiter();

    void setMitarbeiterOrigin(Mitarbeiter mitarbeiterOrigin);
    void setAhvNummer(String ahvNummer) throws SvmValidationException;
    void setLehrkraft(Boolean isSelected);
    void setVertretungsmoeglichkeiten(String vertretungsmoeglichkeiten) throws SvmValidationException;
    void setBemerkungen(String bemerkungen) throws SvmValidationException;
    void setAktiv(Boolean isSelected);

    Set<MitarbeiterCode> getMitarbeiterCodes();
    List<MitarbeiterCode> getMitarbeiterCodesAsList();
    CodesTableData getCodesTableData();
    String getCodesAsStr();
    String getCodesBearbeitenTitle();
    boolean checkMitarbeiterBereitsErfasst(SvmModel svmModel);
    void speichern(SvmModel svmModel, MitarbeitersTableModel mitarbeitersTableModel);
}
