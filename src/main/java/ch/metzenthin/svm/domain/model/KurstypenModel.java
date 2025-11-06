package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
import ch.metzenthin.svm.ui.componentmodel.KurstypenTableModel;

/**
 * @author Martin Schraner
 */
public interface KurstypenModel extends Model {

  DeleteKurstypResult eintragLoeschen(
      KurstypenTableModel kurstypenTableModel, int indexKurstypToBeRemoved);

  CreateOrUpdateKurstypModel createCreateOrUpdateKurstypModel(
      SvmContext svmContext, KurstypenTableModel kurstypenTableModel);

  CreateOrUpdateKurstypModel createCreateOrUpdateKurstypModel(
      SvmContext svmContext, KurstypenTableModel kurstypenTableModel, int indexKurstypToBeModified);
}
