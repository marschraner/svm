package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
import ch.metzenthin.svm.service.result.SaveKurstypResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public interface KurstypService {

  boolean doesKurstypAlreadyExist(Integer kurstypId, String kurstypBezeichnung);

  Optional<Kurstyp> findKurstypById(int kurstypId);

  List<Kurstyp> findAllKurstypen();

  List<Kurstyp> findSelektierbareKurstypen();

  SaveResultAndIdOfSavedEntity<SaveKurstypResult> saveKurstyp(Kurstyp kurstyp);

  DeleteKurstypResult deleteKurstyp(Kurstyp kurstyp);
}
