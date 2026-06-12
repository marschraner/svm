package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.List;

/**
 * @author Martin Schraner
 */
public interface KursLehrkraftService {

  List<Mitarbeiter> findSortedLehrkraefteByKursId(int kursId);
}
