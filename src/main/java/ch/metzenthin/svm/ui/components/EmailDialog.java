package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.EmailEmpfaenger;
import ch.metzenthin.svm.domain.model.EmailModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.control.EmailController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

@SuppressWarnings({"java:S100", "java:S1450"})
public class EmailDialog extends JDialog {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = false;

    private JPanel contentPane;
    private JPanel datenPanel;
    private JComboBox<EmailEmpfaenger> comboBoxEmailEmpfaenger;
    private JPanel buttonPanel;
    private JButton btnOk;
    private JButton btnAbbrechen;

    public EmailDialog(SvmContext svmContext, SchuelerDatenblattModel schuelerDatenblattModel) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        setTitle("Wahl des E-Mail-Empfängers");
        if (DEFAULT_BUTTON_ENABLED) {
            getRootPane().setDefaultButton(btnOk);
        }
        createEmailController(svmContext, schuelerDatenblattModel);
    }

    private void createEmailController(SvmContext svmContext, SchuelerDatenblattModel schuelerDatenblattModel) {
        EmailModel emailModel = svmContext.getModelFactory().createEmailModel();
        EmailController emailController = new EmailController(emailModel, schuelerDatenblattModel);
        emailController.setEmailDialog(this);
        emailController.setContentPane(contentPane);
        emailController.setComboBoxEmailEmpfaenger(comboBoxEmailEmpfaenger);
        emailController.setBtnOk(btnOk);
        emailController.setBtnAbbrechen(btnAbbrechen);
        emailController.constructionDone();
    }

    private void createUIComponents() {
        comboBoxEmailEmpfaenger = new JComboBox<>(EmailEmpfaenger.values());

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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Email-Empfänger", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont()), new Color(-16777216)));
        final JLabel label1 = new JLabel();
        label1.setText("Empfänger:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);
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
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxEmailEmpfaenger, gbc);
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
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        panel1.add(spacer5, gbc);
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
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnOk, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer8, gbc);
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
