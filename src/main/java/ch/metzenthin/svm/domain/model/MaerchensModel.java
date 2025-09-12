package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteMaerchenCommand;
import ch.metzenthin.svm.ui.componentmodel.MaerchensTableModel;

/**
 * @author Martin Schraner
 */
public interface MaerchensModel extends Model {

  MaerchenErfassenModel getMaerchenErfassenModel(
      SvmContext svmContext, int indexMaerchenToBeModified);

  DeleteMaerchenCommand.Result maerchenLoeschen(
      SvmContext svmContext, MaerchensTableModel maerchensTableModel, int indexMaerchenToBeRemoved);
}
