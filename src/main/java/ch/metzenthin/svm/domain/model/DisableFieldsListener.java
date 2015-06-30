package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;

import java.util.Set;

/**
 * @author Martin Schraner
 */
public interface DisableFieldsListener {
    void disableFields(boolean disable, Set<Field> fields);
}
