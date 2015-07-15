package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CodeSchuelerHinzufuegenModel;
import ch.metzenthin.svm.domain.model.CodesModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.Code;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public class CodeSchuelerHinzufuegenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(CodeSchuelerHinzufuegenController.class);
    private final SvmContext svmContext;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private CodeSchuelerHinzufuegenModel codeSchuelerHinzufuegenModel;
    private CodesModel codesModel;
    private JDialog codeSchuelerHinzufuegenDialog;
    private JComboBox<Code> comboBoxCode;

    public CodeSchuelerHinzufuegenController(SvmContext svmContext, CodeSchuelerHinzufuegenModel codeSchuelerHinzufuegenModel, CodesModel codesModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        super(codeSchuelerHinzufuegenModel);
        this.svmContext = svmContext;
        this.codesModel = codesModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.codeSchuelerHinzufuegenModel = codeSchuelerHinzufuegenModel;
        this.codeSchuelerHinzufuegenModel.addPropertyChangeListener(this);
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

    public void setComboBoxCode(JComboBox<Code> comboBoxCode) {
        this.comboBoxCode = comboBoxCode;
        Code[] selectableCodes = codesModel.getSelectableCodes(svmContext.getSvmModel(), schuelerDatenblattModel);
        comboBoxCode.setModel(new DefaultComboBoxModel<>(selectableCodes));
        // Code in Model initialisieren mit erstem ComboBox-Wert
        codeSchuelerHinzufuegenModel.setCode(selectableCodes[0]);
        this.comboBoxCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCodeSelected();
            }
        });
    }

    private void onCodeSelected() {
        LOGGER.trace("CodeSchuelerHinzufuegenController Event Code selected=" + comboBoxCode.getSelectedItem());
        setModelCombobox();
    }

    private void setModelCombobox() {
        codeSchuelerHinzufuegenModel.setCode((Code) comboBoxCode.getSelectedItem());
    }

    public void setBtnOk(JButton btnOk) {
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSpeichern();
            }
        });
    }

    private void onSpeichern() {
        codeSchuelerHinzufuegenModel.hinzufuegen(schuelerDatenblattModel);
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
    void doPropertyChange(PropertyChangeEvent evt) {}

    @Override
    void validateFields() throws SvmValidationException {}

    @Override
    void showErrMsg(SvmValidationException e) {}

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {}

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {}

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}


}
