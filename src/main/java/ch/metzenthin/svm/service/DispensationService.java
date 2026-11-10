package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.service.result.DeleteDispensationResult;
import ch.metzenthin.svm.service.result.SaveDispensationResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public interface DispensationService {

  boolean checkIfDispensationOverlapsOtherDispensationen(
      int schuelerId,
      Integer dispensationId,
      Calendar dispensationsbeginn,
      Calendar dispensationsende);

  Optional<Dispensation> findDispensationById(int dispensationId);

  List<Dispensation> findDispensationenBySchuelerId(int schuelerId);

  SaveResultAndIdOfSavedEntity<SaveDispensationResult> saveDispensation(Dispensation dispensation);

  DeleteDispensationResult deleteDispensation(Dispensation dispensation);
}
