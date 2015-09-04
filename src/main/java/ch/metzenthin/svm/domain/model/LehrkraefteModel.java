package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteLehrkraftCommand;
import ch.metzenthin.svm.ui.componentmodel.LehrkraefteTableModel;

/**
 * @author Martin Schraner
 */
public interface LehrkraefteModel {
    LehrkraftErfassenModel getLehrkraftErfassenModel(SvmContext svmContext, int indexBearbeiten);

    DeleteLehrkraftCommand.Result lehrkraftLoeschen(SvmContext svmContext, LehrkraefteTableModel lehrkraefteTableModel, int selectedRow);
}
