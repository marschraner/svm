package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.result.DeleteKursResult;
import ch.metzenthin.svm.ui.componentmodel.KursTableModel;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class KursListModel
    extends AbstractListModel<
        KursTableData,
        KursAndLehrkraefteAndNumberOfKursanmeldungen,
        CreateOrUpdateKursModel,
        DeleteKursResult> {

  private final KursService kursService;
  private final Semester semester;

  public KursListModel(KursService kursService, Semester semester) {
    super(createTableModel(kursService, semester));
    this.kursService = kursService;
    this.semester = semester;
  }

  private static TableModel<KursTableData, KursAndLehrkraefteAndNumberOfKursanmeldungen>
      createTableModel(KursService kursService, Semester semester) {
    List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
        kurseAndLehrkraefteAndNumberOfKursAnmeldungenForSemester =
            kursService.findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(
                semester.getSemesterId());
    KursTableData kursTableData =
        new KursTableData(kurseAndLehrkraefteAndNumberOfKursAnmeldungenForSemester);
    return new KursTableModel(kursTableData);
  }

  @Override
  public CreateOrUpdateKursModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createCreateOrUpdateKursModel(Optional.empty(), semester);
  }

  @Override
  public CreateOrUpdateKursModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexKursToBeUpdated) {
    Kurs kursToBeUpdated = getSelectedRow(indexKursToBeUpdated).kurs();
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateKursModel(Optional.of(kursToBeUpdated), semester);
  }

  @Override
  public DeleteKursResult eintragLoeschen(int indexOfKursToBeDeleted) {
    Kurs kursToBeDeleted = getSelectedRow(indexOfKursToBeDeleted).kurs();
    DeleteKursResult deleteKursResult;
    try {
      deleteKursResult = kursService.deleteKurs(kursToBeDeleted);
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteKursResult = DeleteKursResult.KURS_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteKursResult;
  }

  @Override
  public void reloadData() {
    List<KursAndLehrkraefteAndNumberOfKursanmeldungen>
        kursAndLehrkraefteAndNumberOfKursanmeldungenList =
            kursService.findAllKurseAndLehrkraefteAndNumberOfKursanmeldungenForSemester(
                semester.getSemesterId());
    tableModel.setData(kursAndLehrkraefteAndNumberOfKursanmeldungenList);
  }

  @Override
  public String getListItemName() {
    return "Kurs";
  }

  public boolean checkIfSemesterIsInPast() {
    Calendar today = new GregorianCalendar();
    return semester.getSemesterende().before(today);
  }

  public int getNumberOfReferencedKursanmeldungen(int selectedSemesterIndex) {
    Kurs selectedKurs = getSelectedRow(selectedSemesterIndex).kurs();
    return kursService.countKursanmeldungenByKursId(selectedKurs.getKursId());
  }

  public String getSemesterDisplayName() {
    return semester.getSemesterbezeichnung() + " " + semester.getSchuljahr();
  }
}
