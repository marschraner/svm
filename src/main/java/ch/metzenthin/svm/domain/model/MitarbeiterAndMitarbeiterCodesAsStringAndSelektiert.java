package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Martin Schraner
 */
@Getter
public final class MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert
    implements EmailAndNachnameAndVornameIfc {

  private final Mitarbeiter mitarbeiter;
  private final String mitarbeiterCodesAsString;
  @Setter private boolean selektiert;

  public MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert(
      Mitarbeiter mitarbeiter, String mitarbeiterCodesAsString, boolean selektiert) {
    this.mitarbeiter = mitarbeiter;
    this.mitarbeiterCodesAsString = mitarbeiterCodesAsString;
    this.selektiert = selektiert;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert) obj;
    return Objects.equals(this.mitarbeiter, that.mitarbeiter)
        && Objects.equals(this.mitarbeiterCodesAsString, that.mitarbeiterCodesAsString)
        && this.selektiert == that.selektiert;
  }

  @Override
  public int hashCode() {
    return Objects.hash(mitarbeiter, mitarbeiterCodesAsString, selektiert);
  }

  @Override
  public String getEmail() {
    return mitarbeiter.getEmail();
  }

  @Override
  public String getNachname() {
    return mitarbeiter.getNachname();
  }

  @Override
  public String getVorname() {
    return mitarbeiter.getVorname();
  }
}
