package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.service.result.DeleteCodeResult;
import ch.metzenthin.svm.service.result.SaveCodeResult;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface CodeService<T extends Code> {

  boolean doesKuerzelAlreadyExist(Integer codeId, String kuerzel);

  List<T> findAllCodes();

  SaveCodeResult saveCode(T code);

  DeleteCodeResult deleteCode(T code);
}
