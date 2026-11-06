package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.service.SchuelerCodeService;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class AddOrRemoveSchuelerCodeAssignmentListModel
    extends AbstractAddOrRemoveCodeAssignmentListModel<SchuelerCode> {

  private final SchuelerCodeService service;

  protected AddOrRemoveSchuelerCodeAssignmentListModel(
      SchuelerCodeService service, List<SchuelerCode> assignedSchuelerCodes) {
    super(createTableModel(assignedSchuelerCodes), assignedSchuelerCodes);
    this.service = service;
  }

  private static TableModel<CodeTableData<SchuelerCode>, SchuelerCode> createTableModel(
      List<SchuelerCode> assignedSchuelerCodes) {
    CodeTableData<SchuelerCode> schuelerCodeTableData =
        new CodeTableData<>(assignedSchuelerCodes, false);
    return new TableModel<>(schuelerCodeTableData, 0.2, 0.8);
  }

  @Override
  public CodeSelectionModel<SchuelerCode> createCodeSelectionModel() {
    return new SchuelerCodeSelectionModelImpl(service);
  }

  @Override
  public String getListItemName() {
    return "Schüler-Code";
  }
}
