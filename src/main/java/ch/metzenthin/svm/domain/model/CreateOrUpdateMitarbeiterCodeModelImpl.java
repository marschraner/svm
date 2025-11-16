package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.CodeService;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CreateOrUpdateMitarbeiterCodeModelImpl
    extends CreateOrUpdateCodeModelImpl<MitarbeiterCode>
    implements CreateOrUpdateMitarbeiterCodeModel {

  public CreateOrUpdateMitarbeiterCodeModelImpl(
      Optional<MitarbeiterCode> mitarbeiterCodeToBeModifiedOptional,
      CodeService<MitarbeiterCode> mitarbeiterCodeService) {
    super(
        mitarbeiterCodeToBeModifiedOptional.orElseGet(MitarbeiterCode::new),
        mitarbeiterCodeService);
  }
}
