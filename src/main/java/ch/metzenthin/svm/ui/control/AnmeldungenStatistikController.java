package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.AnmeldungenStatistikModel;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.SchuelerSuchenResult;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.SchuelerSuchenResultPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class AnmeldungenStatistikController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(AnmeldungenStatistikController.class);

    private static final String AN_ABMELDEMONAT_DATE_FORMAT_STRING = "MM.yyyy";

    private AnmeldungenStatistikModel anmeldungenStatistikModel;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private JTextField txtAnAbmeldemonat;
    private JLabel errLblAnAbmeldemonat;
    private JRadioButton radioBtnAnmeldungen;
    private JRadioButton radioBtnAbmeldungen;
    private JButton btnSuchen;
    private JButton btnAbbrechen;

    public AnmeldungenStatistikController(AnmeldungenStatistikModel anmeldungenStatistikModel) {
        super(anmeldungenStatistikModel);
        this.anmeldungenStatistikModel = anmeldungenStatistikModel;
        this.anmeldungenStatistikModel.addPropertyChangeListener(this);
        this.anmeldungenStatistikModel.addDisableFieldsListener(this);
        this.anmeldungenStatistikModel.addMakeErrorLabelsInvisibleListener(this);
        this.anmeldungenStatistikModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onAnmeldungenStatistikModelCompleted(completed);
            }
        });
    }

    public void setTxtAnAbmeldemonat(JTextField txtAnAbmeldemonat) {
        this.txtAnAbmeldemonat = txtAnAbmeldemonat;
        this.txtAnAbmeldemonat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnAbmeldemonatEvent();
            }
        });
        this.txtAnAbmeldemonat.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAnAbmeldemonatEvent();
            }
        });
    }

    public void setRadioBtnGroupAnAbmeldungen(JRadioButton radioBtnAnmeldungen, JRadioButton radioBtnAbmeldungen) {
        this.radioBtnAnmeldungen = radioBtnAnmeldungen;
        this.radioBtnAbmeldungen = radioBtnAbmeldungen;
        // Action Commands
        this.radioBtnAnmeldungen.setActionCommand(AnmeldungenStatistikModel.AnAbmeldungenSelected.ANMELDUNGEN.toString());
        this.radioBtnAbmeldungen.setActionCommand(AnmeldungenStatistikModel.AnAbmeldungenSelected.ABMELDUNGEN.toString());
        // Listener
        RadioBtnGroupAnAbmeldungenListener radioBtnGroupAnAbmeldungenListener = new RadioBtnGroupAnAbmeldungenListener();
        this.radioBtnAnmeldungen.addActionListener(radioBtnGroupAnAbmeldungenListener);
        this.radioBtnAbmeldungen.addActionListener(radioBtnGroupAnAbmeldungenListener);
    }

    public void setBtnSuchen(JButton btnSuchen) {
        this.btnSuchen = btnSuchen;
        this.btnSuchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSuchen();
            }
        });
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        this.btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setErrLblAnAbmeldemonat(JLabel errLblAnAbmeldemonat) {
        this.errLblAnAbmeldemonat = errLblAnAbmeldemonat;
    }

    private void onAnAbmeldemonatEvent() {
        LOGGER.trace("AnmeldungenStatistikController Event An-/Abmeldemonat");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnAbmeldemonat.getText(), anmeldungenStatistikModel.getAnAbmeldemonat(), AN_ABMELDEMONAT_DATE_FORMAT_STRING);
        setModelAnAbmeldemonat();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnAbmeldemonat() {
        errLblAnAbmeldemonat.setVisible(false);
        try {
            anmeldungenStatistikModel.setAnAbmeldemonat(txtAnAbmeldemonat.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("AnmeldungenStatistikController setModelAnAbmeldemonat Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onAbbrechen() {
        LOGGER.trace("SchuelerSuchenPanel Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onSuchen() {
        LOGGER.trace("SchuelerSuchenPanel Suchen gedrückt");
        SchuelerSuchenResult schuelerSuchenResult = anmeldungenStatistikModel.suchen();
        SchuelerSuchenTableModel schuelerSuchenTableModel = new SchuelerSuchenTableModel(schuelerSuchenResult);
        SchuelerSuchenResultPanel schuelerSuchenResultPanel = new SchuelerSuchenResultPanel(schuelerSuchenTableModel);
        schuelerSuchenResultPanel.addNextPanelListener(nextPanelListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerSuchenResultPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
    }

    private void onAnmeldungenStatistikModelCompleted(boolean completed) {
        LOGGER.trace("AnmeldungenStatistikModel completed=" + completed);
        if (completed) {
            btnSuchen.setToolTipText(null);
            btnSuchen.setEnabled(true);
        } else {
            btnSuchen.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnSuchen.setEnabled(false);
        }
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        if (checkIsFieldChange(Field.AN_ABMELDEMONAT, evt)) {
            txtAnAbmeldemonat.setText(asString(anmeldungenStatistikModel.getAnAbmeldemonat(), AN_ABMELDEMONAT_DATE_FORMAT_STRING));
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtAnAbmeldemonat.isEnabled()) {
            LOGGER.trace("Validate field An-/Abmeldemonat");
            setModelAnAbmeldemonat();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.AN_ABMELDEMONAT)) {
            errLblAnAbmeldemonat.setVisible(true);
            errLblAnAbmeldemonat.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.AN_ABMELDEMONAT)) {
            txtAnAbmeldemonat.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.AN_ABMELDEMONAT)) {
            errLblAnAbmeldemonat.setVisible(false);
            txtAnAbmeldemonat.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.AN_ABMELDEMONAT)) {
            txtAnAbmeldemonat.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANMELDUNGEN)) {
            radioBtnAnmeldungen.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ABMELDUNGEN)) {
            radioBtnAbmeldungen.setEnabled(!disable);
        }
    }

    class RadioBtnGroupAnAbmeldungenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("AnmeldungenStatistikController AnAbmeldungen Event");
            anmeldungenStatistikModel.setAnAbmeldungen(AnmeldungenStatistikModel.AnAbmeldungenSelected.valueOf(e.getActionCommand()));
        }
    }

}
