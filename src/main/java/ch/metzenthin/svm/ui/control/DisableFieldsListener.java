package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Field;

import java.util.Set;

/**
 * @author Martin Schraner
 */
public interface DisableFieldsListener {
    void disableFields(boolean disable, Set<Field> fields);
}
