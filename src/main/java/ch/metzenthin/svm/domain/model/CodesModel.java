package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteCodeCommand;
import ch.metzenthin.svm.persistence.entities.Code;

/**
 * @author Martin Schraner
 */
public interface CodesModel {

    DeleteCodeCommand.Result eintragLoeschenCodesVerwalten(SvmContext svmContext, int indexCodeToBeRemoved);
    void eintragLoeschenCodesSchueler(int indexCodeToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel);
    CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified);
    Code[] getSelectableCodes(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
