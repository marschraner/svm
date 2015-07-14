package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public interface SvmModel {

    CodesTableModel getCodesAllTableModel();

    void setCodesAllTableModel(CodesTableModel codesAllTableModel);
}
