package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface RechnungsdatumErfassenModel extends Model {

  Calendar getRechnungsdatum();

  void setRechnungsdatum(String rechnungsdatum) throws SvmValidationException;

  void replaceRechnungsdatumAndUpdateSemesterrechnung(
      SemesterrechnungenTableModel semesterrechnungenTableModel, Rechnungstyp rechnungstyp);
}
