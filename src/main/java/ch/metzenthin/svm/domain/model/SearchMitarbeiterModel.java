package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.searchfields.MitarbeiterSearchFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndListModel;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import java.util.Optional;
import lombok.Getter;

/**
 * @author Martin Schraner
 */
public interface SearchMitarbeiterModel extends SearchPersonModel {

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  @Getter
  enum LehrkraftJaNeinSelected {
    JA(Optional.of(true)),
    NEIN(Optional.of(false)),
    ALLE(Optional.empty());

    private final Optional<Boolean> selectedOptional;

    LehrkraftJaNeinSelected(Optional<Boolean> selectedOptional) {
      this.selectedOptional = selectedOptional;
    }
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  @Getter
  enum AktivJaNeinSelected {
    AKTIV(Optional.of(true)),
    NICHT_AKTIV(Optional.of(false)),
    ALLE(Optional.empty());

    private final Optional<Boolean> selectedOptional;

    AktivJaNeinSelected(Optional<Boolean> selectedOptional) {
      this.selectedOptional = selectedOptional;
    }
  }

  MitarbeiterCode[] getSelectableCodes();

  ValidationResultsAndListModel<MitarbeiterListModel> search(
      MitarbeiterSearchFields mitarbeiterSearchFields);
}
