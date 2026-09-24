package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.searchfields.MitarbeiterSearchFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndListModel;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import lombok.Getter;

/**
 * @author Martin Schraner
 */
public interface SearchMitarbeiterModel extends SearchPersonModel {

  @Getter
  enum LehrkraftJaNeinSelected {
    JA(true),
    NEIN(false),
    ALLE(null);

    private final Boolean value;

    LehrkraftJaNeinSelected(Boolean value) {
      this.value = value;
    }
  }

  @Getter
  enum StatusSelected {
    AKTIV(true),
    NICHT_AKTIV(false),
    ALLE(null);

    private final Boolean value;

    StatusSelected(Boolean value) {
      this.value = value;
    }
  }

  MitarbeiterCode[] getSelectableCodes();

  ValidationResultsAndListModel<MitarbeiterListModel> search(
      MitarbeiterSearchFields mitarbeiterSearchFields);
}
