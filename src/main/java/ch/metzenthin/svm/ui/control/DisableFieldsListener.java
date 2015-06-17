package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.FieldName;

import java.util.Set;

/**
 * @author Hans Stamm
 */
public interface DisableFieldsListener {
    void disableFields(boolean disable, Set<FieldName> fieldNames);
}
