package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckCodeKuerzelBereitsInVerwendungCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateCodeCommand;
import ch.metzenthin.svm.persistence.entities.Code;

/**
 * @author Martin Schraner
 */
public class CodeErfassenModelImpl extends AbstractModel implements CodeErfassenModel {

    private Code code = new Code();
    private Code codeOrigin;

    public CodeErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public void setCodeOrigin(Code codeOrigin) {
        this.codeOrigin = codeOrigin;
    }

    private final StringModelAttribute kuerzelModelAttribute = new StringModelAttribute(
            this,
            Field.KUERZEL, 1, 2,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return code.getKuerzel();
                }

                @Override
                public void setValue(String value) {
                    code.setKuerzel(value);
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
                    return code.getBeschreibung();
                }

                @Override
                public void setValue(String value) {
                    code.setBeschreibung(value);
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
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(code, codeOrigin, svmModel.getCodesAll());
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        return checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung();
    }

    @Override
    public void speichern(SvmModel svmModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        SaveOrUpdateCodeCommand saveOrUpdateCodeCommand = new SaveOrUpdateCodeCommand(code, codeOrigin, svmModel.getCodesAll());
        commandInvoker.executeCommandAsTransaction(saveOrUpdateCodeCommand);
    }

    @Override
    public void initializeCompleted() {
        if (codeOrigin != null) {
            setBulkUpdate(true);
            try {
                setKuerzel(codeOrigin.getKuerzel());
                setBeschreibung(codeOrigin.getBeschreibung());
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
        return code.getKuerzel() != null && code.getBeschreibung() != null;
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (code.getKuerzel() == null) {
            throw new SvmValidationException(2020, "Kürzel obligatorisch", Field.KUERZEL);
        }
        if (code.getBeschreibung() == null) {
            throw new SvmValidationException(2021, "Beschreibung obligatorisch", Field.BESCHREIBUNG);
        }
    }
}
