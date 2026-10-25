package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.service.result.DeleteCodeResult;
import ch.metzenthin.svm.service.result.SaveCodeResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public interface CodeService<T extends Code> {

  boolean doesKuerzelAlreadyExist(Integer codeId, String kuerzel);

  Optional<T> findCodeById(int codeId);

  List<T> findAllCodes();

  List<T> findAllSelectableCodes();

  SaveResultAndIdOfSavedEntity<SaveCodeResult> saveCode(T code);

  DeleteCodeResult deleteCode(T code);
}
