package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.DisableFieldsListener;
import ch.metzenthin.svm.domain.model.MakeErrorLabelsInvisibleListener;
import ch.metzenthin.svm.domain.model.Model;
import org.apache.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Hans Stamm
 */
public abstract class AbstractController implements PropertyChangeListener, DisableFieldsListener, MakeErrorLabelsInvisibleListener {

    private static final Logger LOGGER = Logger.getLogger(AbstractController.class);

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
            LOGGER.trace("AbstractController model.validate " + e.getMessageLong());
            showErrMsgAsToolTip(e);
        }
    }

    abstract void validateFields() throws SvmValidationException;

    abstract void showErrMsg(SvmValidationException e);

    abstract void showErrMsgAsToolTip(SvmValidationException e);

    public void makeErrorLabelInvisible(Field field) {
        Set<Field> fields = new HashSet<>();
        fields.add(field);
        makeErrorLabelsInvisible(fields);
    }

}
