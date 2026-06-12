package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.KursortDeletedException;
import ch.metzenthin.svm.domain.KurstypDeletedException;
import ch.metzenthin.svm.domain.SemesterDeletedException;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.service.result.SaveKursResult;

/**
 * @author Martin Schraner
 */
public interface KursService {

  boolean existsKursByKursortId(int kursortId);

  boolean existsKursByKurstypId(int kurstypId);

  boolean existsKursByLektionslaenge(int lektionslaenge);

  boolean existsKursBySemesterId(int semesterId);

  SaveKursResult saveKurs(Kurs kurs, Mitarbeiter lehrkraft1, Mitarbeiter lehrkraft2)
      throws SemesterDeletedException, KurstypDeletedException, KursortDeletedException;

  void deleteKurs(Kurs kurs) throws EntityStillReferencedException;
}
