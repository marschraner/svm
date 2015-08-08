package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteSchuelerCodeCommand;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public interface CodesModel {

    DeleteSchuelerCodeCommand.Result eintragLoeschenCodesVerwalten(SvmContext svmContext, int indexCodeToBeRemoved);
    void eintragLoeschenCodesSchueler(CodesTableModel codesTableModel, SchuelerCode schuelerCodeToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel);
    CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified);
    SchuelerCode[] getSelectableCodes(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
