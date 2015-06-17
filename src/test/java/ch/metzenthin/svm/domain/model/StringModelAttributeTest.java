package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Hans Stamm
 */
public class StringModelAttributeTest {

    TestModelAttributeListener testModelAttributeListener;

    @Before
    public void setUp() throws Exception {
        testModelAttributeListener = new TestModelAttributeListener();
    }

    @Test
    public void testGetValue_Null() throws Exception {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        assertEquals("", stringModelAttribute.getValue());
    }

    @Test
    public void testGetValue_NotNull() throws Exception {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor("abc")
        );
        assertEquals("abc", stringModelAttribute.getValue());
    }

    @Test
    public void testGetValue_Empty() throws Exception {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor("")
        );
        assertEquals("", stringModelAttribute.getValue());
    }

    @Test (expected = SvmRequiredException.class)
    public void testSetNewValue_IsRequired_Null() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        try {
            stringModelAttribute.setNewValue(true, null);
            fail("SvmRequiredException erwartet");
        } catch (SvmValidationException e) {
            assertEquals(1, testModelAttributeListener.getInvalidateCounter());
            assertEquals(0, testModelAttributeListener.getFireCounter());
            throw e;
        }
    }

    @Test (expected = SvmRequiredException.class)
    public void testSetNewValue_IsRequired_Empty() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        try {
            stringModelAttribute.setNewValue(true, "");
            fail("SvmRequiredException erwartet");
        } catch (SvmValidationException e) {
            assertEquals(1, testModelAttributeListener.getInvalidateCounter());
            assertEquals(0, testModelAttributeListener.getFireCounter());
            throw e;
        }
    }

    @Test
    public void testSetNewValue_IsRequired_NotEmpty() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, "abc");
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
        stringModelAttribute.setNewValue(false, null);
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
        stringModelAttribute.setNewValue(false, "");
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
        stringModelAttribute.setNewValue(false, "abc");
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test (expected = SvmValidationException.class)
    public void testSetNewValue_MinLength_Greater() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 8,
                new TestAttributeAccessor(null)
        );
        try {
            stringModelAttribute.setNewValue(true, "a");
            fail("SvmValidationException erwartet");
        } catch (SvmValidationException e) {
            assertEquals(1, testModelAttributeListener.getInvalidateCounter());
            assertEquals(0, testModelAttributeListener.getFireCounter());
            throw e;
        }
    }

    @Test
    public void testSetNewValue_MinLength_Equal() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 8,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, "ab");
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
        stringModelAttribute.setNewValue(true, "abc");
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
        stringModelAttribute.setNewValue(true, "abc");
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test (expected = SvmValidationException.class)
    public void testSetNewValue_MaxLength_Lesser() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 4,
                new TestAttributeAccessor(null)
        );
        try {
            stringModelAttribute.setNewValue(true, "abcde");
            fail("SvmValidationException erwartet");
        } catch (SvmValidationException e) {
            assertEquals(1, testModelAttributeListener.getInvalidateCounter());
            assertEquals(0, testModelAttributeListener.getFireCounter());
            throw e;
        }
    }

    @Test
    public void testSetNewValue_MaxLength_Equal() throws SvmValidationException {
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 2, 4,
                new TestAttributeAccessor(null)
        );
        stringModelAttribute.setNewValue(true, "abcd");
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
        stringModelAttribute.setNewValue(true, "abc");
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    @Test
    public void testSetNewValue_Trim() throws SvmValidationException {
        TestAttributeAccessor  testAttributeAccessor = new TestAttributeAccessor(null);
        StringModelAttribute stringModelAttribute = new StringModelAttribute(
                testModelAttributeListener,
                Field.NACHNAME, 0, 10,
                testAttributeAccessor
        );
        stringModelAttribute.setNewValue(true, "  abc  ");
        assertEquals("abc", testAttributeAccessor.settedValue);
        assertEquals(0, testModelAttributeListener.getInvalidateCounter());
        assertEquals(1, testModelAttributeListener.getFireCounter());
    }

    private static class TestModelAttributeListener implements ModelAttributeListener {

        private int invalidateCounter;
        private int fireCounter;

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
        }
    }

}