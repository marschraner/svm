package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;

/**
 * @author Martin Schraner
 */
public interface LektionsgebuehrenModel extends Model {

    boolean checkIfLektionslaengeInVerwendung(LektionsgebuehrenTableModel lektionsgebuehrenTableModel, int indexLektionsgebuehrenToBeRemoved);
    void eintragLoeschen(SvmContext svmContext, LektionsgebuehrenTableModel lektionsgebuehrenTableModel, int indexLektionsgebuehrenToBeRemoved);
    LektionsgebuehrenErfassenModel getLektionsgebuehrenErfassenModel(SvmContext svmContext, int indexBearbeiten);
}
