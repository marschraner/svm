package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.MitarbeiterCodeService;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class AddOrRemoveMitarbeiterCodeAssignmentListModel
    extends AbstractAddOrRemoveCodeAssignmentListModel<MitarbeiterCode> {

  private final MitarbeiterCodeService service;

  protected AddOrRemoveMitarbeiterCodeAssignmentListModel(
      MitarbeiterCodeService service, List<MitarbeiterCode> assignedMitarbeiterCodes) {
    super(createTableModel(assignedMitarbeiterCodes), assignedMitarbeiterCodes);
    this.service = service;
  }

  private static TableModel<CodeTableData<MitarbeiterCode>, MitarbeiterCode> createTableModel(
      List<MitarbeiterCode> assignedMitarbeiterCodes) {
    CodeTableData<MitarbeiterCode> mitarbeiterCodeTableData =
        new CodeTableData<>(assignedMitarbeiterCodes, false);
    return new TableModel<>(mitarbeiterCodeTableData, 0.2, 0.8);
  }

  @Override
  public CodeSelectionModel<MitarbeiterCode> createCodeSelectionModel() {
    return new MitarbeiterCodeSelectionModelImpl(service);
  }

  @Override
  public String getListItemName() {
    return "Mitarbeiter-Code";
  }
}
