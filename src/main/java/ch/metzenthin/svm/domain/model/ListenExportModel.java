package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Listentyp;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CreateListeCommand;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.componentmodel.LehrkraefteTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.io.File;

/**
 * @author Martin Schraner
 */
public interface ListenExportModel extends Model {
    Listentyp getListentyp();
    String getTitel();
    File getTemplateFile();

    void setListentyp(Listentyp listentyp) throws SvmRequiredException;
    void setTitel(String titel) throws SvmValidationException;

    File getSaveFileInit();
    CreateListeCommand.Result createListenFile(File outputFile, SchuelerSuchenTableModel schuelerSuchenTableModel, LehrkraefteTableModel lehrkraefteTableModel, KurseTableModel kurseTableModel);
}
