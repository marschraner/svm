package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.AddSchuelerCodeToSchuelerAndSaveCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public class SchuelerCodeSchuelerHinzufuegenModelImpl extends AbstractModel implements SchuelerCodeSchuelerHinzufuegenModel {

    private SchuelerCode schuelerCode;

    SchuelerCodeSchuelerHinzufuegenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public SchuelerCode getSchuelerCode() {
        return schuelerCode;
    }

    @Override
    public void setSchuelerCode(SchuelerCode schuelerCode) throws SvmRequiredException {
        this.schuelerCode = schuelerCode;
        if (schuelerCode == null) {
            invalidate();
            throw new SvmRequiredException(Field.CODE);
        }
    }

    @Override
    public void hinzufuegen(CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        AddSchuelerCodeToSchuelerAndSaveCommand addSchuelerCodeToSchuelerAndSaveCommand = new AddSchuelerCodeToSchuelerAndSaveCommand(schuelerCode, schuelerDatenblattModel.getSchueler());
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.executeCommandAsTransaction(addSchuelerCodeToSchuelerAndSaveCommand);
        Schueler schuelerUpdated = addSchuelerCodeToSchuelerAndSaveCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedatetem Schüler updaten
        if (schuelerUpdated != null) {
            codesTableModel.getCodesTableData().setCodes(schuelerUpdated.getCodesAsList());
        }
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
