package ch.metzenthin.svm.domain.comparators;

import ch.metzenthin.svm.persistence.entities.Person;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * @author Martin Schraner
 */
public class PersonComparator implements Comparator<Person> {

  @Override
  public int compare(Person person1, Person person2) {
    // Alphabetische Sortierung mit Berücksichtigung von Umlauten
    // http://50226.de/sortieren-mit-umlauten-in-java.html
    Collator collator = Collator.getInstance(Locale.GERMAN);
    collator.setStrength(Collator.SECONDARY); // a == A, a < Ä
    int result = collator.compare(person1.getNachname(), person2.getNachname());
    if (result == 0) {
      result = collator.compare(person1.getVorname(), person2.getVorname());
      if (result == 0 && person1.getAdresse() != null && person2.getAdresse() != null) {
        result = collator.compare(person1.getAdresse().getOrt(), person2.getAdresse().getOrt());
        if (result == 0
            && person1.getAdresse().getStrasse() != null
            && person2.getAdresse().getStrasse() != null) {
          result =
              collator.compare(
                  person1.getAdresse().getStrasse(), person2.getAdresse().getStrasse());
        }
      }
    }
    return result;
  }
}
