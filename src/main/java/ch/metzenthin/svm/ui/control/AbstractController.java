package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hans Stamm
 */
public abstract class AbstractController implements PropertyChangeListener, DisableFieldsListener, MakeErrorLabelsInvisibleListener {

    private final Model model;

    public AbstractController(Model model) {
        this.model = model;
    }

    /**
     * Template Methode. Nachdem die Subklassen den PropertyChangeEvent verarbeitet haben, wird die Validierung aufgerufen.
     *
     * @param evt Event
     */
    @Override
    public final void propertyChange(PropertyChangeEvent evt) {
        doPropertyChange(evt);
        validate();
    }

    public boolean checkIsFieldChange(Field field, PropertyChangeEvent evt) {
        return field.toString().equals(evt.getPropertyName());
    }

    /**
     * Subklassen verarbeiten den PropertyChangeEvent und danach wird die Validierung aufgerufen.
     *
     * @param evt Event
     */
    abstract void doPropertyChange(PropertyChangeEvent evt);

    protected void validate() {
        try {
            validateFields();
        } catch (SvmValidationException e) {
            return;
        }
        try {
            model.validate();
        } catch (SvmValidationException e) {
            System.out.println("AbstractController model.validate " + e.getMessage());
            // Keine Fehlermeldung ausgeben
            // showErrMsg(e);
        }
    }

    abstract void validateFields() throws SvmValidationException;

    abstract void showErrMsg(SvmValidationException e);

    public void makeErrorLabelInvisible(Field field) {
        Set<Field> fields = new HashSet<>();
        fields.add(field);
        makeErrorLabelsInvisible(fields);

    }

}
