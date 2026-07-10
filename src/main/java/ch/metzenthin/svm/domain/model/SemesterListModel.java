package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.SemesterrechnungService;
import ch.metzenthin.svm.service.result.DeleteSemesterResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class SemesterListModel
    extends AbstractListModel<
        SemesterTableData,
        SemesterAndNumberOfKurse,
        CreateOrUpdateSemesterModel,
        DeleteSemesterResult> {

  private final KursService kursService;
  private final SemesterService semesterService;
  private final SemesterrechnungService semesterrechnungService;

  public SemesterListModel(
      KursService kursService,
      SemesterService semesterService,
      SemesterrechnungService semesterrechnungService) {
    super(createTableModel(semesterService));
    this.kursService = kursService;
    this.semesterService = semesterService;
    this.semesterrechnungService = semesterrechnungService;
  }

  private static TableModel<SemesterTableData, SemesterAndNumberOfKurse> createTableModel(
      SemesterService semesterService) {
    List<SemesterAndNumberOfKurse> semesterList = semesterService.findAllSemesterAndNumberOfKurse();
    SemesterTableData semesterTableData = new SemesterTableData(semesterList);
    return new TableModel<>(semesterTableData);
  }

  @Override
  public CreateOrUpdateSemesterModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createCreateOrUpdateSemesterModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateSemesterModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexSemesterToBeUpdated) {
    Semester semesterToBeUpdated = getSelectedRow(indexSemesterToBeUpdated).semester();
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateSemesterModel(Optional.of(semesterToBeUpdated));
  }

  @Override
  public DeleteSemesterResult eintragLoeschen(int indexSemesterToBeDeleted) {
    Semester semesterToBeDeleted = getSelectedRow(indexSemesterToBeDeleted).semester();
    DeleteSemesterResult deleteSemesterResult;
    try {
      deleteSemesterResult =
          semesterService.deleteSemesterrechnungenAndSemester(semesterToBeDeleted);
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteSemesterResult = DeleteSemesterResult.SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteSemesterResult;
  }

  @Override
  public void reloadData() {
    List<SemesterAndNumberOfKurse> semesterList = semesterService.findAllSemesterAndNumberOfKurse();
    tableModel.setData(semesterList);
  }

  @Override
  public String getListItemName() {
    return "Semesterliste mit Anzahl Kurse";
  }

  public boolean existsKurs(int selectedSemesterIndex) {
    Semester selectedSemester = getSelectedRow(selectedSemesterIndex).semester();
    return kursService.existsKursBySemesterId(selectedSemester.getSemesterId());
  }

  public int getNumberOfReferencedSemesterrechnungen(int selectedSemesterIndex) {
    Semester selectedSemester = getSelectedRow(selectedSemesterIndex).semester();
    return semesterrechnungService.countSemesterrechnungenBySemesterId(
        selectedSemester.getSemesterId());
  }
}
