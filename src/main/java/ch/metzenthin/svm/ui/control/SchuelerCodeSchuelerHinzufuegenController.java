package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SchuelerCodeSchuelerHinzufuegenModel;
import ch.metzenthin.svm.domain.model.CodesModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SchuelerCodeSchuelerHinzufuegenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(SchuelerCodeSchuelerHinzufuegenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final SvmContext svmContext;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private SchuelerCodeSchuelerHinzufuegenModel schuelerCodeSchuelerHinzufuegenModel;
    private CodesTableModel codesTableModel;
    private CodesModel codesModel;
    private JDialog codeSchuelerHinzufuegenDialog;
    private JComboBox<SchuelerCode> comboBoxCode;
    private JLabel errLblCode;
    private JButton btnOk;

    public SchuelerCodeSchuelerHinzufuegenController(SvmContext svmContext, CodesTableModel codesTableModel, SchuelerCodeSchuelerHinzufuegenModel schuelerCodeSchuelerHinzufuegenModel, CodesModel codesModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        super(schuelerCodeSchuelerHinzufuegenModel);
        this.svmContext = svmContext;
        this.codesTableModel = codesTableModel;
        this.codesModel = codesModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.schuelerCodeSchuelerHinzufuegenModel = schuelerCodeSchuelerHinzufuegenModel;
        this.schuelerCodeSchuelerHinzufuegenModel.addPropertyChangeListener(this);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void setCodeSchuelerHinzufuegenDialog(JDialog codeSchuelerHinzufuegenDialog) {
        // call onCancel() when cross is clicked
        this.codeSchuelerHinzufuegenDialog = codeSchuelerHinzufuegenDialog;
        codeSchuelerHinzufuegenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        codeSchuelerHinzufuegenDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setContentPane(JPanel contentPane) {
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setComboBoxCode(JComboBox<SchuelerCode> comboBoxCode) {
        this.comboBoxCode = comboBoxCode;
        SchuelerCode[] selectableSchuelerCodes = codesModel.getSelectableSchuelerCodes(svmContext.getSvmModel(), schuelerDatenblattModel);
        comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableSchuelerCodes));
        // Leeren ComboBox-Wert anzeigen
        comboBoxCode.setSelectedItem(null);
        this.comboBoxCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCodeSelected();
            }
        });
    }

    private void onCodeSelected() {
        LOGGER.trace("SchuelerCodeSchuelerHinzufuegenController Event SchuelerCode selected=" + comboBoxCode.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxCode.getSelectedItem(), schuelerCodeSchuelerHinzufuegenModel.getSchuelerCode());
        try {
            setModelCode();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelCode() throws SvmRequiredException {
        try {
            schuelerCodeSchuelerHinzufuegenModel.setSchuelerCode((SchuelerCode) comboBoxCode.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("SchuelerCodeSchuelerHinzufuegenController setModelCode RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxCode.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setErrLblCode(JLabel errLblCode) {
        this.errLblCode = errLblCode;
    }

    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHinzufuegen();
            }
        });
    }

    private void onHinzufuegen() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnOk.setFocusPainted(false);
            return;
        }
        schuelerCodeSchuelerHinzufuegenModel.hinzufuegen(codesTableModel, schuelerDatenblattModel);
        codeSchuelerHinzufuegenDialog.dispose();
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    private void onAbbrechen() {
        codeSchuelerHinzufuegenDialog.dispose();
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        if (checkIsFieldChange(Field.CODE, evt)) {
            comboBoxCode.setSelectedItem(schuelerCodeSchuelerHinzufuegenModel.getSchuelerCode());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (comboBoxCode.isEnabled()) {
            LOGGER.trace("Validate combobox SchuelerCode");
            setModelCode();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.CODE)) {
            errLblCode.setVisible(true);
            errLblCode.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.CODE)) {
            comboBoxCode.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.CODE)) {
            errLblCode.setVisible(false);
            comboBoxCode.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}


}
