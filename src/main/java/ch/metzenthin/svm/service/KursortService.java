package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Kursort;
import java.util.List;

/**
 * @author Martin Schraner
 */
public interface KursortService {

  List<Kursort> findAllKursorte();

  void saveKursort(Kursort kursort) throws EntityAlreadyExistsException;

  void deleteKursort(Kursort kursort) throws EntityStillReferencedException;
}
