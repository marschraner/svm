package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Hans Stamm
 */
public class StringModelAttributeTest {

    TestModelAttributeListener testModelAttributeListener;

    @Before
    public void setUp() {
        testModelAttributeListener = new TestModelAttributeListener();
    }

    @Test
    public void testGetValue_Null() {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        assertEquals("", stringModelAttribute.getValue());
    }

    @Test
    public void testGetValue_NotNull() {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor("abc")
        );
        assertEquals("abc", stringModelAttribute.getValue());
    }

    @Test
    public void testGetValue_Empty() {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor("")
        );
        assertEquals("", stringModelAttribute.getValue());
    }

    @Test
    public void testSetNewValue_IsRequired_Null() {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        assertThrows(SvmRequiredException.class,
                () -> stringModelAttribute.setNewValue(true, null, false));
    }

    @Test
    public void testSetNewValue_IsRequired_Null_BulkUpdate() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, null, true);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_IsRequired_Empty() {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        assertThrows(SvmRequiredException.class,
                () -> stringModelAttribute.setNewValue(true, "", false));
    }

    @Test
    public void testSetNewValue_IsRequired_NotEmpty() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, "abc", false);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_IsNotRequired_Null() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(false, null, false);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_IsNotRequired_Empty() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(false, "", false);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_IsNotRequired_NotEmpty() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(false, "abc", false);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_MinLength_Greater() {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 8,
                new TestAttributeAccessor(null)
        );
        assertThrows(SvmValidationException.class,
                () -> stringModelAttribute.setNewValue(true, "a", false));
    }

    @Test
    public void testSetNewValue_MinLength_Equal() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, "ab", false);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_MinLength_Lesser() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, "abc", false);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_MinLength_Zero() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, "cde", false);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_MaxLength_Lesser() {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 4,
                new TestAttributeAccessor(null)
        );
        assertThrows(SvmValidationException.class,
                () -> stringModelAttribute.setNewValue(true, "abcde", false));
    }

    @Test
    public void testSetNewValue_MaxLength_Equal() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 4,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, "abcd", false);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_MaxLength_Greater() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 4,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, "abc", false);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_Trim() throws SvmValidationException {
        TestAttributeAccessor testAttributeAccessor = new TestAttributeAccessor(null);
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 10,
                testAttributeAccessor
        );
        stringModelAttribute.setNewValue(true, "  abc  ", false);
        assertEquals("abc", testAttributeAccessor.settedValue);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testInitValue_Null() {
        TestAttributeAccessor testAttributeAccessor = new TestAttributeAccessor("TestValue");
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 10,
                testAttributeAccessor
        );
        stringModelAttribute.initValue(null);
        assertNull(testAttributeAccessor.settedValue);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testInitValue_Value() {
        TestAttributeAccessor testAttributeAccessor = new TestAttributeAccessor("TestValue");
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 10,
                testAttributeAccessor
        );
        stringModelAttribute.initValue("initValue");
        assertEquals("initValue", testAttributeAccessor.settedValue);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_NoFormatter() throws SvmValidationException {
        String strasse = "Austrasse 5";
        TestAttributeAccessor testAttributeAccessor = new TestAttributeAccessor(strasse);
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.STRASSE_HAUSNUMMER, 0, 50,
                testAttributeAccessor
        );
        String input = "Austr. 5";
        stringModelAttribute.setNewValue(true, input, false);
        assertEquals(input, testAttributeAccessor.settedValue);
        assertEquals(strasse, testModelAttributeListener.oldValue);
        assertEquals(input, testModelAttributeListener.newValue);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_FormatterChanged() throws SvmValidationException {
        String strasse = "Austrasse 5";
        TestAttributeAccessor testAttributeAccessor = new TestAttributeAccessor(strasse);
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.STRASSE_HAUSNUMMER, 0, 50,
                testAttributeAccessor,
                new StrasseFormatter()
        );
        String input = "Austr. 5";
        stringModelAttribute.setNewValue(true, input, false);
        assertEquals(strasse, testAttributeAccessor.getValue);
        assertEquals(input, testModelAttributeListener.oldValue);
        assertEquals(strasse, testModelAttributeListener.newValue);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_FormatterUnchanged() throws SvmValidationException {
        String strasse = "Austrasse 5";
        TestAttributeAccessor testAttributeAccessor = new TestAttributeAccessor(strasse);
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.STRASSE_HAUSNUMMER, 0, 50,
                testAttributeAccessor,
                new StrasseFormatter()
        );
        stringModelAttribute.setNewValue(true, strasse, false);
        assertEquals(strasse, testAttributeAccessor.getValue);
        assertEquals(strasse, testModelAttributeListener.oldValue);
        assertEquals(strasse, testModelAttributeListener.newValue);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    private static class TestModelAttributeListener implements ModelAttributeListener {

        private int invalidateCounter;
        private int fireCounter;

        private Object oldValue;
        private Object newValue;

        public int getInvalidateCounter() {
            return invalidateCounter;
        }

        public int getFireCounter() {
            return fireCounter;
        }

        @Override
        public void invalidate() {
            invalidateCounter++;
        }

        @Override
        public void firePropertyChange(Field field, Object oldValue, Object newValue) {
            fireCounter++;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

    }

    private static class TestAttributeAccessor implements AttributeAccessor<String> {
        private String getValue;
        private String settedValue;

        private TestAttributeAccessor(String getValue) {
            this.getValue = getValue;
        }

        @Override
        public String getValue() {
            return getValue;
        }

        @Override
        public void setValue(String value) {
            settedValue = value;
            getValue = value;
        }
    }

}