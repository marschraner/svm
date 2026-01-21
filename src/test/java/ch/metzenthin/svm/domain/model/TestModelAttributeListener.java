package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import lombok.Getter;

class TestModelAttributeListener implements ModelAttributeListener, PropertyChangeListener {

  @Getter private int fireCounter;
  @Getter private int invalidateCounter;
  @Getter private int propertyChangeCounter;
  private final PropertyChangeSupport propertyChangeSupport;
  @Getter private Object oldValue;
  @Getter private Object newValue;

  public TestModelAttributeListener() {
    propertyChangeSupport = new PropertyChangeSupport(this);
    propertyChangeSupport.addPropertyChangeListener(this);
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
    // Gemäss AbstractModel.firePropertyChange(Field field, Object oldValue, Object newValue)
    if ((oldValue == null) && (newValue == null)) {
      return;
    }
    this.propertyChangeSupport.firePropertyChange(field.toString(), oldValue, newValue);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    propertyChangeCounter++;
  }
}
