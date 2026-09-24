package ch.metzenthin.svm.persistence.repository.custom;

import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.List;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface MitarbeiterRepositoryCustom {

  List<Mitarbeiter> findMitarbeitersWithoutMitarbeiterCode(
      Optional<String> nachnameOptional,
      Optional<String> vornameOptional,
      Optional<Boolean> lehrkraftOptional,
      Optional<Boolean> aktivOptional,
      Optional<List<Integer>> mitarbeiterIdsOptional);

  List<MitarbeiterAndMitarbeiterCode>
      findMitarbeiterAndMitarbeiterCodesOfMitarbeitersWithMitarbeiterCode(
          Optional<String> nachnameOptional,
          Optional<String> vornameOptional,
          Optional<Boolean> lehrkraftOptional,
          Optional<Boolean> aktivOptional,
          Optional<List<Integer>> mitarbeiterIdsOptional);
}
