package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckCodeKuerzelBereitsInVerwendungCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateSchuelerCodeCommand;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;

/**
 * @author Martin Schraner
 */
public class CodeErfassenModelImpl extends AbstractModel implements CodeErfassenModel {

    private SchuelerCode schuelerCode = new SchuelerCode();
    private SchuelerCode schuelerCodeOrigin;

    public CodeErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public void setSchuelerCodeOrigin(SchuelerCode schuelerCodeOrigin) {
        this.schuelerCodeOrigin = schuelerCodeOrigin;
    }

    private final StringModelAttribute kuerzelModelAttribute = new StringModelAttribute(
            this,
            Field.KUERZEL, 1, 2,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schuelerCode.getKuerzel();
                }

                @Override
                public void setValue(String value) {
                    schuelerCode.setKuerzel(value);
                }
            }
    );

    @Override
    public String getKuerzel() {
        return kuerzelModelAttribute.getValue();
    }

    @Override
    public void setKuerzel(String kuerzel) throws SvmValidationException {
        kuerzelModelAttribute.setNewValue(true, kuerzel, isBulkUpdate());
    }

    private final StringModelAttribute beschreibungModelAttribute = new StringModelAttribute(
            this,
            Field.BESCHREIBUNG, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schuelerCode.getBeschreibung();
                }

                @Override
                public void setValue(String value) {
                    schuelerCode.setBeschreibung(value);
                }
            }
    );

    @Override
    public String getBeschreibung() {
        return beschreibungModelAttribute.getValue();
    }

    @Override
    public void setBeschreibung(String beschreibung) throws SvmValidationException {
        beschreibungModelAttribute.setNewValue(true, beschreibung, isBulkUpdate());
    }

    @Override
    public boolean checkCodeKuerzelBereitsInVerwendung(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(schuelerCode, schuelerCodeOrigin, svmModel.getCodesAll());
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        return checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung();
    }

    @Override
    public void speichern(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateSchuelerCodeCommand saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode, schuelerCodeOrigin, svmModel.getCodesAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);
    }

    @Override
    public void initializeCompleted() {
        if (schuelerCodeOrigin != null) {
            setBulkUpdate(true);
            try {
                setKuerzel(schuelerCodeOrigin.getKuerzel());
                setBeschreibung(schuelerCodeOrigin.getBeschreibung());
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
        return schuelerCode.getKuerzel() != null && schuelerCode.getBeschreibung() != null;
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (schuelerCode.getKuerzel() == null) {
            throw new SvmValidationException(2020, "Kürzel obligatorisch", Field.KUERZEL);
        }
        if (schuelerCode.getBeschreibung() == null) {
            throw new SvmValidationException(2021, "Beschreibung obligatorisch", Field.BESCHREIBUNG);
        }
    }
}
