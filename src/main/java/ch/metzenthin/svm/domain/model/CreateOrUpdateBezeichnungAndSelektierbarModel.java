package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.service.result.SaveDialogResult;

/**
 * @param <T> Result-Typ, z.B. SaveKursortResult
 * @author Hans Stamm
 */
public interface CreateOrUpdateBezeichnungAndSelektierbarModel<T extends SaveDialogResult>
    extends CreateOrUpdateModel<T> {

  String getBezeichnung();

  Boolean isSelektierbar();

  void setBezeichnung(String bezeichnung) throws SvmValidationException;

  void setSelektierbar(Boolean isSelected);
}
