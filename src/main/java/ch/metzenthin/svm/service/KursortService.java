package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.service.result.SaveKursortResult;
import java.util.List;

/**
 * @author Martin Schraner
 */
public interface KursortService {

  boolean doesKursortAlreadyExist(Integer kursortId, String kursortBezeichnung);

  List<Kursort> findAllKursorte();

  List<Kursort> findSelektierbareKursorte();

  SaveKursortResult saveKursort(Kursort kursort);

  DeleteKursortResult deleteKursort(Kursort kursort);
}
