package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.service.SchuelerCodeService;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public class SchuelerCodeListModel extends CodeListModel<SchuelerCode> {

  protected SchuelerCodeListModel(SchuelerCodeService service) {
    super(createTableModel(service), service);
  }

  private static TableModel<CodeTableData<SchuelerCode>, SchuelerCode> createTableModel(
      SchuelerCodeService service) {
    List<SchuelerCode> schuelerCodeList = service.findAllCodes();
    CodeTableData<SchuelerCode> schuelerCodeTableData = new CodeTableData<>(schuelerCodeList);
    return new TableModel<>(schuelerCodeTableData, 0.15, 0.65, 0.2);
  }

  @Override
  public CreateOrUpdateCodeModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createCreateOrUpdateSchuelerCodeModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateCodeModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexToBeUpdated) {
    SchuelerCode schuelerCodeToBeModified = getSelectedRow(indexToBeUpdated);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateSchuelerCodeModel(Optional.of(schuelerCodeToBeModified));
  }

  @Override
  public String getListItemName() {
    return "Schüler-Code";
  }
}
