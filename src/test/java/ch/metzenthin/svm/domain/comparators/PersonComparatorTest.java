package ch.metzenthin.svm.domain.comparators;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Martin Schraner
 */
public class PersonComparatorTest {

    @Test
    public void testCompare() {
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