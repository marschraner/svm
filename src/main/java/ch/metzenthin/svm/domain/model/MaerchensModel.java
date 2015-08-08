package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteMaerchenCommand;

/**
 * @author Martin Schraner
 */
public interface MaerchensModel extends Model {
    MaerchenErfassenModel getMaerchenErfassenModel(SvmContext svmContext, int indexMaerchenToBeModified);
    DeleteMaerchenCommand.Result maerchenLoeschen(SvmContext svmContext, int indexMaerchenToBeRemoved);
}
