package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;

/**
 * @author Martin Schraner
 */
public interface KursanmeldungenModel extends Model {
    
    KursanmeldungErfassenModel getKurseinteilungErfassenModel(SvmContext svmContext, KursanmeldungenTableModel kursanmeldungenTableModel, int rowSelected);
    void kurseinteilungLoeschen(KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, int rowSelected);
}
