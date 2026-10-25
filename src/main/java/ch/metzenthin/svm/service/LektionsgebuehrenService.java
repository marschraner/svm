package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.service.result.DeleteLektionsgebuehrenResult;
import ch.metzenthin.svm.service.result.SaveLektionsgebuehrenResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public interface LektionsgebuehrenService {

  Optional<Lektionsgebuehren> findLektionsgebuehrenById(int lektionsgebuehrenId);

  List<Lektionsgebuehren> findAllLektionsgebuehren();

  SaveResultAndIdOfSavedEntity<SaveLektionsgebuehrenResult> saveLektionsgebuehren(
      Lektionsgebuehren lektionsgebuehren);

  DeleteLektionsgebuehrenResult deleteLektionsgebuehren(Lektionsgebuehren lektionsgebuehren);
}
