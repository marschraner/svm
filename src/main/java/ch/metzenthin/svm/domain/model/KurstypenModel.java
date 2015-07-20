package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteKurstypCommand;

/**
 * @author Martin Schraner
 */
public interface KurstypenModel extends Model {
    DeleteKurstypCommand.Result eintragLoeschen(SvmContext svmContext, int indexKurstypToBeRemoved);

    KurstypErfassenModel getKurstypErfassenModel(SvmContext svmContext, int indexBearbeiten);
}
