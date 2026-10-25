package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.service.result.SaveKursortResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.List;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public interface KursortService {

  boolean doesKursortAlreadyExist(Integer kursortId, String kursortBezeichnung);

  Optional<Kursort> findKursortById(int kursortId);

  List<Kursort> findAllKursorte();

  List<Kursort> findSelektierbareKursorte();

  SaveResultAndIdOfSavedEntity<SaveKursortResult> saveKursort(Kursort kursort);

  DeleteKursortResult deleteKursort(Kursort kursort);
}
