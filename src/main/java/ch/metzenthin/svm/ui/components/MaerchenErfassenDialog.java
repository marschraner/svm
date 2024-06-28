package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.domain.model.MaerchenErfassenModel;
import ch.metzenthin.svm.domain.model.MaerchensModel;
import ch.metzenthin.svm.ui.componentmodel.MaerchensTableModel;
import ch.metzenthin.svm.ui.control.MaerchenErfassenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class MaerchenErfassenDialog extends JDialog {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = false;

    private JPanel contentPane;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JSpinner spinnerSchuljahre;
    private JTextField txtBezeichnung;
    private JTextField txtAnzahlVorstellungen;
    private JLabel errLblBezeichnung;
    private JLabel errLblAnzahlVorstellungen;
    private JButton btnSpeichern;
    private JButton btnAbbrechen;

    public MaerchenErfassenDialog(SvmContext svmContext, MaerchensTableModel maerchensTableModel, MaerchensModel maerchensModel, int indexBearbeiten, boolean isBearbeiten, String title) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        initializeErrLbls();
        if (DEFAULT_BUTTON_ENABLED) {
            getRootPane().setDefaultButton(btnSpeichern);
        }
        createMaerchenErfassenController(svmContext, maerchensTableModel, maerchensModel, indexBearbeiten, isBearbeiten);
    }

    private void createMaerchenErfassenController(SvmContext svmContext, MaerchensTableModel maerchensTableModel, MaerchensModel maerchensModel, int indexBearbeiten, boolean isBearbeiten) {
        MaerchenErfassenModel maerchenErfassenModel = (isBearbeiten ? maerchensModel.getMaerchenErfassenModel(svmContext, indexBearbeiten) : svmContext.getModelFactory().createMaerchenErfassenModel());
        MaerchenErfassenController maerchenErfassenController = new MaerchenErfassenController(svmContext, maerchensTableModel, maerchenErfassenModel, isBearbeiten, DEFAULT_BUTTON_ENABLED);
        maerchenErfassenController.setMaerchenErfassenDialog(this);
        maerchenErfassenController.setContentPane(contentPane);
        maerchenErfassenController.setSpinnerSchuljahre(spinnerSchuljahre);
        maerchenErfassenController.setTxtBezeichnung(txtBezeichnung);
        maerchenErfassenController.setTxtAnzahlVorstellungen(txtAnzahlVorstellungen);
        maerchenErfassenController.setBtnSpeichern(btnSpeichern);
        maerchenErfassenController.setBtnAbbrechen(btnAbbrechen);
        maerchenErfassenController.setErrLblBezeichnung(errLblBezeichnung);
        maerchenErfassenController.setErrLblAnzahlVorstellungen(errLblAnzahlVorstellungen);
        maerchenErfassenController.constructionDone();
    }

    private void initializeErrLbls() {
        errLblBezeichnung.setVisible(false);
        errLblBezeichnung.setForeground(Color.RED);
        errLblAnzahlVorstellungen.setVisible(false);
        errLblAnzahlVorstellungen.setForeground(Color.RED);
    }

    private void createUIComponents() {
        String[] schuljahre = new Schuljahre().getSchuljahre();
        SpinnerModel spinnerModelSchuljahre = new SpinnerListModel(schuljahre);
        spinnerSchuljahre = new JSpinner(spinnerModelSchuljahre);
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Märchen", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont()), new Color(-16777216)));
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
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spinnerSchuljahre, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 300;
        panel1.add(spacer5, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Bezeichnung");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        txtBezeichnung = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBezeichnung, gbc);
        errLblBezeichnung = new JLabel();
        errLblBezeichnung.setText("errLblBezeichnung");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBezeichnung, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer6, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Anzahl Vorstellungen");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);
        panel1.add(label3, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer7, gbc);
        txtAnzahlVorstellungen = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtAnzahlVorstellungen, gbc);
        errLblAnzahlVorstellungen = new JLabel();
        errLblAnzahlVorstellungen.setText("errLblAnzahlVorstellungen");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblAnzahlVorstellungen, gbc);
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
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer10, gbc);
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
        label2.setLabelFor(txtBezeichnung);
        errLblAnzahlVorstellungen.setLabelFor(txtAnzahlVorstellungen);
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
