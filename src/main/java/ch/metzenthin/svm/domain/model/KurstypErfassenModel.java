package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.service.result.SaveKurstypResult;

/**
 * @author Martin Schraner
 */
public interface KurstypErfassenModel extends Model {

  String getBezeichnung();

  Boolean isSelektierbar();

  void setBezeichnung(String bezeichnung) throws SvmValidationException;

  void setSelektierbar(Boolean isSelected);

  SaveKurstypResult speichern();
}
