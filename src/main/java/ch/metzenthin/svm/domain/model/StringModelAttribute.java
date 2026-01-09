package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.Converter.emptyStringAsNull;
import static ch.metzenthin.svm.common.utils.Converter.nullAsEmptyString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hans Stamm
 */
public class StringModelAttribute {

  private static final Logger LOGGER = LogManager.getLogger(StringModelAttribute.class);

  private final ModelAttributeListener modelAttributeListener;
  private final AttributeAccessor<String> attributeAccessor;
  private final Field field;
  private final int minLength;
  private final int maxLength;
  private final Formatter<String> formatter;

  StringModelAttribute(
      ModelAttributeListener modelAttributeListener,
      Field field,
      int minLength,
      int maxLength,
      AttributeAccessor<String> attributeAccessor) {
    this(modelAttributeListener, field, minLength, maxLength, attributeAccessor, null);
  }

  StringModelAttribute(
      ModelAttributeListener modelAttributeListener,
      Field field,
      int minLength,
      int maxLength,
      AttributeAccessor<String> attributeAccessor,
      Formatter<String> formatter) {
    this.modelAttributeListener = modelAttributeListener;
    this.attributeAccessor = attributeAccessor;
    this.field = field;
    this.minLength = minLength;
    this.maxLength = maxLength;
    this.formatter = formatter;
  }

  String getValue() {
    return nullAsEmptyString(attributeAccessor.getValue());
  }

  void setNewValue(boolean isRequired, String newValue, boolean isBulkUpdate)
      throws SvmValidationException {
    setNewValue(isRequired, newValue, isBulkUpdate, false);
  }

  void setNewValue(
      boolean isRequired, String newValue, boolean isBulkUpdate, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    String newValueTrimmed = (newValue != null) ? newValue.trim() : "";
    if (!isBulkUpdate) {
      checkRequired(isRequired, newValueTrimmed);
    }
    String newValueFormatted = newValueTrimmed;
    if (checkNotEmpty(newValueTrimmed)) {
      if (formatter != null) {
        newValueFormatted = formatter.format(newValueTrimmed);
      }
      if (!isBulkUpdate) {
        checkMinLength(newValueFormatted);
        checkMaxLength(newValueFormatted);
      }
    }
    String oldValue = (enforcePropertyChangeEvent) ? "" : getValue();
    attributeAccessor.setValue(emptyStringAsNull(newValueFormatted));
    if (!equalsNullSafe(newValueTrimmed, newValueFormatted)
        && equalsNullSafe(oldValue, getValue())) {
      // Der Wert wurde formatiert und das Resultat entspricht dem alten Wert. Dann wird kein
      // PropertyChangeEvent ausgelöst. Damit trotzdem ein Event ausgelöst wird, wird der alte Wert
      // auf den nicht formatierten Wert gesetzt.
      // Dieser Fall tritt z.B. in folgender Situation auf:
      // - Es wird ein Attributwert eines Attributs mit Formatter im UI angezeigt
      //   (z.B. Strasse: Austrasse 5)
      // - Der Benutzer ändert den Wert auf: Austr. 5
      // - Der StrasseFormatter ändert den Wert im Model auf Austrasse 5
      //   - newValueTrimmed: Austr. 5
      //   - newValueFormatted: Austrasse 5
      //   - oldValue: Austrasse 5
      //   - getValue(): Austrasse 5
      // => ohne dieses if-Statement würde kein PropertyChange ausgeführt und im UI würde weiterhin
      // der Wert Austr. 5 angezeigt
      oldValue = newValueTrimmed;
      LOGGER.trace(
          "setNewValue: Alten Wert auf Eingabewert gesetzt, damit PropertyChangeEvent ausgelöst wird. Alter Wert={}, neuer Wert={}",
          oldValue,
          getValue());
    }
    modelAttributeListener.firePropertyChange(field, oldValue, getValue());
  }

  /**
   * Achtung: Neuer Wert wird nicht geprüft!<br>
   * Diese Methode wird verwendet für das Setzen von Attributwerten ohne Benutzereingabe (z.B.
   * Kopieren des Attributs von einem anderen Objekt (z.B. Adresse) oder Initialisierung des
   * Attributwerts mit null).
   */
  void initValue(String newValue) {
    String oldValue = getValue();
    attributeAccessor.setValue(emptyStringAsNull(newValue));
    modelAttributeListener.firePropertyChange(field, oldValue, getValue());
  }

  private void checkMaxLength(String newValue) throws SvmValidationException {
    if (newValue.length() > maxLength) {
      modelAttributeListener.invalidate();
      throw new SvmValidationException(1100, "Länge darf höchstens " + maxLength + " sein", field);
    }
  }

  private void checkMinLength(String newValue) throws SvmValidationException {
    if (newValue.length() < minLength) {
      modelAttributeListener.invalidate();
      throw new SvmValidationException(1100, "Länge muss mindestens " + minLength + " sein", field);
    }
  }

  private void checkRequired(boolean isRequired, String newValue) throws SvmRequiredException {
    if (isRequired && !checkNotEmpty(newValue)) {
      modelAttributeListener.invalidate();
      throw new SvmRequiredException(field);
    }
  }
}
