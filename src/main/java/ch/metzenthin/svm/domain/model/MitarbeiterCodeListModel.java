package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.MitarbeiterCodeService;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public class MitarbeiterCodeListModel extends CodeListModel<MitarbeiterCode> {

  protected MitarbeiterCodeListModel(MitarbeiterCodeService service) {
    super(createTableModel(service), service);
  }

  private static TableModel<CodeTableData<MitarbeiterCode>, MitarbeiterCode> createTableModel(
      MitarbeiterCodeService service) {
    List<MitarbeiterCode> mitarbeiterCodeList = service.findAllCodes();
    CodeTableData<MitarbeiterCode> mitarbeiterCodeTableData =
        new CodeTableData<>(mitarbeiterCodeList);
    return new TableModel<>(mitarbeiterCodeTableData, 0.15, 0.65, 0.2);
  }

  @Override
  public CreateOrUpdateCodeModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createCreateOrUpdateMitarbeiterCodeModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateCodeModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexToBeUpdated) {
    MitarbeiterCode mitarbeiterCodeToBeModified = getSelectedRow(indexToBeUpdated);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateMitarbeiterCodeModel(Optional.of(mitarbeiterCodeToBeModified));
  }

  @Override
  public String getListItemName() {
    return "Mitarbeiter-Code";
  }
}
