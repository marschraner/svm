package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Hans Stamm
 */
public abstract class AbstractController implements PropertyChangeListener, DisableFieldsListener {

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
            show(e);
        }
    }

    abstract void validateFields() throws SvmValidationException;

    abstract void show(SvmValidationException e);

}
