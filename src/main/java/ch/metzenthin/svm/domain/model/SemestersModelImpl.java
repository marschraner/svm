package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.SemesterrechnungService;
import ch.metzenthin.svm.service.result.DeleteSemesterResult;
import ch.metzenthin.svm.ui.componentmodel.SemestersTableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class SemestersModelImpl extends AbstractModel implements SemestersModel {

  private final KursService kursService;
  private final SemesterService semesterService;
  private final SemesterrechnungService semesterrechnungService;

  public SemestersModelImpl(
      KursService kursService,
      SemesterService semesterService,
      SemesterrechnungService semesterrechnungService) {
    this.kursService = kursService;
    this.semesterService = semesterService;
    this.semesterrechnungService = semesterrechnungService;
  }

  @Override
  public SemesterErfassenModel createSemesterErfassenModel(
      SvmContext svmContext, SemestersTableModel semestersTableModel) {
    return svmContext.getModelFactory().createSemesterErfassenModel(Optional.empty());
  }

  @Override
  public SemesterErfassenModel createSemesterErfassenModel(
      SvmContext svmContext,
      SemestersTableModel semestersTableModel,
      int indexSemesterToBeModified) {
    Semester semesterToBeModified =
        getSelectedSemester(semestersTableModel, indexSemesterToBeModified);
    return svmContext
        .getModelFactory()
        .createSemesterErfassenModel(Optional.of(semesterToBeModified));
  }

  private static Semester getSelectedSemester(
      SemestersTableModel semestersTableModel, int selectedIndex) {
    return semestersTableModel
        .getSemestersTableData()
        .getSemestersAndNumberOfKurses()
        .get(selectedIndex)
        .semester();
  }

  @Override
  public boolean existsKurs(SemestersTableModel semestersTableModel, int selectedSemesterIndex) {
    Semester selectedSemester = getSelectedSemester(semestersTableModel, selectedSemesterIndex);
    return kursService.existsKursBySemesterId(selectedSemester.getSemesterId());
  }

  @Override
  public int getNumberOfReferencedSemesterrechnungen(
      SemestersTableModel semestersTableModel, int selectedSemesterIndex) {
    Semester selectedSemester = getSelectedSemester(semestersTableModel, selectedSemesterIndex);
    return semesterrechnungService.countSemesterrechnungenBySemesterId(
        selectedSemester.getSemesterId());
  }

  @Override
  public DeleteSemesterResult semesterLoeschen(
      SemestersTableModel semestersTableModel, int indexSemesterToBeRemoved) {
    Semester semesterToBeDeleted =
        getSelectedSemester(semestersTableModel, indexSemesterToBeRemoved);
    DeleteSemesterResult deleteSemesterResult;
    try {
      semesterService.deleteSemesterrechnungenAndSemester(semesterToBeDeleted);
      deleteSemesterResult = DeleteSemesterResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteSemesterResult = DeleteSemesterResult.SEMESTER_VON_KURS_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteSemesterResult = DeleteSemesterResult.SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteSemesterResult;
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
