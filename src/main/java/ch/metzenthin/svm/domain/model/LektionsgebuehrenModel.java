package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.service.result.DeleteLektionsgebuehrenResult;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;

/**
 * @author Martin Schraner
 */
public interface LektionsgebuehrenModel extends Model {

  DeleteLektionsgebuehrenResult eintragLoeschen(
      LektionsgebuehrenTableModel lektionsgebuehrenTableModel,
      int indexLektionsgebuehrenToBeRemoved);

  LektionsgebuehrenErfassenModel createLektionsgebuehrenErfassenModel(
      SvmContext svmContext, LektionsgebuehrenTableModel lektionsgebuehrenTableModel);

  LektionsgebuehrenErfassenModel createLektionsgebuehrenErfassenModel(
      SvmContext svmContext,
      LektionsgebuehrenTableModel lektionsgebuehrenTableModel,
      int indexBearbeiten);
}
