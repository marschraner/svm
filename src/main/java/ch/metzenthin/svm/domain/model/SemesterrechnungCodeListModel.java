package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import ch.metzenthin.svm.service.SemesterrechnungCodeService;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public class SemesterrechnungCodeListModel extends CodeListModel<SemesterrechnungCode> {

  protected SemesterrechnungCodeListModel(SemesterrechnungCodeService service) {
    super(createTableModel(service), service);
  }

  private static TableModel<CodeTableData<SemesterrechnungCode>, SemesterrechnungCode>
      createTableModel(SemesterrechnungCodeService service) {
    List<SemesterrechnungCode> semesterrechnungCodeList = service.findAllCodes();
    CodeTableData<SemesterrechnungCode> semesterrechnungCodeTableData =
        new CodeTableData<>(semesterrechnungCodeList);
    return new TableModel<>(semesterrechnungCodeTableData, 0.15, 0.65, 0.2);
  }

  @Override
  public CreateOrUpdateCodeModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateSemesterrechnungCodeModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateCodeModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexToBeUpdated) {
    SemesterrechnungCode semesterrechnungCodeToBeModified = getSelectedRow(indexToBeUpdated);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateSemesterrechnungCodeModel(
            Optional.of(semesterrechnungCodeToBeModified));
  }

  @Override
  public String getListItemName() {
    return "Semesterrechnung-Code";
  }
}
