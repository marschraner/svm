package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface MitarbeiterService {

  List<Mitarbeiter> findAktiveLehrkraefte();
}
