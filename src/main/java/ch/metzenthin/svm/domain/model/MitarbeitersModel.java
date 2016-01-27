package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.DeleteMitarbeiterCommand;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface MitarbeitersModel {
    MitarbeiterErfassenModel getMitarbeiterErfassenModel(SvmContext svmContext, MitarbeitersTableModel mitarbeitersTableModel, int indexBearbeiten);

    DeleteMitarbeiterCommand.Result mitarbeiterLoeschen(MitarbeitersTableModel mitarbeitersTableModel, int selectedRow);
    String getTotal(MitarbeitersTableModel mitarbeitersTableModel);
    CallDefaultEmailClientCommand.Result callEmailClient(MitarbeitersTableModel mitarbeitersTableModel);
    List<String> getFehlendeEmailAdressen();
    List<String> getUngueltigeEmailAdressen();
}
