package ch.metzenthin.svm.service.impl;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.checkIfTwoPeriodsOverlap;

import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.repository.DispensationRepository;
import ch.metzenthin.svm.persistence.repository.SchuelerRepository;
import ch.metzenthin.svm.service.DispensationService;
import ch.metzenthin.svm.service.result.DeleteDispensationResult;
import ch.metzenthin.svm.service.result.SaveDispensationResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hans Stamm
 */
@Service
public class DispensationServiceImpl implements DispensationService {

  private final DispensationRepository dispensationRepository;
  private final SchuelerRepository schuelerRepository;

  public DispensationServiceImpl(
      DispensationRepository dispensationRepository, SchuelerRepository schuelerRepository) {
    this.dispensationRepository = dispensationRepository;
    this.schuelerRepository = schuelerRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkIfDispensationOverlapsOtherDispensationen(
      int schuelerId,
      Integer dispensationId,
      Calendar dispensationsbeginn,
      Calendar dispensationsende) {

    return doCheckIfDispensationOverlapsOtherDispensationen(
        schuelerId, dispensationId, dispensationsbeginn, dispensationsende);
  }

  private boolean doCheckIfDispensationOverlapsOtherDispensationen(
      int schuelerId,
      Integer dispensationId,
      Calendar dispensationsbeginn,
      Calendar dispensationsende) {

    List<Dispensation> dispensationen =
        dispensationRepository.findBySchuelerIdOrderByDispensationsbeginnDesc(schuelerId);
    return dispensationen.stream()
        .anyMatch(
            dispensation ->
                (dispensationId == null
                    || !dispensationId.equals(dispensation.getDispensationId())
                        && checkIfTwoPeriodsOverlap(
                            dispensation.getDispensationsbeginn(),
                            dispensation.getDispensationsende(),
                            dispensationsbeginn,
                            dispensationsende)));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Dispensation> findDispensationById(int dispensationId) {
    return dispensationRepository.findById(dispensationId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Dispensation> findDispensationenBySchuelerId(int schuelerId) {
    return dispensationRepository.findBySchuelerIdOrderByDispensationsbeginnDesc(schuelerId);
  }

  @Override
  @Transactional
  public SaveResultAndIdOfSavedEntity<SaveDispensationResult> saveDispensation(
      Dispensation dispensation) {

    Optional<Schueler> schuelerOptional =
        schuelerRepository.findById(dispensation.getSchueler().getPersonId());
    if (schuelerOptional.isEmpty()) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveDispensationResult.SCHUELER_DURCH_ANDEREN_BENUTZER_GELOESCHT, -1);
    }

    if (doCheckIfDispensationOverlapsOtherDispensationen(
        schuelerOptional.get().getPersonId(),
        dispensation.getDispensationId(),
        dispensation.getDispensationsbeginn(),
        dispensation.getDispensationsende())) {
      return new SaveResultAndIdOfSavedEntity<>(
          SaveDispensationResult.DISPENSATION_UEBERLAPPT_MIT_ANDERER_DISPENSATION, -1);
    }

    dispensation.setSchueler(schuelerOptional.get());
    dispensation = dispensationRepository.save(dispensation);

    return new SaveResultAndIdOfSavedEntity<>(
        SaveDispensationResult.SPEICHERN_ERFOLGREICH, dispensation.getDispensationId());
  }

  @Override
  @Transactional
  public DeleteDispensationResult deleteDispensation(Dispensation dispensation) {
    dispensationRepository.delete(dispensation);
    return DeleteDispensationResult.LOESCHEN_ERFOLGREICH;
  }
}
