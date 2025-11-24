package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.service.result.SaveCodeResult;

/**
 * @author Martin Schraner
 */
public interface CodeErfassenModel extends Model {

  String getKuerzel();

  String getBeschreibung();

  Boolean isSelektierbar();

  void setKuerzel(String kuerzel) throws SvmValidationException;

  void setBeschreibung(String beschreibung) throws SvmValidationException;

  void setSelektierbar(Boolean isSelected);

  SaveCodeResult speichern();
}
