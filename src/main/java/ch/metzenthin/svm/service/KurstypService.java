package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface KurstypService {

  List<Kurstyp> findAllKurstypen();

  void saveKurstyp(Kurstyp kurstyp) throws EntityAlreadyExistsException;

  DeleteKurstypResult deleteKurstyp(Kurstyp kurstyp);
}
