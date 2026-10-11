package ch.metzenthin.svm.service;

import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCodes;
import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.result.DeleteMitarbeiterResult;
import ch.metzenthin.svm.service.result.ExportListResult;
import ch.metzenthin.svm.service.result.SaveMitarbeiterResult;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface MitarbeiterService {

  List<Mitarbeiter> findAktiveLehrkraefte();

  List<MitarbeiterAndMitarbeiterCodes> findMitarbeiterAndMitarbeiterCodes(
      Optional<String> nachnameOptional,
      Optional<String> vornameOptional,
      Optional<Boolean> lehrkraftOptional,
      Optional<Boolean> aktivOptional,
      Optional<MitarbeiterCode> mitarbeiterCodeOptional);

  SaveMitarbeiterResult saveMitarbeiter(
      Mitarbeiter mitarbeiter,
      Optional<Adresse> adresseOptional,
      Set<MitarbeiterCode> mitabeiterCodes);

  DeleteMitarbeiterResult deleteMitarbeiter(Mitarbeiter mitarbeiterToBeDeleted);

  ExportListResult exportList(
      Listentyp listentyp,
      String listenTitel,
      File outputFile,
      Iterator<MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert> rowIterator);
}
