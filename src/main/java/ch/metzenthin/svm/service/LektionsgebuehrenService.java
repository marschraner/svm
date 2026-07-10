package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.service.result.DeleteLektionsgebuehrenResult;
import ch.metzenthin.svm.service.result.SaveLektionsgebuehrenResult;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface LektionsgebuehrenService {

  List<Lektionsgebuehren> findAllLektionsgebuehren();

  SaveLektionsgebuehrenResult saveLektionsgebuehren(Lektionsgebuehren lektionsgebuehren);

  DeleteLektionsgebuehrenResult deleteLektionsgebuehren(Lektionsgebuehren lektionsgebuehren);
}
