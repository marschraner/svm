package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.ui.componentmodel.DispensationenTableModel;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface DispensationErfassenModel extends Model {
    void setDispensationOrigin(Dispensation dispensationOrigin);

    Calendar getDispensationsbeginn();
    Calendar getDispensationsende();
    String getVoraussichtlicheDauer();
    String getGrund();
    Dispensation getDispensation();

    void setDispensationsbeginn(String text) throws SvmValidationException;
    void setDispensationsende(String text) throws SvmValidationException;
    void setVoraussichtlicheDauer(String text) throws SvmValidationException;
    void setGrund(String text) throws SvmValidationException;

    boolean checkDispensationUeberlapptAndereDispensationen(SchuelerDatenblattModel schuelerDatenblattModel);
    void speichern(DispensationenTableModel dispensationenTableModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
