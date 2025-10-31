package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.service.result.SaveKursortResult;

/**
 * @author Martin Schraner
 */
public interface KursortErfassenModel extends Model {

  String getBezeichnung();

  Boolean isSelektierbar();

  void setBezeichnung(String bezeichnung) throws SvmValidationException;

  void setSelektierbar(Boolean isSelected);

  SaveKursortResult speichern();
}
