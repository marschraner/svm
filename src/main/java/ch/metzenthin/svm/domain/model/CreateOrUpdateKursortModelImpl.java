package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.result.SaveKursortResult;
import jakarta.persistence.OptimisticLockException;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CreateOrUpdateKursortModelImpl extends AbstractModel
    implements CreateOrUpdateKursortModel {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateKursortModelImpl.class);

  private final Kursort kursort;
  private final KursortService kursortService;

  public CreateOrUpdateKursortModelImpl(
      Optional<Kursort> kursortToBeModifiedOptional, KursortService kursortService) {
    this.kursort = kursortToBeModifiedOptional.orElseGet(Kursort::new);
    this.kursortService = kursortService;
  }

  private final StringModelAttribute bezeichnungModelAttribute =
      new StringModelAttribute(
          this,
          Field.BEZEICHNUNG,
          2,
          50,
          new AttributeAccessor<>() {
            @Override
            public String getValue() {
              return kursort.getBezeichnung();
            }

            @Override
            public void setValue(String value) {
              kursort.setBezeichnung(value);
            }
          });

  @Override
  public String getBezeichnung() {
    return bezeichnungModelAttribute.getValue();
  }

  @Override
  public void setBezeichnung(String bezeichnung) throws SvmValidationException {
    setBezeichnung(bezeichnung, false);
  }

  private void setBezeichnung(String bezeichnung, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    bezeichnungModelAttribute.setNewValue(
        true, bezeichnung, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  @Override
  public void setSelektierbar(Boolean isSelected) {
    setSelektierbar(isSelected, false);
  }

  private void setSelektierbar(Boolean isSelected, boolean enforcePropertyChangeEvent) {
    Boolean oldValue = (enforcePropertyChangeEvent) ? !isSelected : kursort.isSelektierbar();
    kursort.setSelektierbar(isSelected);
    firePropertyChange(Field.SELEKTIERBAR, oldValue, isSelected);
  }

  @Override
  public Boolean isSelektierbar() {
    return kursort.isSelektierbar();
  }

  @Override
  public SaveKursortResult speichern() {
    SaveKursortResult saveKursortResult;
    try {
      kursortService.saveKursort(kursort);
      saveKursortResult = SaveKursortResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveKursortResult = SaveKursortResult.KURSORT_BEREITS_ERFASST;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveKursortResult = SaveKursortResult.KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveKursortResult;
  }

  @SuppressWarnings("DuplicatedCode")
  @Override
  public void initializeCompleted() {
    if (kursort.getKursortId() != null) {
      setBulkUpdate(true);
      try {
        setBezeichnung(kursort.getBezeichnung(), true);
        setSelektierbar(kursort.isSelektierbar(), true);
      } catch (SvmValidationException e) {
        LOGGER.error(e.getMessage());
      }
      setBulkUpdate(false);
    } else {
      super.initializeCompleted();
    }
  }

  @Override
  public boolean isCompleted() {
    return true;
  }

  @Override
  void doValidate() throws SvmValidationException {
    // Keine feldübergreifende Validierung notwendig
  }
}
