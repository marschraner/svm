package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import ch.metzenthin.svm.service.CodeService;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CreateOrUpdateSemesterrechnungCodeModelImpl
    extends CreateOrUpdateCodeModelImpl<SemesterrechnungCode>
    implements CreateOrUpdateSemesterrechnungCodeModel {

  public CreateOrUpdateSemesterrechnungCodeModelImpl(
      Optional<SemesterrechnungCode> semesterrechnungCodeToBeModifiedOptional,
      CodeService<SemesterrechnungCode> semesterrechnungCodeService) {
    super(
        semesterrechnungCodeToBeModifiedOptional.orElseGet(SemesterrechnungCode::new),
        semesterrechnungCodeService);
  }
}
