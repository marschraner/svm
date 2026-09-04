package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.KursListModel;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndListModel;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.view.KurseSemesterwahlView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public class KurseSemesterwahlController
    extends AbstractSuchenPanelController<KurseSemesterwahlView, KursListModel> {

  private final SvmContext svmContext;
  private final KurseSemesterwahlModel model;
  private final ActionListener closeListener;
  private final ActionListener nextPanelListener;

  public KurseSemesterwahlController(
      SvmContext svmContext,
      KurseSemesterwahlModel kurseSemesterwahlModel,
      ActionListener closeListener,
      ActionListener nextPanelListener) {
    super(createView(closeListener));
    this.svmContext = svmContext;
    this.model = kurseSemesterwahlModel;
    this.closeListener = closeListener;
    this.nextPanelListener = nextPanelListener;
    configSpinnerSemesterList();
    initialiseViewFields();
  }

  private static KurseSemesterwahlView createView(ActionListener closeListener) {
    return new KurseSemesterwahlView(closeListener);
  }

  public void configSpinnerSemesterList() {
    List<Semester> semesterList = model.getAllSemesters();
    view.setSemesterSpinnerModelValues(semesterList);
  }

  private void initialiseViewFields() {
    Optional<Semester> initSemesterOptional = model.getInitSemester();
    initSemesterOptional.ifPresent(view::setSemesterSpinnerValue);
  }

  @Override
  protected ValidationResultsAndListModel<KursListModel> suchen() {
    view.setWaitCursorAllComponents();
    KursListModel kursListModel = model.suchen(view.getSemesterSpinnerValue());
    view.resetCursorAllComponents();
    return new ValidationResultsAndListModel<>(kursListModel);
  }

  @Override
  protected void showNextPanel(KursListModel kursListModel) {
    KursListController kursListController =
        new KursListController(svmContext, kursListModel, closeListener);
    nextPanelListener.actionPerformed(
        new ActionEvent(
            new Object[] {
              kursListController.getView().getRootComponent(),
              kursListModel.getSemesterDisplayName()
            },
            ActionEvent.ACTION_PERFORMED,
            "Suchresultat verfügbar"));
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    // No validation
  }

  @Override
  protected void setAllErrorLabelsInvisible() {
    // No validation
  }
}
