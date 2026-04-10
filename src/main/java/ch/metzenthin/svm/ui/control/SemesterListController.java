package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateSemesterModel;
import ch.metzenthin.svm.domain.model.SemesterAndNumberOfKurse;
import ch.metzenthin.svm.domain.model.SemesterListModel;
import ch.metzenthin.svm.domain.model.SemesterTableData;
import ch.metzenthin.svm.service.result.DeleteSemesterResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.SemesterListView;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("DuplicatedCode")
public class SemesterListController
    extends AbstractListController<SemesterListModel, DeleteSemesterResult> {

  public SemesterListController(
      SvmContext svmContext, SemesterListModel semesterListModel, ActionListener closeListener) {
    super(
        svmContext,
        semesterListModel,
        createView(semesterListModel.getTableModel(), closeListener));
  }

  private static SemesterListView createView(
      TableModel<SemesterTableData, SemesterAndNumberOfKurse> tableModel,
      ActionListener closeListener) {
    return new SemesterListView(tableModel, closeListener);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateOrUpdateSemesterModel createOrUpdateSemesterModel =
        model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateSemesterController createOrUpdateSemesterController =
        new CreateOrUpdateSemesterController(createOrUpdateSemesterModel, false, "Neues Semester");
    createOrUpdateSemesterController.initialiseModelValuesAndViewFieldsAndShowDialog();
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateSemesterModel createOrUpdateSemesterModel =
        model.createCreateOrUpdateModel(svmContext, view.getSelectedRow());
    CreateOrUpdateSemesterController createOrUpdateSemesterController =
        new CreateOrUpdateSemesterController(
            createOrUpdateSemesterModel, true, "Semester bearbeiten");
    createOrUpdateSemesterController.initialiseModelValuesAndViewFieldsAndShowDialog();
  }

  @Override
  protected void onLoeschenDialog() {
    int n =
        view.showYesNoDialog(
            "Soll das Semester aus der Datenbank gelöscht werden?", "Semester löschen?");
    if (n == 0) {
      // Löschen durchführen
      int numberOfReferencedSemesterrechnungen =
          model.getNumberOfReferencedSemesterrechnungen(view.getSelectedRow());
      boolean existsKurs = model.existsKurs(view.getSelectedRow());
      int n1 = 0;
      if (!existsKurs && numberOfReferencedSemesterrechnungen > 0) {
        n1 =
            view.showYesNoDialog(
                "ACHTUNG!\n"
                    + "Das zu löschende Semester wird von "
                    + numberOfReferencedSemesterrechnungen
                    + " Semesterrechnungen referenziert. "
                    + "Diese werden beim Löschen des Semesters mitgelöscht!\n"
                    + "Soll das Semester trotzdem gelöscht werden?",
                "Semester von Semesterrechnungen referenziert");
      }
      if (n1 == 0) {
        DeleteSemesterResult deleteSemesterResult = model.eintragLoeschen(view.getSelectedRow());
        switch (deleteSemesterResult) {
          case SEMESTER_VON_KURS_REFERENZIERT -> showErrorMessageDialog(deleteSemesterResult);
          case SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
            showErrorMessageDialog(deleteSemesterResult);
            reloadTableData();
          }
          case LOESCHEN_ERFOLGREICH -> reloadTableData();
        }
      }
    }
  }

  private void showErrorMessageDialog(DeleteSemesterResult deleteSemesterResult) {
    view.showErrorMessageDialog(deleteSemesterResult.getMessage(), "Fehler");
  }
}
