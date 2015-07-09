package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface DispensationErfassenModel extends Model {
    Calendar getDispensationsbeginn();
    Calendar getDispensationsende();
    String getVoraussichtlicheDauer();
    String getGrund();

    void setDispensationsbeginn(String text) throws SvmValidationException;
    void setDispensationsende(String text) throws SvmValidationException;
    void setVoraussichtlicheDauer(String text) throws SvmValidationException;
    void setGrund(String text) throws SvmValidationException;
}
