package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import java.util.List;

/**
 * @author Martin Schraner
 */
public interface KursortService {

  boolean checkIfAlreadyExists(Kursort kursort);

  List<Kursort> findAllKursorte();

  void saveKursort(Kursort kursort);

  DeleteKursortResult deleteKursort(Kursort kursort);
}
