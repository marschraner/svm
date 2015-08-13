package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.domain.model.CodeErfassenModel;
import ch.metzenthin.svm.domain.model.CodesModel;
import ch.metzenthin.svm.ui.control.CodeErfassenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CodeErfassenDialog extends JDialog {
    private JPanel contentPane;
    private JPanel datenPanel;
    private JTextField txtKuerzel;
    private JLabel errLblKuerzel;
    private JTextField txtBeschreibung;
    private JLabel errLblBeschreibung;
    private JButton btnSpeichern;
    private JPanel buttonPanel;
    private JButton btnAbbrechen;

    public CodeErfassenDialog(SvmContext svmContext, CodesModel codesModel, int indexBearbeiten, boolean isBearbeiten, String title, Codetyp codetyp) {
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        initializeErrLbls();
        createCodeErfassenController(svmContext, codesModel, indexBearbeiten, isBearbeiten, codetyp);
    }

    private void createCodeErfassenController(SvmContext svmContext, CodesModel codesModel, int indexBearbeiten, boolean isBearbeiten, Codetyp codetyp) {
        CodeErfassenModel codeErfassenModel = (isBearbeiten ? codesModel.getCodeErfassenModel(svmContext, indexBearbeiten, codetyp) : svmContext.getModelFactory().createCodeErfassenModel());
        CodeErfassenController codeErfassenController = new CodeErfassenController(svmContext, codeErfassenModel, codetyp);
        codeErfassenController.setCodeErfassenDialog(this);
        codeErfassenController.setContentPane(contentPane);
        codeErfassenController.setTxtKuerzel(txtKuerzel);
        codeErfassenController.setTxtBeschreibung(txtBeschreibung);
        codeErfassenController.setBtnSpeichern(btnSpeichern);
        codeErfassenController.setBtnAbbrechen(btnAbbrechen);
        codeErfassenController.setErrLblKuerzel(errLblKuerzel);
        codeErfassenController.setErrLblBeschreibung(errLblBeschreibung);
        codeErfassenController.constructionDone();
    }

    private void initializeErrLbls() {
        errLblKuerzel.setVisible(false);
        errLblKuerzel.setForeground(Color.RED);
        errLblBeschreibung.setVisible(false);
        errLblBeschreibung.setForeground(Color.RED);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Code", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel1.getFont().getName(), Font.BOLD, panel1.getFont().getSize())));
        final JLabel label1 = new JLabel();
        label1.setText("Kürzel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 300;
        panel1.add(spacer5, gbc);
        txtKuerzel = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtKuerzel, gbc);
        errLblKuerzel = new JLabel();
        errLblKuerzel.setText("errLblKuerzel");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblKuerzel, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Beschreibung");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        txtBeschreibung = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBeschreibung, gbc);
        errLblBeschreibung = new JLabel();
        errLblBeschreibung.setText("errLblBeschreibung");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBeschreibung, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel1.add(spacer7, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        btnSpeichern = new JButton();
        btnSpeichern.setMaximumSize(new Dimension(114, 29));
        btnSpeichern.setMinimumSize(new Dimension(114, 29));
        btnSpeichern.setPreferredSize(new Dimension(114, 29));
        btnSpeichern.setText("Speichern");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 10, 5);
        buttonPanel.add(btnSpeichern, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(114, 29));
        btnAbbrechen.setMinimumSize(new Dimension(114, 29));
        btnAbbrechen.setPreferredSize(new Dimension(114, 29));
        btnAbbrechen.setText("Abbrechen");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 10, 5);
        buttonPanel.add(btnAbbrechen, gbc);
        label1.setLabelFor(txtKuerzel);
        label2.setLabelFor(txtBeschreibung);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
