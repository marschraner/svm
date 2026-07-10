package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
import ch.metzenthin.svm.service.result.SaveKurstypResult;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface KurstypService {

  boolean doesKurstypAlreadyExist(Integer kurstypId, String kurstypBezeichnung);

  List<Kurstyp> findAllKurstypen();

  List<Kurstyp> findSelektierbareKurstypen();

  SaveKurstypResult saveKurstyp(Kurstyp kurstyp);

  DeleteKurstypResult deleteKurstyp(Kurstyp kurstyp);
}
