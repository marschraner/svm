package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;

import java.math.BigDecimal;

/**
 * @author Martin Schraner
 */
public interface LektionsgebuehrenErfassenModel extends Model {

    void setLektionsgebuehrenOrigin(Lektionsgebuehren lektionsgebuehrenOrigin);

    Integer getLektionslaenge();

    BigDecimal getBetrag1Kind();

    BigDecimal getBetrag2Kinder();

    BigDecimal getBetrag3Kinder();

    BigDecimal getBetrag4Kinder();

    BigDecimal getBetrag5Kinder();

    BigDecimal getBetrag6Kinder();

    Lektionsgebuehren getLektionsgebuehren();

    void setLektionslaenge(String lektionslaenge) throws SvmValidationException;

    void setBetrag1Kind(String betrag1Kind) throws SvmValidationException;

    void setBetrag2Kinder(String betrag1Kind) throws SvmValidationException;

    void setBetrag3Kinder(String betrag1Kind) throws SvmValidationException;

    void setBetrag4Kinder(String betrag1Kind) throws SvmValidationException;

    void setBetrag5Kinder(String betrag1Kind) throws SvmValidationException;

    void setBetrag6Kinder(String betrag1Kind) throws SvmValidationException;

    boolean checkLektionslaengeBereitsErfasst(SvmModel svmModel);

    void speichern(SvmModel svmModel, LektionsgebuehrenTableModel lektionsgebuehrenTableModel);
}
