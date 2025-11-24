package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Code;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface CodeService<T extends Code> {

  List<T> findAllCodes();

  void saveCode(T code) throws EntityAlreadyExistsException;

  void deleteCode(T code) throws EntityStillReferencedException;
}
