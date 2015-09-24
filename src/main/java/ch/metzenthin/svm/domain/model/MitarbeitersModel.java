package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteMitarbeiterCommand;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

/**
 * @author Martin Schraner
 */
public interface MitarbeitersModel {
    MitarbeiterErfassenModel getMitarbeiterErfassenModel(SvmContext svmContext, int indexBearbeiten);

    DeleteMitarbeiterCommand.Result mitarbeiterLoeschen(SvmContext svmContext, MitarbeitersTableModel mitarbeitersTableModel, int selectedRow);
    String getTotal(MitarbeitersTableModel mitarbeitersTableModel);
}
