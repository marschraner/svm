package ch.metzenthin.svm.domain.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Person;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Martin Schraner
 */
class PersonComparatorTest {

  @Test
  void testCompare() {
    Person person1 = new Angehoeriger(Anrede.HERR, "Ruth", "Huber", null, null, null, false);
    Person person2 = new Angehoeriger(Anrede.HERR, "Hans", "Huber", null, null, null, false);
    Person person3 = new Angehoeriger(Anrede.HERR, "Hans", "Hofer", null, null, null, false);
    List<Person> persons = new ArrayList<>();
    persons.add(person1);
    persons.add(person2);
    persons.add(person3);

    persons.sort(new PersonComparator());

    assertEquals(person3, persons.get(0));
    assertEquals(person2, persons.get(1));
    assertEquals(person1, persons.get(2));
  }
}
