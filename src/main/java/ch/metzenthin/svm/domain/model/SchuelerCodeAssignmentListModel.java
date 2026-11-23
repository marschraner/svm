package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.service.SchuelerSchuelerCodeService;
import ch.metzenthin.svm.service.result.DeleteAssignmentResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.List;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public class SchuelerCodeAssignmentListModel
    extends AbstractCreateOrDeleteListModel<
        SchuelerCode,
        TableModel<CodeTableData<SchuelerCode>, SchuelerCode>,
        DeleteAssignmentResult> {

  private final int schuelerId;
  private final SchuelerSchuelerCodeService schuelerSchuelerCodeService;

  protected SchuelerCodeAssignmentListModel(
      int schuelerId, SchuelerSchuelerCodeService schuelerSchuelerCodeService) {
    super(createTableModel(schuelerId, schuelerSchuelerCodeService));
    this.schuelerId = schuelerId;
    this.schuelerSchuelerCodeService = schuelerSchuelerCodeService;
  }

  private static TableModel<CodeTableData<SchuelerCode>, SchuelerCode> createTableModel(
      int schuelerId, SchuelerSchuelerCodeService schuelerSchuelerCodeService) {
    List<SchuelerCode> schuelerCodes =
        schuelerSchuelerCodeService.findAssignedSchuelerCodesBySchuelerId(schuelerId);
    CodeTableData<SchuelerCode> schuelerCodeTableData = new CodeTableData<>(schuelerCodes, false);
    return new TableModel<>(schuelerCodeTableData, 0.2, 0.8);
  }

  @Override
  public DeleteAssignmentResult eintragLoeschen(int indexOfCodeToBeDeleted) {
    SchuelerCode schuelerCodeToBeRemoved = getSelectedRow(indexOfCodeToBeDeleted);
    return schuelerSchuelerCodeService.deleteSchuelerCodeAssignment(
        schuelerId, schuelerCodeToBeRemoved);
  }

  @Override
  public CreateCodeAssignmentModel<SchuelerCode> createCreateModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createSchuelerCodeAssignmentModel(schuelerId);
  }

  @Override
  protected Optional<SchuelerCode> loadObjectFromDatabase(int codeId) {
    return schuelerSchuelerCodeService.findAssignedSchuelerCodeBySchuelerIdAndSchuelerCodeId(
        schuelerId, codeId);
  }

  @Override
  public void reloadData() {
    List<SchuelerCode> codeList =
        schuelerSchuelerCodeService.findAssignedSchuelerCodesBySchuelerId(schuelerId);
    tableModel.setData(codeList);
  }

  @Override
  public String getListItemName() {
    return "Schüler-Code";
  }
}
