package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Codetyp;
import ch.metzenthin.svm.domain.model.CodeSpecificHinzufuegenModel;
import ch.metzenthin.svm.domain.model.CodesModel;
import ch.metzenthin.svm.domain.model.MitarbeiterErfassenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import ch.metzenthin.svm.ui.control.CodeSpecificHinzufuegenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

@SuppressWarnings({"java:S100", "java:S1450"})
public class CodeSpecificHinzufuegenDialog extends JDialog {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = false;

    private JPanel contentPane;
    private JPanel dataPanel;
    private JPanel buttonPanel;
    private JComboBox<Code> comboBoxCode;
    private JButton btnOk;
    private JButton btnAbbrechen;
    private JLabel errLblCode;

    public CodeSpecificHinzufuegenDialog(SvmContext svmContext, CodesTableModel codesTableModel, CodesModel codesModel, SchuelerDatenblattModel schuelerDatenblattModel, MitarbeiterErfassenModel mitarbeiterErfassenModel, Codetyp codetyp) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        setTitle("Schüler-Code hinzufügen");
        initializeErrLbls();
        if (DEFAULT_BUTTON_ENABLED) {
            getRootPane().setDefaultButton(btnOk);
        }
        createCodeSchuelerHinzufuegenController(svmContext, codesTableModel, codesModel, schuelerDatenblattModel, mitarbeiterErfassenModel, codetyp);
    }

    private void createCodeSchuelerHinzufuegenController(SvmContext svmContext, CodesTableModel codesTableModel, CodesModel codesModel, SchuelerDatenblattModel schuelerDatenblattModel, MitarbeiterErfassenModel mitarbeiterErfassenModel, Codetyp codetyp) {
        CodeSpecificHinzufuegenModel codeSpecificHinzufuegenModel = svmContext.getModelFactory().createCodeSchuelerHinzufuegenModel();
        CodeSpecificHinzufuegenController codeSpecificHinzufuegenController = new CodeSpecificHinzufuegenController(svmContext, codesTableModel, codeSpecificHinzufuegenModel, codesModel, schuelerDatenblattModel, mitarbeiterErfassenModel, codetyp);
        codeSpecificHinzufuegenController.setCodeSpecificHinzufuegenDialog(this);
        codeSpecificHinzufuegenController.setContentPane(contentPane);
        codeSpecificHinzufuegenController.setComboBoxCode(comboBoxCode);
        codeSpecificHinzufuegenController.setErrLblCode(errLblCode);
        codeSpecificHinzufuegenController.setBtnOk(btnOk);
        codeSpecificHinzufuegenController.setBtnAbbrechen(btnAbbrechen);
    }

    private void initializeErrLbls() {
        errLblCode.setVisible(false);
        errLblCode.setForeground(Color.RED);
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Code", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont()), null));
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
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        panel1.add(spacer6, gbc);
        errLblCode = new JLabel();
        errLblCode.setText("errLblCode");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblCode, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        btnOk = new JButton();
        btnOk.setMaximumSize(new Dimension(114, 29));
        btnOk.setMinimumSize(new Dimension(114, 29));
        btnOk.setPreferredSize(new Dimension(114, 29));
        btnOk.setText("OK");
        btnOk.setMnemonic('O');
        btnOk.setDisplayedMnemonicIndex(0);
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
        btnAbbrechen.setMnemonic('A');
        btnAbbrechen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 10, 5);
        buttonPanel.add(btnAbbrechen, gbc);
        label1.setLabelFor(comboBoxCode);
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
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
