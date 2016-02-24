package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public interface RechnungsdatumErfassenModel extends Model {

    Calendar getRechnungsdatum();

    void setRechnungsdatum(String rechnungdatum) throws SvmValidationException;

    void replaceRechnungsdatumAndUpdateSemesterrechnung(List<Semesterrechnung> semesterrechnungen, Rechnungstyp rechnungstyp);
}
