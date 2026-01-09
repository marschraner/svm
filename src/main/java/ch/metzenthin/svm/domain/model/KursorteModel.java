package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.service.result.DeleteKursortResult;

/**
 * @author Martin Schraner
 */
public interface KursorteModel extends Model {

  DeleteKursortResult eintragLoeschen(int indexKursortToBeRemoved);

  CreateOrUpdateKursortModel createOrUpdateKursortModel(SvmContext svmContext);

  CreateOrUpdateKursortModel createOrUpdateKursortModel(SvmContext svmContext, int indexBearbeiten);

  KursorteTableData getKursorteTableData();

  void reloadData();
}
