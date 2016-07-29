package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.ui.componentmodel.MaercheneinteilungenTableModel;

import java.util.List;

/**
 * @author Martin Schraner
 */
public interface MaercheneinteilungenModel extends Model {


    MaercheneinteilungErfassenModel getMaercheneinteilungErfassenModel(SvmContext svmContext, MaercheneinteilungenTableModel maercheneinteilungenTableModel, int rowSelected);
    void maercheneinteilungLoeschen(MaercheneinteilungenTableModel maercheneinteilungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, int rowSelected);
    List<Maerchen> getSelectableMaerchens(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel);
    boolean isSchuelerAbgemeldet(SchuelerDatenblattModel schuelerDatenblattModel);
}
