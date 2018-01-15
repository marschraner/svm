package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Schuljahre;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.model.SemesterErfassenModel;
import ch.metzenthin.svm.domain.model.SemestersModel;
import ch.metzenthin.svm.ui.componentmodel.SemestersTableModel;
import ch.metzenthin.svm.ui.control.SemesterErfassenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SemesterErfassenDialog extends JDialog {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = false;

    private JPanel contentPane;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JSpinner spinnerSchuljahre;
    private JComboBox<Semesterbezeichnung> comboBoxSemesterbezeichnung;
    private JTextField txtSemesterbeginn;
    private JTextField txtSemesterende;
    private JTextField txtFerienbeginn1;
    private JTextField txtFerienende1;
    private JTextField txtFerienbeginn2;
    private JTextField txtFerienende2;
    private JLabel errLblSemesterende;
    private JLabel errLblSemesterbeginn;
    private JLabel errLblFerienbeginn1;
    private JLabel errLblFerienende1;
    private JLabel errLblFerienbeginn2;
    private JLabel errLblFerienende2;
    private JButton btnSpeichern;
    private JButton btnAbbrechen;

    public SemesterErfassenDialog(SvmContext svmContext, SemestersTableModel semestersTableModel, SemestersModel semestersModel, int indexBearbeiten, boolean isBearbeiten, String title) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        initializeErrLbls();
        if (DEFAULT_BUTTON_ENABLED) {
            getRootPane().setDefaultButton(btnSpeichern);
        }
        createSemesterErfassenController(svmContext, semestersTableModel, semestersModel, indexBearbeiten, isBearbeiten);
    }

    private void createSemesterErfassenController(SvmContext svmContext, SemestersTableModel semestersTableModel, SemestersModel semestersModel, int indexBearbeiten, boolean isBearbeiten) {
        SemesterErfassenModel semesterErfassenModel = (isBearbeiten ? semestersModel.getSemesterErfassenModel(svmContext, indexBearbeiten) : svmContext.getModelFactory().createSemesterErfassenModel());
        SemesterErfassenController semesterErfassenController = new SemesterErfassenController(svmContext, semestersTableModel, semesterErfassenModel, isBearbeiten, DEFAULT_BUTTON_ENABLED);
        semesterErfassenController.setSemesterErfassenDialog(this);
        semesterErfassenController.setContentPane(contentPane);
        semesterErfassenController.setSpinnerSchuljahre(spinnerSchuljahre);
        semesterErfassenController.setComboBoxSemesterbezeichnung(comboBoxSemesterbezeichnung);
        semesterErfassenController.setTxtSemesterbeginn(txtSemesterbeginn);
        semesterErfassenController.setTxtSemesterende(txtSemesterende);
        semesterErfassenController.setTxtFerienbeginn1(txtFerienbeginn1);
        semesterErfassenController.setTxtFerienende1(txtFerienende1);
        semesterErfassenController.setTxtFerienbeginn2(txtFerienbeginn2);
        semesterErfassenController.setTxtFerienende2(txtFerienende2);
        semesterErfassenController.setBtnSpeichern(btnSpeichern);
        semesterErfassenController.setBtnAbbrechen(btnAbbrechen);
        semesterErfassenController.setErrLblSemesterbeginn(errLblSemesterbeginn);
        semesterErfassenController.setErrLblSemesterende(errLblSemesterende);
        semesterErfassenController.setErrLblFerienbeginn1(errLblFerienbeginn1);
        semesterErfassenController.setErrLblFerienende1(errLblFerienende1);
        semesterErfassenController.setErrLblFerienbeginn2(errLblFerienbeginn2);
        semesterErfassenController.setErrLblFerienende2(errLblFerienende2);
        semesterErfassenController.constructionDone();
    }

    private void initializeErrLbls() {
        errLblSemesterbeginn.setVisible(false);
        errLblSemesterbeginn.setForeground(Color.RED);
        errLblSemesterende.setVisible(false);
        errLblSemesterende.setForeground(Color.RED);
        errLblFerienbeginn1.setVisible(false);
        errLblFerienbeginn1.setForeground(Color.RED);
        errLblFerienende1.setVisible(false);
        errLblFerienende1.setForeground(Color.RED);
        errLblFerienbeginn2.setVisible(false);
        errLblFerienbeginn2.setForeground(Color.RED);
        errLblFerienende2.setVisible(false);
        errLblFerienende2.setForeground(Color.RED);
    }

    private void createUIComponents() {
        String[] schuljahre = new Schuljahre().getSchuljahre();
        SpinnerModel spinnerModelSchuljahre = new SpinnerListModel(schuljahre);
        spinnerSchuljahre = new JSpinner(spinnerModelSchuljahre);
        comboBoxSemesterbezeichnung = new JComboBox<>(Semesterbezeichnung.values());
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
        datenPanel = new JPanel();
        datenPanel.setLayout(new GridBagLayout());
        contentPane.add(datenPanel, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        datenPanel.add(panel1, gbc);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Semester", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont())));
        final JLabel label1 = new JLabel();
        label1.setText("Schuljahr");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
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
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spinnerSchuljahre, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Semester");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxSemesterbezeichnung, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer5, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Semesterbeginn");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);
        panel1.add(label3, gbc);
        txtSemesterbeginn = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtSemesterbeginn, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 300;
        panel1.add(spacer6, gbc);
        errLblSemesterbeginn = new JLabel();
        errLblSemesterbeginn.setText("errLblSemesterbeginn");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblSemesterbeginn, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer7, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Semesterende");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label4, gbc);
        txtSemesterende = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtSemesterende, gbc);
        errLblSemesterende = new JLabel();
        errLblSemesterende.setText("errLblSemesterende");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblSemesterende, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer8, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Ferienbeginn 1");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label5, gbc);
        txtFerienbeginn1 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtFerienbeginn1, gbc);
        errLblFerienbeginn1 = new JLabel();
        errLblFerienbeginn1.setText("errLblFerienbeginn1");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblFerienbeginn1, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer9, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Ferienende 1");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label6, gbc);
        txtFerienende1 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtFerienende1, gbc);
        errLblFerienende1 = new JLabel();
        errLblFerienende1.setText("errLblFerienende1");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblFerienende1, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer10, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Ferienbeginn 2");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label7, gbc);
        txtFerienbeginn2 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtFerienbeginn2, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer11, gbc);
        errLblFerienbeginn2 = new JLabel();
        errLblFerienbeginn2.setText("errLblFerienbeginn2");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblFerienbeginn2, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Ferienende 2");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label8, gbc);
        txtFerienende2 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtFerienende2, gbc);
        errLblFerienende2 = new JLabel();
        errLblFerienende2.setText("errLblFerienende2");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblFerienende2, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 16;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer12, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        btnSpeichern = new JButton();
        btnSpeichern.setMaximumSize(new Dimension(114, 29));
        btnSpeichern.setMinimumSize(new Dimension(114, 29));
        btnSpeichern.setPreferredSize(new Dimension(114, 29));
        btnSpeichern.setText("Speichern");
        btnSpeichern.setMnemonic('S');
        btnSpeichern.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnSpeichern, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer13, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer14, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer15, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(114, 29));
        btnAbbrechen.setMinimumSize(new Dimension(114, 29));
        btnAbbrechen.setPreferredSize(new Dimension(114, 29));
        btnAbbrechen.setText("Abbrechen");
        btnAbbrechen.setMnemonic('A');
        btnAbbrechen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnAbbrechen, gbc);
        label1.setLabelFor(spinnerSchuljahre);
        label2.setLabelFor(comboBoxSemesterbezeichnung);
        errLblSemesterbeginn.setLabelFor(txtSemesterbeginn);
        errLblSemesterende.setLabelFor(txtSemesterende);
        label5.setLabelFor(txtFerienbeginn1);
        label6.setLabelFor(txtFerienende1);
        label7.setLabelFor(txtFerienbeginn2);
        label8.setLabelFor(txtFerienende2);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
