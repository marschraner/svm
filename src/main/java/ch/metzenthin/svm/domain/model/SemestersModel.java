package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteSemesterCommand;
import ch.metzenthin.svm.ui.componentmodel.SemestersTableModel;

/**
 * @author Martin Schraner
 */
public interface SemestersModel {

  SemesterErfassenModel getSemesterErfassenModel(SvmContext svmContext, int indexBearbeiten);

  DeleteSemesterCommand.Result semesterLoeschen(
      SvmContext svmContext, SemestersTableModel semestersTableModel, int indexSemesterToBeRemoved);
}
