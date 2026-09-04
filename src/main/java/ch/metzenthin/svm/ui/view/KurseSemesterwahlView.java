package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.components.KurseSemesterwahlPanel;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

/**
 * @author Hans Stamm
 */
public class KurseSemesterwahlView extends AbstractSuchenPanelView<KurseSemesterwahlPanel> {

  private final JSpinner semesterSpinner;

  public KurseSemesterwahlView(ActionListener closeListener) {
    super(new KurseSemesterwahlPanel(), closeListener);
    this.semesterSpinner = panel.getSemesterSpinner();
  }

  // Semester
  public void setSemesterSpinnerModelValues(List<Semester> semesterList) {
    SpinnerModel spinnerModelSemesterList = new SpinnerListModel(semesterList);
    semesterSpinner.setModel(spinnerModelSemesterList);
    if (semesterList.isEmpty()) {
      semesterSpinner.setEnabled(false);
    }
  }

  public Semester getSemesterSpinnerValue() {
    return (Semester) semesterSpinner.getValue();
  }

  public void setSemesterSpinnerValue(Object value) {
    semesterSpinner.setValue(value);
  }
}
