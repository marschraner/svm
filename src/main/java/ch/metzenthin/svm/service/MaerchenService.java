package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.model.MaerchenAndNumberOfMaercheneinteilungen;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.service.result.DeleteMaerchenResult;
import ch.metzenthin.svm.service.result.SaveMaerchenResult;
import ch.metzenthin.svm.service.result.SaveResultAndIdOfSavedEntity;
import java.util.List;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public interface MaerchenService {

  Optional<MaerchenAndNumberOfMaercheneinteilungen> findMaerchenAndNumberOfMaercheneinteilungenById(
      int maerchenId);

  List<MaerchenAndNumberOfMaercheneinteilungen> findAllMaerchenAndNumberOfMaercheneinteilungen();

  String findNaechstesNochErfasstesSchuljahr();

  boolean existsMaerchenForSchuljahr(Integer maerchenId, String schuljahr);

  SaveResultAndIdOfSavedEntity<SaveMaerchenResult> saveMaerchen(Maerchen maerchen);

  DeleteMaerchenResult deleteMaerchen(Maerchen maerchen);
}
