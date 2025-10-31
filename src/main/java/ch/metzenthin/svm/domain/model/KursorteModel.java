package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.ui.componentmodel.KursorteTableModel;

/**
 * @author Martin Schraner
 */
public interface KursorteModel extends Model {

  DeleteKursortResult eintragLoeschen(
      KursorteTableModel kursorteTableModel, int indexKursortToBeRemoved);

  KursortErfassenModel createKursortErfassenModel(
      SvmContext svmContext, KursorteTableModel kursorteTableModel);

  KursortErfassenModel createKursortErfassenModel(
      SvmContext svmContext, KursorteTableModel kursorteTableModel, int indexBearbeiten);
}
