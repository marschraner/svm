package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckLektionslaengeBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateLektionsgebuehrenCommand;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;

import java.math.BigDecimal;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenErfassenModelImpl extends AbstractModel implements LektionsgebuehrenErfassenModel {

    private Lektionsgebuehren lektionsgebuehren = new Lektionsgebuehren();
    private Lektionsgebuehren lektionsgebuehrenOrigin;

    @Override
    public Lektionsgebuehren getLektionsgebuehren() {
        return lektionsgebuehren;
    }

    @Override
    public void setLektionsgebuehrenOrigin(Lektionsgebuehren lektionsgebuehrenOrigin) {
        this.lektionsgebuehrenOrigin = lektionsgebuehrenOrigin;
    }

    private final IntegerModelAttribute lektionslaengeModelAttribute = new IntegerModelAttribute(
            this,
            Field.LEKTIONSLAENGE, 10, 200,
            new AttributeAccessor<Integer>() {
                @Override
                public Integer getValue() {
                    return lektionsgebuehren.getLektionslaenge();
                }

                @Override
                public void setValue(Integer value) {
                    lektionsgebuehren.setLektionslaenge(value);
                }
            }
    );

    @Override
    public Integer getLektionslaenge() {
        return lektionslaengeModelAttribute.getValue();
    }

    @Override
    public void setLektionslaenge(String lektionslaenge) throws SvmValidationException {
        lektionslaengeModelAttribute.setNewValue(true, lektionslaenge, isBulkUpdate());
    }

    private final PreisModelAttribute betrag1KindModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_1_KIND, new BigDecimal("0.00"), new BigDecimal("999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return lektionsgebuehren.getBetrag1Kind();
                }

                @Override
                public void setValue(BigDecimal value) {
                    lektionsgebuehren.setBetrag1Kind(value);
                }
            }
    );

    @Override
    public BigDecimal getBetrag1Kind() {
        return betrag1KindModelAttribute.getValue();
    }

    @Override
    public void setBetrag1Kind(String betrag1Kind) throws SvmValidationException {
        betrag1KindModelAttribute.setNewValue(true, betrag1Kind, isBulkUpdate());
    }

    private final PreisModelAttribute betrag2KinderModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_2_KINDER, new BigDecimal("0.00"), new BigDecimal("999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return lektionsgebuehren.getBetrag2Kinder();
                }

                @Override
                public void setValue(BigDecimal value) {
                    lektionsgebuehren.setBetrag2Kinder(value);
                }
            }
    );

    @Override
    public BigDecimal getBetrag2Kinder() {
        return betrag2KinderModelAttribute.getValue();
    }

    @Override
    public void setBetrag2Kinder(String betrag2Kinder) throws SvmValidationException {
        betrag2KinderModelAttribute.setNewValue(true, betrag2Kinder, isBulkUpdate());
    }

    private final PreisModelAttribute betrag3KinderModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_3_KINDER, new BigDecimal("0.00"), new BigDecimal("999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return lektionsgebuehren.getBetrag3Kinder();
                }

                @Override
                public void setValue(BigDecimal value) {
                    lektionsgebuehren.setBetrag3Kinder(value);
                }
            }
    );

    @Override
    public BigDecimal getBetrag3Kinder() {
        return betrag3KinderModelAttribute.getValue();
    }

    @Override
    public void setBetrag3Kinder(String betrag3Kinder) throws SvmValidationException {
        betrag3KinderModelAttribute.setNewValue(true, betrag3Kinder, isBulkUpdate());
    }

    private final PreisModelAttribute betrag4KinderModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_4_KINDER, new BigDecimal("0.00"), new BigDecimal("999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return lektionsgebuehren.getBetrag4Kinder();
                }

                @Override
                public void setValue(BigDecimal value) {
                    lektionsgebuehren.setBetrag4Kinder(value);
                }
            }
    );

    @Override
    public BigDecimal getBetrag4Kinder() {
        return betrag4KinderModelAttribute.getValue();
    }

    @Override
    public void setBetrag4Kinder(String betrag4Kinder) throws SvmValidationException {
        betrag4KinderModelAttribute.setNewValue(true, betrag4Kinder, isBulkUpdate());
    }

    private final PreisModelAttribute betrag5KinderModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_5_KINDER, new BigDecimal("0.00"), new BigDecimal("999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return lektionsgebuehren.getBetrag5Kinder();
                }

                @Override
                public void setValue(BigDecimal value) {
                    lektionsgebuehren.setBetrag5Kinder(value);
                }
            }
    );

    @Override
    public BigDecimal getBetrag5Kinder() {
        return betrag5KinderModelAttribute.getValue();
    }

    @Override
    public void setBetrag5Kinder(String betrag5Kinder) throws SvmValidationException {
        betrag5KinderModelAttribute.setNewValue(true, betrag5Kinder, isBulkUpdate());
    }

    private final PreisModelAttribute betrag6KinderModelAttribute = new PreisModelAttribute(
            this,
            Field.BETRAG_6_KINDER, new BigDecimal("0.00"), new BigDecimal("999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return lektionsgebuehren.getBetrag6Kinder();
                }

                @Override
                public void setValue(BigDecimal value) {
                    lektionsgebuehren.setBetrag6Kinder(value);
                }
            }
    );

    @Override
    public BigDecimal getBetrag6Kinder() {
        return betrag6KinderModelAttribute.getValue();
    }

    @Override
    public void setBetrag6Kinder(String betrag6Kinder) throws SvmValidationException {
        betrag6KinderModelAttribute.setNewValue(true, betrag6Kinder, isBulkUpdate());
    }


    @Override
    public boolean checkLektionslaengeBereitsErfasst(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckLektionslaengeBereitsErfasstCommand checkLektionslaengeBereitsErfasstCommand = new CheckLektionslaengeBereitsErfasstCommand(lektionsgebuehren, lektionsgebuehrenOrigin, svmModel.getLektionsgebuehrenAllList());
        commandInvoker.executeCommand(checkLektionslaengeBereitsErfasstCommand);
        return checkLektionslaengeBereitsErfasstCommand.isBereitsErfasst();
    }

    @Override
    public void speichern(SvmModel svmModel, LektionsgebuehrenTableModel lektionsgebuehrenTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateLektionsgebuehrenCommand saveOrUpdateLektionsgebuehrenCommand = new SaveOrUpdateLektionsgebuehrenCommand(lektionsgebuehren, lektionsgebuehrenOrigin, svmModel.getLektionsgebuehrenAllList());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateLektionsgebuehrenCommand);
        // TableData mit von der Datenbank upgedateten Lektionsgebühren updaten
        lektionsgebuehrenTableModel.getLektionsgebuehrenTableData().setLektionsgebuehrenList(svmModel.getLektionsgebuehrenAllList());
    }

    @Override
    public void initializeCompleted() {
        if (lektionsgebuehrenOrigin != null) {
            setBulkUpdate(true);
            try {
                setLektionslaenge(Integer.toString(lektionsgebuehrenOrigin.getLektionslaenge()));
                setBetrag1Kind(lektionsgebuehrenOrigin.getBetrag1Kind().toString());
                setBetrag2Kinder(lektionsgebuehrenOrigin.getBetrag2Kinder().toString());
                setBetrag3Kinder(lektionsgebuehrenOrigin.getBetrag3Kinder().toString());
                setBetrag4Kinder(lektionsgebuehrenOrigin.getBetrag4Kinder().toString());
                setBetrag5Kinder(lektionsgebuehrenOrigin.getBetrag5Kinder().toString());
                setBetrag6Kinder(lektionsgebuehrenOrigin.getBetrag6Kinder().toString());
            } catch (SvmValidationException ignore) {
                ignore.printStackTrace();
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
    void doValidate() throws SvmValidationException {}
}
