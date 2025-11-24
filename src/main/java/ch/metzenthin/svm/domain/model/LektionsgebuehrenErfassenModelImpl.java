package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.service.LektionsgebuehrenService;
import ch.metzenthin.svm.service.result.SaveLektionsgebuehrenResult;
import jakarta.persistence.OptimisticLockException;
import java.math.BigDecimal;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class LektionsgebuehrenErfassenModelImpl extends AbstractModel
    implements LektionsgebuehrenErfassenModel {

  private static final Logger LOGGER =
      LogManager.getLogger(LektionsgebuehrenErfassenModelImpl.class);
  private static final BigDecimal MIN_VALID_VALUE = new BigDecimal("0.00");
  private static final BigDecimal MAX_VALID_VALUE = new BigDecimal("999.95");

  private final Lektionsgebuehren lektionsgebuehren;
  private final LektionsgebuehrenService lektionsgebuehrenService;

  public LektionsgebuehrenErfassenModelImpl(
      Optional<Lektionsgebuehren> lektionsgebuehrenToBeModifiedOptional,
      LektionsgebuehrenService lektionsgebuehrenService) {
    this.lektionsgebuehren =
        lektionsgebuehrenToBeModifiedOptional.orElseGet(Lektionsgebuehren::new);
    this.lektionsgebuehrenService = lektionsgebuehrenService;
  }

  private final IntegerModelAttribute lektionslaengeModelAttribute =
      new IntegerModelAttribute(
          this,
          Field.LEKTIONSLAENGE,
          10,
          200,
          new AttributeAccessor<>() {
            @Override
            public Integer getValue() {
              return lektionsgebuehren.getLektionslaenge();
            }

            @Override
            public void setValue(Integer value) {
              lektionsgebuehren.setLektionslaenge(value);
            }
          });

  @Override
  public Integer getLektionslaenge() {
    return lektionslaengeModelAttribute.getValue();
  }

  @Override
  public void setLektionslaenge(String lektionslaenge) throws SvmValidationException {
    setLektionslaenge(lektionslaenge, false);
  }

  private void setLektionslaenge(String lektionslaenge, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    lektionslaengeModelAttribute.setNewValue(
        true, lektionslaenge, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  private final PreisModelAttribute betrag1KindModelAttribute =
      new PreisModelAttribute(
          this,
          Field.BETRAG_1_KIND,
          MIN_VALID_VALUE,
          MAX_VALID_VALUE,
          new AttributeAccessor<>() {
            @Override
            public BigDecimal getValue() {
              return lektionsgebuehren.getBetrag1Kind();
            }

            @Override
            public void setValue(BigDecimal value) {
              lektionsgebuehren.setBetrag1Kind(value);
            }
          });

  @Override
  public BigDecimal getBetrag1Kind() {
    return betrag1KindModelAttribute.getValue();
  }

  @Override
  public void setBetrag1Kind(String betrag1Kind) throws SvmValidationException {
    setBetrag1Kind(betrag1Kind, false);
  }

  private void setBetrag1Kind(String betrag1Kind, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    betrag1KindModelAttribute.setNewValue(
        true, betrag1Kind, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  private final PreisModelAttribute betrag2KinderModelAttribute =
      new PreisModelAttribute(
          this,
          Field.BETRAG_2_KINDER,
          MIN_VALID_VALUE,
          MAX_VALID_VALUE,
          new AttributeAccessor<>() {
            @Override
            public BigDecimal getValue() {
              return lektionsgebuehren.getBetrag2Kinder();
            }

            @Override
            public void setValue(BigDecimal value) {
              lektionsgebuehren.setBetrag2Kinder(value);
            }
          });

  @Override
  public BigDecimal getBetrag2Kinder() {
    return betrag2KinderModelAttribute.getValue();
  }

  @Override
  public void setBetrag2Kinder(String betrag2Kinder) throws SvmValidationException {
    setBetrag2Kinder(betrag2Kinder, false);
  }

  private void setBetrag2Kinder(String betrag2Kinder, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    betrag2KinderModelAttribute.setNewValue(
        true, betrag2Kinder, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  private final PreisModelAttribute betrag3KinderModelAttribute =
      new PreisModelAttribute(
          this,
          Field.BETRAG_3_KINDER,
          MIN_VALID_VALUE,
          MAX_VALID_VALUE,
          new AttributeAccessor<>() {
            @Override
            public BigDecimal getValue() {
              return lektionsgebuehren.getBetrag3Kinder();
            }

            @Override
            public void setValue(BigDecimal value) {
              lektionsgebuehren.setBetrag3Kinder(value);
            }
          });

  @Override
  public BigDecimal getBetrag3Kinder() {
    return betrag3KinderModelAttribute.getValue();
  }

  @Override
  public void setBetrag3Kinder(String betrag3Kinder) throws SvmValidationException {
    setBetrag3Kinder(betrag3Kinder, false);
  }

  private void setBetrag3Kinder(String betrag3Kinder, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    betrag3KinderModelAttribute.setNewValue(
        true, betrag3Kinder, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  private final PreisModelAttribute betrag4KinderModelAttribute =
      new PreisModelAttribute(
          this,
          Field.BETRAG_4_KINDER,
          MIN_VALID_VALUE,
          MAX_VALID_VALUE,
          new AttributeAccessor<>() {
            @Override
            public BigDecimal getValue() {
              return lektionsgebuehren.getBetrag4Kinder();
            }

            @Override
            public void setValue(BigDecimal value) {
              lektionsgebuehren.setBetrag4Kinder(value);
            }
          });

  @Override
  public BigDecimal getBetrag4Kinder() {
    return betrag4KinderModelAttribute.getValue();
  }

  @Override
  public void setBetrag4Kinder(String betrag4Kinder) throws SvmValidationException {
    setBetrag4Kinder(betrag4Kinder, false);
  }

  private void setBetrag4Kinder(String betrag4Kinder, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    betrag4KinderModelAttribute.setNewValue(
        true, betrag4Kinder, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  private final PreisModelAttribute betrag5KinderModelAttribute =
      new PreisModelAttribute(
          this,
          Field.BETRAG_5_KINDER,
          MIN_VALID_VALUE,
          MAX_VALID_VALUE,
          new AttributeAccessor<>() {
            @Override
            public BigDecimal getValue() {
              return lektionsgebuehren.getBetrag5Kinder();
            }

            @Override
            public void setValue(BigDecimal value) {
              lektionsgebuehren.setBetrag5Kinder(value);
            }
          });

  @Override
  public BigDecimal getBetrag5Kinder() {
    return betrag5KinderModelAttribute.getValue();
  }

  @Override
  public void setBetrag5Kinder(String betrag5Kinder) throws SvmValidationException {
    setBetrag5Kinder(betrag5Kinder, false);
  }

  private void setBetrag5Kinder(String betrag5Kinder, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    betrag5KinderModelAttribute.setNewValue(
        true, betrag5Kinder, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  private final PreisModelAttribute betrag6KinderModelAttribute =
      new PreisModelAttribute(
          this,
          Field.BETRAG_6_KINDER,
          MIN_VALID_VALUE,
          MAX_VALID_VALUE,
          new AttributeAccessor<>() {
            @Override
            public BigDecimal getValue() {
              return lektionsgebuehren.getBetrag6Kinder();
            }

            @Override
            public void setValue(BigDecimal value) {
              lektionsgebuehren.setBetrag6Kinder(value);
            }
          });

  @Override
  public BigDecimal getBetrag6Kinder() {
    return betrag6KinderModelAttribute.getValue();
  }

  @Override
  public void setBetrag6Kinder(String betrag6Kinder) throws SvmValidationException {
    setBetrag6Kinder(betrag6Kinder, false);
  }

  private void setBetrag6Kinder(String betrag6Kinder, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    betrag6KinderModelAttribute.setNewValue(
        true, betrag6Kinder, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  @Override
  public SaveLektionsgebuehrenResult speichern() {
    SaveLektionsgebuehrenResult saveLektionsgebuehrenResult;
    try {
      lektionsgebuehrenService.saveLektionsgebuehren(lektionsgebuehren);
      saveLektionsgebuehrenResult = SaveLektionsgebuehrenResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveLektionsgebuehrenResult = SaveLektionsgebuehrenResult.LEKTIONSGEBUEHREN_BEREITS_ERFASST;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveLektionsgebuehrenResult =
          SaveLektionsgebuehrenResult.LEKTIONSGEBUEHREN_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveLektionsgebuehrenResult;
  }

  @Override
  public void initializeCompleted() {
    if (lektionsgebuehren.getId() != null) {
      setBulkUpdate(true);
      try {
        setLektionslaenge(Integer.toString(lektionsgebuehren.getLektionslaenge()), true);
        setBetrag1Kind(lektionsgebuehren.getBetrag1Kind().toString(), true);
        setBetrag2Kinder(lektionsgebuehren.getBetrag2Kinder().toString(), true);
        setBetrag3Kinder(lektionsgebuehren.getBetrag3Kinder().toString(), true);
        setBetrag4Kinder(lektionsgebuehren.getBetrag4Kinder().toString(), true);
        setBetrag5Kinder(lektionsgebuehren.getBetrag5Kinder().toString(), true);
        setBetrag6Kinder(lektionsgebuehren.getBetrag6Kinder().toString(), true);
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
