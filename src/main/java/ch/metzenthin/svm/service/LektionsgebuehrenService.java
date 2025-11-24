package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface LektionsgebuehrenService {

  List<Lektionsgebuehren> findAllLektionsgebuehren();

  void saveLektionsgebuehren(Lektionsgebuehren lektionsgebuehren)
      throws EntityAlreadyExistsException;

  void deleteLektionsgebuehren(Lektionsgebuehren lektionsgebuehren)
      throws EntityStillReferencedException;
}
