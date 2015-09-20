package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.AddSchuelerCodeToSchuelerAndSaveCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public class CodeSpecificHinzufuegenModelImpl extends AbstractModel implements CodeSpecificHinzufuegenModel {

    private Code code;

    CodeSpecificHinzufuegenModelImpl(CommandInvoker commandInvoker) {
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
    public void schuelerCodeHinzufuegen(CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        AddSchuelerCodeToSchuelerAndSaveCommand addSchuelerCodeToSchuelerAndSaveCommand = new AddSchuelerCodeToSchuelerAndSaveCommand((SchuelerCode) code, schuelerDatenblattModel.getSchueler());
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.executeCommandAsTransaction(addSchuelerCodeToSchuelerAndSaveCommand);
        Schueler schuelerUpdated = addSchuelerCodeToSchuelerAndSaveCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedatetem Schüler updaten
        if (schuelerUpdated != null) {
            codesTableModel.getCodesTableData().setCodes(schuelerUpdated.getCodesAsList());
        }
    }

    @Override
    public void mitarbeiterCodeHinzufuegen(CodesTableModel codesTableModel, MitarbeiterErfassenModel mitarbeiterErfassenModel) {
        mitarbeiterErfassenModel.getMitarbeiterCodes().add((MitarbeiterCode) code);
        // TableData updaten
        codesTableModel.getCodesTableData().setCodes(mitarbeiterErfassenModel.getMitarbeiterCodesAsList());
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
