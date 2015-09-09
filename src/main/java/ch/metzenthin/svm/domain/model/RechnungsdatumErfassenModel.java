package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface RechnungsdatumErfassenModel extends Model {

    Calendar getRechnungsdatum();

    void setRechnungsdatum(String rechnungdatum) throws SvmValidationException;

    void replaceRechnungsdatumAndUpdateSemesterrechnung(SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel, Rechnungstyp rechnungstyp);
}
