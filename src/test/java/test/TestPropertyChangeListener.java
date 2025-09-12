package test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class TestPropertyChangeListener implements PropertyChangeListener {

  private final List<PropertyChangeEvent> events = new ArrayList<>();

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    events.add(evt);
  }

  public int eventsSize() {
    return events.size();
  }
}
