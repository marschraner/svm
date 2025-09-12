package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

/**
 * @author Hans Stamm
 */
public interface Model {

  void addPropertyChangeListener(PropertyChangeListener listener);

  void addCompletedListener(CompletedListener listener);

  void removePropertyChangeListener(PropertyChangeListener listener);

  boolean checkIsFieldChange(Field field, PropertyChangeEvent evt);

  void addDisableFieldsListener(DisableFieldsListener listener);

  void removeDisableFieldsListener(DisableFieldsListener listener);

  void disableFields();

  void disableFields(Set<Field> fields);

  void enableFields();

  void enableFields(Set<Field> fields);

  void addMakeErrorLabelsInvisibleListener(
      MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener);

  void removeMakeErrorLabelsInvisibleListener(
      MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener);

  void makeErrorLabelsInvisible(Set<Field> fields);

  void initializeCompleted();

  boolean isCompleted();

  void validate() throws SvmValidationException;

  void setModelValidationMode(boolean modelValidationMode);
}
