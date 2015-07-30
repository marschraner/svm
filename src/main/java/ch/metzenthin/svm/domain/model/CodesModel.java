package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteCodeCommand;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public interface CodesModel {

    DeleteCodeCommand.Result eintragLoeschenCodesVerwalten(SvmContext svmContext, int indexCodeToBeRemoved);
    void eintragLoeschenCodesSchueler(CodesTableModel codesTableModel, Code codeToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel);
    CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified);
    Code[] getSelectableCodes(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
