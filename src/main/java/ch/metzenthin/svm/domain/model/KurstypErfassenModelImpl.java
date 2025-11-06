package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.service.KurstypService;
import ch.metzenthin.svm.service.result.SaveKurstypResult;
import jakarta.persistence.OptimisticLockException;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class KurstypErfassenModelImpl extends AbstractModel implements KurstypErfassenModel {

  private static final Logger LOGGER = LogManager.getLogger(KurstypErfassenModelImpl.class);

  private final Kurstyp kurstyp;
  private final KurstypService kurstypService;

  public KurstypErfassenModelImpl(
      Optional<Kurstyp> kurstypToBeModifiedOptional, KurstypService kurstypService) {
    this.kurstyp = kurstypToBeModifiedOptional.orElseGet(Kurstyp::new);
    this.kurstypService = kurstypService;
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
              return kurstyp.getBezeichnung();
            }

            @Override
            public void setValue(String value) {
              kurstyp.setBezeichnung(value);
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
    Boolean oldValue = (enforcePropertyChangeEvent) ? !isSelected : kurstyp.isSelektierbar();
    kurstyp.setSelektierbar(isSelected);
    firePropertyChange(Field.SELEKTIERBAR, oldValue, isSelected);
  }

  @Override
  public Boolean isSelektierbar() {
    return kurstyp.isSelektierbar();
  }

  @Override
  public SaveKurstypResult speichern() {
    SaveKurstypResult saveKurstypResult;
    try {
      kurstypService.saveKurstyp(kurstyp);
      saveKurstypResult = SaveKurstypResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveKurstypResult = SaveKurstypResult.KURSTYP_BEREITS_ERFASST;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveKurstypResult = SaveKurstypResult.KURSTYP_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveKurstypResult;
  }

  @SuppressWarnings("DuplicatedCode")
  @Override
  public void initializeCompleted() {
    if (kurstyp.getKurstypId() != null) {
      setBulkUpdate(true);
      try {
        setBezeichnung(kurstyp.getBezeichnung(), true);
        setSelektierbar(kurstyp.isSelektierbar(), true);
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
