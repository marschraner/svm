package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.AddCodeToSchuelerAndSaveCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public class CodeSchuelerHinzufuegenModelImpl extends AbstractModel implements CodeSchuelerHinzufuegenModel {

    private Code code;

    CodeSchuelerHinzufuegenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public void setCode(Code code) throws SvmRequiredException {
        this.code = code;
        if (code == null) {
            invalidate();
            throw new SvmRequiredException(Field.CODE);
        }
    }

    @Override
    public void hinzufuegen(CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        AddCodeToSchuelerAndSaveCommand addCodeToSchuelerAndSaveCommand = new AddCodeToSchuelerAndSaveCommand(code, schuelerDatenblattModel.getSchueler());
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.executeCommandAsTransaction(addCodeToSchuelerAndSaveCommand);
        Schueler schuelerUpdated = addCodeToSchuelerAndSaveCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedatetem Schüler updaten
        codesTableModel.getCodesTableData().setCodes(schuelerUpdated.getCodesAsList());
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
