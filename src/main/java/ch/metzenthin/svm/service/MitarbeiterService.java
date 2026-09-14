package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.result.SaveMitarbeiterResult;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface MitarbeiterService {

  List<Mitarbeiter> findAktiveLehrkraefte();

  SaveMitarbeiterResult saveMitarbeiter(
      Mitarbeiter mitarbeiter,
      Optional<Adresse> adresseOptional,
      Set<MitarbeiterCode> mitabeiterCodes);
}
