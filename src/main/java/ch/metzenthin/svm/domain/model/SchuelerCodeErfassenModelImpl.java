package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.service.CodeService;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SchuelerCodeErfassenModelImpl extends CodeErfassenModelImpl<SchuelerCode>
    implements SchuelerCodeErfassenModel {

  public SchuelerCodeErfassenModelImpl(
      Optional<SchuelerCode> schuelerCodeToBeModifiedOptional,
      CodeService<SchuelerCode> schuelerCodeService) {
    super(schuelerCodeToBeModifiedOptional.orElseGet(SchuelerCode::new), schuelerCodeService);
  }
}
