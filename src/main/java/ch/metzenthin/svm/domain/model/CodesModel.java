package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteCodeCommand;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface CodesModel {

    List<Code> getCodes();

    DeleteCodeCommand.Result eintragLoeschen(int selectedRow);

    CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified);
}
