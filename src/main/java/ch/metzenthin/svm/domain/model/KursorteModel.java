package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteKursortCommand;

/**
 * @author Martin Schraner
 */
public interface KursorteModel extends Model {
    DeleteKursortCommand.Result eintragLoeschen(SvmContext svmContext, int indexKursortToBeRemoved);
    KursortErfassenModel getKursortErfassenModel(SvmContext svmContext, int indexBearbeiten);
}
