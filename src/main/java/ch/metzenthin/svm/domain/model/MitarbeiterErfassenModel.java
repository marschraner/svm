package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

/**
 * @author Martin Schraner
 */
public interface MitarbeiterErfassenModel extends PersonModel {
    String getAhvNummer();
    String getVertretungsmoeglichkeiten();
    Boolean isAktiv();
    Mitarbeiter getMitarbeiter();

    void setAhvNummer(String ahvNummer) throws SvmValidationException;
    void setVertretungsmoeglichkeiten(String vertretungsmoeglichkeiten) throws SvmValidationException;
    void setAktiv(Boolean isSelected);
    void setMitarbeiterOrigin(Mitarbeiter mitarbeiterOrigin);

    boolean checkMitarbeiterBereitsErfasst(SvmModel svmModel);
    void speichern(SvmModel svmModel, MitarbeitersTableModel mitarbeitersTableModel);
}
