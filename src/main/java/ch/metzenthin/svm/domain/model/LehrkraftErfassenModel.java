package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.ui.componentmodel.LehrkraefteTableModel;

/**
 * @author Martin Schraner
 */
public interface LehrkraftErfassenModel extends PersonModel {
    String getAhvNummer();
    String getVertretungsmoeglichkeiten();
    Boolean isAktiv();
    Lehrkraft getLehrkraft();

    void setAhvNummer(String ahvNummer) throws SvmValidationException;
    void setVertretungsmoeglichkeiten(String vertretungsmoeglichkeiten) throws SvmValidationException;
    void setAktiv(Boolean isSelected);
    void setLehrkraftOrigin(Lehrkraft lehrkraftOrigin);

    boolean checkLehrkraftBereitsErfasst(SvmModel svmModel);
    void speichern(SvmModel svmModel, LehrkraefteTableModel lehrkraefteTableModel);
}
