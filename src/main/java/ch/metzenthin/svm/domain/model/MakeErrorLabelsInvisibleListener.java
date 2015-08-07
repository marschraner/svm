package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;

import java.util.Set;

/**
 * @author Martin Schraner
 */
public interface MakeErrorLabelsInvisibleListener {
    void makeErrorLabelsInvisible(Set<Field> fields);
}
