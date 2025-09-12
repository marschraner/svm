package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteKurstypCommand;
import ch.metzenthin.svm.ui.componentmodel.KurstypenTableModel;

/**
 * @author Martin Schraner
 */
public interface KurstypenModel extends Model {

  DeleteKurstypCommand.Result eintragLoeschen(
      SvmContext svmContext, KurstypenTableModel kurstypenTableModel, int indexKurstypToBeRemoved);

  KurstypErfassenModel getKurstypErfassenModel(SvmContext svmContext, int indexBearbeiten);
}
