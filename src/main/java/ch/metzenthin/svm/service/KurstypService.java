package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface KurstypService {

  boolean doesKurstypAlreadyExist(Integer kurstypId, String kurstypBezeichnung);

  List<Kurstyp> findAllKurstypen();

  List<Kurstyp> findSelektierbareKurstypen();

  void saveKurstyp(Kurstyp kurstyp) throws EntityAlreadyExistsException;

  void deleteKurstyp(Kurstyp kurstyp) throws EntityStillReferencedException;
}
