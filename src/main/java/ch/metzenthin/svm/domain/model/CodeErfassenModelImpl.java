package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckCodeKuerzelBereitsInVerwendungCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateElternmithilfeCodeCommand;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateSchuelerCodeCommand;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;

/**
 * @author Martin Schraner
 */
public class CodeErfassenModelImpl extends AbstractModel implements CodeErfassenModel {

    private String kuerzel;
    private String beschreibung;

    private SchuelerCode schuelerCodeOrigin;
    private ElternmithilfeCode elternmithilfeCodeOrigin;

    public CodeErfassenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public void setSchuelerCodeOrigin(SchuelerCode schuelerCodeOrigin) {
        this.schuelerCodeOrigin = schuelerCodeOrigin;
    }

    @Override
    public void setElternmithilfeCodeOrigin(ElternmithilfeCode elternmithilfeCodeOrigin) {
        this.elternmithilfeCodeOrigin = elternmithilfeCodeOrigin;
    }

    private final StringModelAttribute kuerzelModelAttribute = new StringModelAttribute(
            this,
            Field.KUERZEL, 1, 2,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return kuerzel;
                }

                @Override
                public void setValue(String value) {
                    kuerzel = value;
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
                    return beschreibung;
                }

                @Override
                public void setValue(String value) {
                    beschreibung = value;
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
    public boolean checkCodeKuerzelBereitsInVerwendung(SvmModel svmModel, Codetyp codetyp) {
        CommandInvoker commandInvoker = getCommandInvoker();
        CheckCodeKuerzelBereitsInVerwendungCommand checkCodeKuerzelBereitsInVerwendungCommand = null;
        switch (codetyp) {
            case SCHUELER:
                checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(kuerzel, schuelerCodeOrigin, svmModel.getSchuelerCodesAll());
                break;
            case ELTERNMITHILFE:
                checkCodeKuerzelBereitsInVerwendungCommand = new CheckCodeKuerzelBereitsInVerwendungCommand(kuerzel, elternmithilfeCodeOrigin, svmModel.getElternmithilfeCodesAll());
                break;
        }
        commandInvoker.executeCommand(checkCodeKuerzelBereitsInVerwendungCommand);
        return checkCodeKuerzelBereitsInVerwendungCommand.isBereitsInVerwendung();
    }

    @Override
    public void speichern(SvmModel svmModel, Codetyp codetyp) {
        CommandInvoker commandInvoker = getCommandInvoker();
        switch (codetyp) {
            case SCHUELER:
                SchuelerCode schuelerCode = new SchuelerCode(kuerzel, beschreibung);
                SaveOrUpdateSchuelerCodeCommand saveOrUpdateSchuelerCodeCommand = new SaveOrUpdateSchuelerCodeCommand(schuelerCode, schuelerCodeOrigin, svmModel.getSchuelerCodesAll());
                commandInvoker.executeCommandAsTransaction(saveOrUpdateSchuelerCodeCommand);
                break;
            case ELTERNMITHILFE:
                ElternmithilfeCode elternmithilfeCode = new ElternmithilfeCode(kuerzel, beschreibung);
                SaveOrUpdateElternmithilfeCodeCommand saveOrUpdateElternmithilfeCodeCommand = new SaveOrUpdateElternmithilfeCodeCommand(elternmithilfeCode, elternmithilfeCodeOrigin, svmModel.getElternmithilfeCodesAll());
                commandInvoker.executeCommandAsTransaction(saveOrUpdateElternmithilfeCodeCommand);
                break;
        }
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
        } else if (elternmithilfeCodeOrigin != null) {
            setBulkUpdate(true);
            try {
                setKuerzel(elternmithilfeCodeOrigin.getKuerzel());
                setBeschreibung(elternmithilfeCodeOrigin.getBeschreibung());
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
