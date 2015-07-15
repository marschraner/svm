package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CodeSchuelerHinzufuegenModel;
import ch.metzenthin.svm.domain.model.CodesModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.ui.control.CodeSchuelerHinzufuegenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CodeSchuelerHinzufuegenDialog extends JDialog {
    private JPanel contentPane;
    private JPanel dataPanel;
    private JPanel buttonPanel;
    private JComboBox<Code> comboBoxCode;
    private JButton btnOk;
    private JButton btnAbbrechen;

    public CodeSchuelerHinzufuegenDialog(SvmContext svmContext, CodesModel codesModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnOk);
        setTitle("Code hinzufügen");
        createCodeSchuelerHinzufuegenController(svmContext, codesModel, schuelerDatenblattModel);
    }

    private void createCodeSchuelerHinzufuegenController(SvmContext svmContext, CodesModel codesModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        CodeSchuelerHinzufuegenModel codeSchuelerHinzufuegenModel = svmContext.getModelFactory().createCodeSchuelerHinzufuegenModel();
        CodeSchuelerHinzufuegenController codeSchuelerHinzufuegenController = new CodeSchuelerHinzufuegenController(svmContext, codeSchuelerHinzufuegenModel, codesModel, schuelerDatenblattModel);
        codeSchuelerHinzufuegenController.setCodeSchuelerHinzufuegenDialog(this);
        codeSchuelerHinzufuegenController.setContentPane(contentPane);
        codeSchuelerHinzufuegenController.setComboBoxCode(comboBoxCode);
        codeSchuelerHinzufuegenController.setBtnOk(btnOk);
        codeSchuelerHinzufuegenController.setBtnAbbrechen(btnAbbrechen);
    }

    private void createUIComponents() {
        comboBoxCode = new JComboBox<>();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridBagLayout());
        contentPane.add(dataPanel, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        dataPanel.add(panel1, gbc);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Code", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel1.getFont().getName(), Font.BOLD, panel1.getFont().getSize())));
        final JLabel label1 = new JLabel();
        label1.setText("Neuer Code:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 10;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxCode, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        panel1.add(spacer6, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        btnOk = new JButton();
        btnOk.setMaximumSize(new Dimension(114, 29));
        btnOk.setMinimumSize(new Dimension(114, 29));
        btnOk.setPreferredSize(new Dimension(114, 29));
        btnOk.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 10, 5);
        buttonPanel.add(btnOk, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(114, 29));
        btnAbbrechen.setMinimumSize(new Dimension(114, 29));
        btnAbbrechen.setPreferredSize(new Dimension(114, 29));
        btnAbbrechen.setSelected(true);
        btnAbbrechen.setText("Abbrechen");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 10, 5);
        buttonPanel.add(btnAbbrechen, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
