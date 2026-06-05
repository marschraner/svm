package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.model.MaerchenAndNumberOfMaercheneinteilungen;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import java.util.List;

/**
 * @author Martin Schraner
 */
public interface MaerchenService {

  List<MaerchenAndNumberOfMaercheneinteilungen> findAllMaerchenAndNumberOfMaercheneinteilungen();

  String findNaechstesNochErfasstesSchuljahr();

  void saveMaerchen(Maerchen maerchen) throws EntityAlreadyExistsException;

  void deleteMaerchen(Maerchen maerchen) throws EntityStillReferencedException;
}
