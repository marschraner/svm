package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;

/**
 * @author Martin Schraner
 */
public interface LektionsgebuehrenModel extends Model {

    void eintragLoeschen(SvmContext svmContext, int indexLektionsgebuehrenToBeRemoved);
    LektionsgebuehrenErfassenModel getLektionsgebuehrenErfassenModel(SvmContext svmContext, int indexBearbeiten);
}
