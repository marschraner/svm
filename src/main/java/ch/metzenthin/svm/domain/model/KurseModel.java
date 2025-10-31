package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;

/**
 * @author Martin Schraner
 */
public interface KurseModel {

  KursErfassenModel getKursErfassenModel(
      SvmContext svmContext, KurseTableModel kurseTableModel, int indexBearbeiten);

  String getTotal(KurseTableModel kurseTableModel);

  boolean checkIfKursHasKursanmeldungen(KurseTableModel kurseTableModel, int indexKursToBeRemoved);

  void kursLoeschen(KurseTableModel kurseTableModel, int indexKursToBeRemoved);

  void importKurseFromPreviousSemester(
      SvmModel svmModel,
      KurseSemesterwahlModel kurseSemesterwahlModel,
      KurseTableModel kurseTableModel);

  boolean checkIfSemesterIsInPast(SvmModel svmModel, KurseSemesterwahlModel kurseSemesterwahlModel);
}
