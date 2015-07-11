package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;

/**
 * @author Martin Schraner
 */
public interface DispensationenModel {

    DispensationErfassenModel getDispensationErfassenModel(SvmContext svmContext, SchuelerDatenblattModel schuelerDatenblattModel, int indexDispensationToBeModified);

    void eintragLoeschen(SchuelerDatenblattModel schuelerDatenblattModel, int indexDispensationToBeDeleted);
}
