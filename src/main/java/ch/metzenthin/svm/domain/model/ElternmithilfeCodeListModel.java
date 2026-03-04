package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.service.ElternmithilfeCodeService;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public class ElternmithilfeCodeListModel extends CodeListModel<ElternmithilfeCode> {

  protected ElternmithilfeCodeListModel(ElternmithilfeCodeService service) {
    super(createTableModel(service), service);
  }

  private static TableModel<CodeTableData<ElternmithilfeCode>, ElternmithilfeCode> createTableModel(
      ElternmithilfeCodeService service) {
    List<ElternmithilfeCode> elternmithilfeCodeList = service.findAllCodes();
    CodeTableData<ElternmithilfeCode> elternmithilfeCodeTableData =
        new CodeTableData<>(elternmithilfeCodeList);
    return new TableModel<>(elternmithilfeCodeTableData, 0.15, 0.65, 0.2);
  }

  @Override
  public CreateOrUpdateElternmithilfeCodeModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateElternmithilfeCodeModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateElternmithilfeCodeModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexToBeUpdated) {
    ElternmithilfeCode elternmithilfeCodeToBeModified = getSelectedRow(indexToBeUpdated);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateElternmithilfeCodeModel(Optional.of(elternmithilfeCodeToBeModified));
  }

  @Override
  public String getListItemName() {
    return "Eltern-Mithilfe-Code";
  }
}
