package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Hans Stamm
 */
public abstract class AbstractController implements PropertyChangeListener{

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
        if (validateFields()) { // todo $$$ besser mit Exceptions!
            try {
                model.validate();
            } catch (SvmValidationException e) {
                show(e);
            }
        }
    }

    abstract boolean validateFields();

    abstract void show(SvmValidationException e);

}
