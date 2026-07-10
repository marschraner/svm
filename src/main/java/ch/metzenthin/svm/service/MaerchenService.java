package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.model.MaerchenAndNumberOfMaercheneinteilungen;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.service.result.DeleteMaerchenResult;
import ch.metzenthin.svm.service.result.SaveMaerchenResult;
import java.util.List;

/**
 * @author Martin Schraner
 */
public interface MaerchenService {

  List<MaerchenAndNumberOfMaercheneinteilungen> findAllMaerchenAndNumberOfMaercheneinteilungen();

  String findNaechstesNochErfasstesSchuljahr();

  boolean existsMaerchenForSchuljahr(Integer maerchenId, String schuljahr);

  SaveMaerchenResult saveMaerchen(Maerchen maerchen);

  DeleteMaerchenResult deleteMaerchen(Maerchen maerchen);
}
