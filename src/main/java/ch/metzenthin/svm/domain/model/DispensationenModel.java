package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.DispensationenTableModel;

/**
 * @author Martin Schraner
 */
public interface DispensationenModel {

  DispensationErfassenModel getDispensationErfassenModel(
      SvmContext svmContext,
      SchuelerDatenblattModel schuelerDatenblattModel,
      int indexDispensationToBeModified);

  void eintragLoeschen(
      DispensationenTableModel dispensationenTableModel,
      SchuelerDatenblattModel schuelerDatenblattModel,
      int indexDispensationToBeDeleted);
}
