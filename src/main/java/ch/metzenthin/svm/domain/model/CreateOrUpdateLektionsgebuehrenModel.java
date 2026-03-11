package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.service.result.SaveLektionsgebuehrenResult;
import java.math.BigDecimal;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateLektionsgebuehrenModel
    extends CreateOrUpdateModel<SaveLektionsgebuehrenResult> {

  Integer getLektionslaenge();

  BigDecimal getBetrag1Kind();

  BigDecimal getBetrag2Kinder();

  BigDecimal getBetrag3Kinder();

  BigDecimal getBetrag4Kinder();

  BigDecimal getBetrag5Kinder();

  BigDecimal getBetrag6Kinder();

  void setLektionslaenge(String lektionslaenge) throws SvmValidationException;

  void setBetrag1Kind(String betrag1Kind) throws SvmValidationException;

  void setBetrag2Kinder(String betrag2Kinder) throws SvmValidationException;

  void setBetrag3Kinder(String betrag3Kinder) throws SvmValidationException;

  void setBetrag4Kinder(String betrag4Kinder) throws SvmValidationException;

  void setBetrag5Kinder(String betrag5Kinder) throws SvmValidationException;

  void setBetrag6Kinder(String betrag6Kinder) throws SvmValidationException;

  SaveLektionsgebuehrenResult speichern();
}
