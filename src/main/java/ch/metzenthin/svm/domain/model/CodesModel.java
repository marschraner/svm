package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.domain.commands.DeleteElternmithilfeCodeCommand;
import ch.metzenthin.svm.domain.commands.DeleteSchuelerCodeCommand;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public interface CodesModel {

    DeleteSchuelerCodeCommand.Result eintragLoeschenSchuelerCodesVerwalten(SvmContext svmContext, int indexCodeToBeRemoved);
    DeleteElternmithilfeCodeCommand.Result eintragLoeschenElternmithilfeCodesVerwalten(SvmContext svmContext, int selectedRow);
    void eintragLoeschenSchuelerCodesSchueler(CodesTableModel codesTableModel, SchuelerCode schuelerCodeToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel);
    CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified, Codetyp codetyp);
    SchuelerCode[] getSelectableSchuelerCodes(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
