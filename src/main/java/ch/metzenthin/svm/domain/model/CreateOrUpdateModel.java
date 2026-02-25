package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.service.result.SaveDialogResult;

/**
 * @author Hans Stamm
 */
public interface CreateOrUpdateModel<T extends SaveDialogResult> extends Model {

  T speichern();
}
