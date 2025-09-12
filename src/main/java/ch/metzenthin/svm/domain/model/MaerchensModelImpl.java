package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteMaerchenCommand;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.ui.componentmodel.MaerchensTableModel;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaerchensModelImpl extends AbstractModel implements MaerchensModel {

  @Override
  public MaerchenErfassenModel getMaerchenErfassenModel(
      SvmContext svmContext, int indexMaerchenToBeModified) {
    MaerchenErfassenModel maerchenErfassenModel =
        svmContext.getModelFactory().createMaerchenErfassenModel();
    List<Maerchen> maerchens = svmContext.getSvmModel().getMaerchensAll();
    maerchenErfassenModel.setMaerchenOrigin(maerchens.get(indexMaerchenToBeModified));
    return maerchenErfassenModel;
  }

  @Override
  public DeleteMaerchenCommand.Result maerchenLoeschen(
      SvmContext svmContext,
      MaerchensTableModel maerchensTableModel,
      int indexMaerchenToBeRemoved) {
    List<Maerchen> maerchens = svmContext.getSvmModel().getMaerchensAll();
    CommandInvoker commandInvoker = getCommandInvoker();
    DeleteMaerchenCommand deleteMaerchenCommand =
        new DeleteMaerchenCommand(maerchens, indexMaerchenToBeRemoved);
    commandInvoker.executeCommandAsTransaction(deleteMaerchenCommand);
    // TableData mit von der Datenbank upgedateten Märchens updaten
    maerchensTableModel
        .getMaerchensTableData()
        .setMaerchens(svmContext.getSvmModel().getMaerchensAll());
    return deleteMaerchenCommand.getResult();
  }

  @Override
  void doValidate() throws SvmValidationException {
    // Keine feldübergreifende Validierung notwendig
  }

  @Override
  public boolean isCompleted() {
    return false;
  }
}
