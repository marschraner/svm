package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.service.CodeService;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ElternmithilfeCodeErfassenModelImpl extends CodeErfassenModelImpl<ElternmithilfeCode>
    implements ElternmithilfeCodeErfassenModel {

  public ElternmithilfeCodeErfassenModelImpl(
      Optional<ElternmithilfeCode> elternmithilfeCodeToBeModifiedOptional,
      CodeService<ElternmithilfeCode> elternmithilfeCodeService) {
    super(
        elternmithilfeCodeToBeModifiedOptional.orElseGet(ElternmithilfeCode::new),
        elternmithilfeCodeService);
  }
}
