package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

/**
 * @author Martin Schraner
 */
public record MitarbeiterAndMitarbeiterCode(
    Mitarbeiter mitarbeiter, MitarbeiterCode mitarbeiterCode)
    implements Comparable<MitarbeiterAndMitarbeiterCode> {

  @Override
  public int compareTo(MitarbeiterAndMitarbeiterCode other) {
    int result = mitarbeiter.compareTo(other.mitarbeiter());

    if (result == 0) {
      if (mitarbeiterCode != null && other.mitarbeiterCode != null) {
        result = mitarbeiterCode.compareTo(other.mitarbeiterCode);
      } else if (mitarbeiterCode == null && other.mitarbeiterCode != null) {
        result = -1;
      } else if (mitarbeiterCode != null) {
        result = 1;
      }
    }

    return result;
  }
}
