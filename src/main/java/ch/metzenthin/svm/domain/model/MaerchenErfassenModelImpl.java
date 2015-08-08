package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckMaerchenBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateMaerchenCommand;
import ch.metzenthin.svm.persistence.entities.Maerchen;

/**
 * @author Martin Schraner
 */
public class MaerchenErfassenModelImpl extends AbstractModel implements MaerchenErfassenModel {

    private Maerchen maerchen = new Maerchen();
    private Maerchen maerchenOrigin;

    public MaerchenErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public Maerchen getMaerchen() {
        return maerchen;
    }

    @Override
    public void setMaerchenOrigin(Maerchen maerchenOrigin) {
        this.maerchenOrigin = maerchenOrigin;
    }

    private final StringModelAttribute schuljahrModelAttribute = new StringModelAttribute(
            this,
            Field.SCHULJAHR, 9, 9,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return maerchen.getSchuljahr();
                }

                @Override
                public void setValue(String value) {
                    maerchen.setSchuljahr(value);
                }
            }
    );

    @Override
    public String getSchuljahr() {
        return schuljahrModelAttribute.getValue();
    }

    @Override
    public void setSchuljahr(String schuljahr) throws SvmValidationException {
        schuljahrModelAttribute.setNewValue(true, schuljahr, isBulkUpdate());
    }

    private StringModelAttribute bezeichnungModelAttribute = new StringModelAttribute(
            this,
            Field.BEZEICHNUNG, 2, 30,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return maerchen.getBezeichnung();
                }

                @Override
                public void setValue(String value) {
                    maerchen.setBezeichnung(value);
                }
            }
    );

    @Override
    public String getBezeichnung() {
        return bezeichnungModelAttribute.getValue();
    }

    @Override
    public void setBezeichnung(String bezeichnung) throws SvmValidationException {
        bezeichnungModelAttribute.setNewValue(true, bezeichnung, isBulkUpdate());
    }

    @Override
    public void initializeCompleted() {
        if (maerchenOrigin != null) {
            setBulkUpdate(true);
            try {
                setSchuljahr(maerchenOrigin.getSchuljahr());
                setBezeichnung(maerchenOrigin.getBezeichnung());
            } catch (SvmValidationException ignore) {
                ignore.printStackTrace();
            }
            setBulkUpdate(false);
        } else {
            super.initializeCompleted();
        }
    }

    @Override
    public boolean checkMaerchenBereitsErfasst(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckMaerchenBereitsErfasstCommand checkMaerchenBereitsErfasstCommand = new CheckMaerchenBereitsErfasstCommand(maerchen, maerchenOrigin, svmModel.getMaerchensAll());
        commandInvoker.executeCommand(checkMaerchenBereitsErfasstCommand);
        return checkMaerchenBereitsErfasstCommand.isBereitsErfasst();
    }

    @Override
    public void speichern(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateMaerchenCommand saveOrUpdateMaerchenCommand = new SaveOrUpdateMaerchenCommand(maerchen, maerchenOrigin, svmModel.getMaerchensAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateMaerchenCommand);
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {}
}
