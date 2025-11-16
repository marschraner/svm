package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.service.CodeService;
import ch.metzenthin.svm.service.result.SaveCodeResult;
import jakarta.persistence.OptimisticLockException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public abstract class CreateOrUpdateCodeModelImpl<T extends Code> extends AbstractModel
    implements CreateOrUpdateCodeModel {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateCodeModelImpl.class);

  private final T code;
  private final CodeService<T> codeService;

  protected CreateOrUpdateCodeModelImpl(T code, CodeService<T> codeService) {
    this.code = code;
    this.codeService = codeService;
  }

  private final StringModelAttribute kuerzelModelAttribute =
      new StringModelAttribute(
          this,
          Field.KUERZEL,
          1,
          3,
          new AttributeAccessor<>() {
            @Override
            public String getValue() {
              return code.getKuerzel();
            }

            @Override
            public void setValue(String value) {
              code.setKuerzel(value);
            }
          });

  @Override
  public String getKuerzel() {
    return kuerzelModelAttribute.getValue();
  }

  @Override
  public void setKuerzel(String kuerzel) throws SvmValidationException {
    setKuerzel(kuerzel, false);
  }

  private void setKuerzel(String kuerzel, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    kuerzelModelAttribute.setNewValue(true, kuerzel, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  private final StringModelAttribute beschreibungModelAttribute =
      new StringModelAttribute(
          this,
          Field.BESCHREIBUNG,
          2,
          50,
          new AttributeAccessor<>() {
            @Override
            public String getValue() {
              return code.getBeschreibung();
            }

            @Override
            public void setValue(String value) {
              code.setBeschreibung(value);
            }
          });

  @Override
  public String getBeschreibung() {
    return beschreibungModelAttribute.getValue();
  }

  @Override
  public void setBeschreibung(String beschreibung) throws SvmValidationException {
    setBeschreibung(beschreibung, false);
  }

  private void setBeschreibung(String bezeichnung, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    beschreibungModelAttribute.setNewValue(
        true, bezeichnung, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  @Override
  public void setSelektierbar(Boolean isSelected) {
    setSelektierbar(isSelected, false);
  }

  private void setSelektierbar(Boolean isSelected, boolean enforcePropertyChangeEvent) {
    Boolean oldValue = (enforcePropertyChangeEvent) ? !isSelected : code.isSelektierbar();
    code.setSelektierbar(isSelected);
    firePropertyChange(Field.SELEKTIERBAR, oldValue, isSelected);
  }

  @Override
  public Boolean isSelektierbar() {
    return code.isSelektierbar();
  }

  @Override
  public SaveCodeResult speichern() {
    SaveCodeResult saveCodeResult;
    try {
      codeService.saveCode(code);
      saveCodeResult = SaveCodeResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveCodeResult = SaveCodeResult.CODE_BEREITS_ERFASST;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveCodeResult = SaveCodeResult.CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveCodeResult;
  }

  @Override
  public void initializeCompleted() {
    if (code.getCodeId() != null) {
      setBulkUpdate(true);
      try {
        setKuerzel(code.getKuerzel(), true);
        setBeschreibung(code.getBeschreibung(), true);
        setSelektierbar(code.isSelektierbar(), true);
      } catch (SvmValidationException e) {
        LOGGER.error(e.getMessage());
      }
      setBulkUpdate(false);
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
