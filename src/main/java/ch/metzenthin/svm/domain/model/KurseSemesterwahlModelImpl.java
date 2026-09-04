package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.SemesterService;
import java.util.List;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public class KurseSemesterwahlModelImpl implements KurseSemesterwahlModel {

  private final KursService kursService;
  private final SemesterService semesterService;

  public KurseSemesterwahlModelImpl(KursService kursService, SemesterService semesterService) {
    this.kursService = kursService;
    this.semesterService = semesterService;
  }

  @Override
  public List<Semester> getAllSemesters() {
    return semesterService.findAllSemesters();
  }

  @Override
  public Optional<Semester> getInitSemester() {
    return semesterService.determineInitSemesterForSemesterSelectionComponents(40);
  }

  @Override
  public KursListModel suchen(Semester semester) {
    return new KursListModel(kursService, semester);
  }
}
