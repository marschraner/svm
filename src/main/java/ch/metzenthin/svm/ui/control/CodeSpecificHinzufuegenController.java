package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CodesModel;
import ch.metzenthin.svm.domain.model.MitarbeiterErfassenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.domain.model.CodeSpecificHinzufuegenModel;
import ch.metzenthin.svm.persistence.entities.Code;
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
public class CodeSpecificHinzufuegenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(CodeSpecificHinzufuegenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final SvmContext svmContext;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private CodeSpecificHinzufuegenModel codeSpecificHinzufuegenModel;
    private MitarbeiterErfassenModel mitarbeiterErfassenModel;
    private Codetyp codetyp;
    private CodesTableModel codesTableModel;
    private CodesModel codesModel;
    private JDialog codeSpecificHinzufuegenDialog;
    private JComboBox<Code> comboBoxCode;
    private JLabel errLblCode;
    private JButton btnOk;

    public CodeSpecificHinzufuegenController(SvmContext svmContext, CodesTableModel codesTableModel, CodeSpecificHinzufuegenModel codeSpecificHinzufuegenModel, CodesModel codesModel, SchuelerDatenblattModel schuelerDatenblattModel, MitarbeiterErfassenModel mitarbeiterErfassenModel, Codetyp codetyp) {
        super(codeSpecificHinzufuegenModel);
        this.svmContext = svmContext;
        this.codesTableModel = codesTableModel;
        this.codesModel = codesModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.codeSpecificHinzufuegenModel = codeSpecificHinzufuegenModel;
        this.mitarbeiterErfassenModel = mitarbeiterErfassenModel;
        this.codetyp = codetyp;
        this.codeSpecificHinzufuegenModel.addPropertyChangeListener(this);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void setCodeSpecificHinzufuegenDialog(JDialog codeSpecificHinzufuegenDialog) {
        // call onCancel() when cross is clicked
        this.codeSpecificHinzufuegenDialog = codeSpecificHinzufuegenDialog;
        codeSpecificHinzufuegenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        codeSpecificHinzufuegenDialog.addWindowListener(new WindowAdapter() {
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

    public void setComboBoxCode(JComboBox<Code> comboBoxCode) {
        this.comboBoxCode = comboBoxCode;
        Code[] selectableCodes = new Code[]{};
        switch (codetyp) {
            case SCHUELER:
                selectableCodes = codesModel.getSelectableSchuelerCodes(svmContext.getSvmModel(), schuelerDatenblattModel);
                break;
            case MITARBEITER:
                selectableCodes = codesModel.getSelectableMitarbeiterCodes(svmContext.getSvmModel(), mitarbeiterErfassenModel);
                break;
        }
        comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableCodes));
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
        LOGGER.trace("xCodeXHinzufuegenController Event SchuelerCode selected=" + comboBoxCode.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxCode.getSelectedItem(), codeSpecificHinzufuegenModel.getCode());
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
            codeSpecificHinzufuegenModel.setCode((Code) comboBoxCode.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("xCodeXHinzufuegenController setModelCode RequiredException=" + e.getMessage());
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
                switch (codetyp) {
                    case SCHUELER:
                        onSchuelerCodeHinzufuegen();
                        break;
                    case MITARBEITER:
                        onMitarbeiterCodeHinzufuegen();
                        break;
                }
            }
        });
    }

    private void onSchuelerCodeHinzufuegen() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnOk.setFocusPainted(false);
            return;
        }
        codeSpecificHinzufuegenModel.schuelerCodeHinzufuegen(codesTableModel, schuelerDatenblattModel);
        codeSpecificHinzufuegenDialog.dispose();
    }

    private void onMitarbeiterCodeHinzufuegen() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnOk.setFocusPainted(false);
            return;
        }
        codeSpecificHinzufuegenModel.mitarbeiterCodeHinzufuegen(codesTableModel, mitarbeiterErfassenModel);
        codeSpecificHinzufuegenDialog.dispose();
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
        codeSpecificHinzufuegenDialog.dispose();
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        if (checkIsFieldChange(Field.CODE, evt)) {
            comboBoxCode.setSelectedItem(codeSpecificHinzufuegenModel.getCode());
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
