package ch.metzenthin.svm.domain.model.searchfields;

import ch.metzenthin.svm.domain.model.SearchMitarbeiterModel.AktivJaNeinSelected;
import ch.metzenthin.svm.domain.model.SearchMitarbeiterModel.LehrkraftJaNeinSelected;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public record MitarbeiterSearchFields(
    String nachname,
    String vorname,
    MitarbeiterCode mitarbeiterCode,
    LehrkraftJaNeinSelected lehrkraftJaNeinSelected,
    AktivJaNeinSelected aktivJaNeinSelected) {

  public Optional<String> getNachnameAsOptional() {
    return nachname != null ? Optional.of(nachname) : Optional.empty();
  }

  public Optional<String> getVornameAsOptional() {
    return vorname != null ? Optional.of(vorname) : Optional.empty();
  }

  public Optional<MitarbeiterCode> getMitarbeiterCodeAsOptional() {
    return mitarbeiterCode != null ? Optional.of(mitarbeiterCode) : Optional.empty();
  }

  public Optional<Boolean> isLehrkraftAsOptional() {
    return lehrkraftJaNeinSelected.getSelectedOptional();
  }

  public Optional<Boolean> isAktivAsOptional() {
    return aktivJaNeinSelected.getSelectedOptional();
  }
}
