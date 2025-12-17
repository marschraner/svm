package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.service.result.DeleteSemesterResult;
import ch.metzenthin.svm.ui.componentmodel.SemestersTableModel;

/**
 * @author Martin Schraner
 */
public interface SemestersModel {

  CreateOrUpdateSemesterModel createCreateOrUpdateSemesterModel(
      SvmContext svmContext, SemestersTableModel semestersTableModel);

  CreateOrUpdateSemesterModel createCreateOrUpdateSemesterModel(
      SvmContext svmContext, SemestersTableModel semestersTableModel, int indexBearbeiten);

  boolean existsKurs(SemestersTableModel semestersTableModel, int selectedSemesterIndex);

  int getNumberOfReferencedSemesterrechnungen(
      SemestersTableModel semestersTableModel, int selectedSemesterIndex);

  DeleteSemesterResult semesterLoeschen(
      SemestersTableModel semestersTableModel, int indexSemesterToBeRemoved);
}
