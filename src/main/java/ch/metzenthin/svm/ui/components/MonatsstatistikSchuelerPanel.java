package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.MonatsstatistikSchuelerModel;
import ch.metzenthin.svm.ui.control.MonatsstatistikSchuelerController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikSchuelerPanel {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = true;

    private final SvmContext svmContext;
    private JPanel panel;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private JLabel lblMonatJahr;
    private JTextField txtMonatJahr;
    private JRadioButton radioBtnAnmeldungenKindertheater;
    private JRadioButton radioBtnAbmeldungenKindertheater;
    private JRadioButton radioBtnAnmeldungenKurse;
    private JRadioButton radioBtnAbmeldungenKurse;
    private JRadioButton radioBtnDispensationen;
    private JLabel errLblMonatJahr;
    private JPanel titelPanel;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private MonatsstatistikSchuelerController monatsstatistikSchuelerController;
    private MonatsstatistikSchuelerModel monatsstatistikSchuelerModel;
    private ActionListener nextPanelListener;

    MonatsstatistikSchuelerPanel(SvmContext svmContext) {
        this.svmContext = svmContext;
        $$$setupUI$$$();
        monatsstatistikSchuelerModel = svmContext.getModelFactory().createMonatsstatistikSchuelerModel();
        initializeErrLbls();
        btnSuchen.setEnabled(true);
        setDefaultButton();
        createMonatsstatistikSchuelerController(svmContext);
    }

    private void initializeErrLbls() {
        errLblMonatJahr.setVisible(false);
        errLblMonatJahr.setForeground(Color.RED);
    }

    private void createMonatsstatistikSchuelerController(SvmContext svmContext) {
        monatsstatistikSchuelerController = new MonatsstatistikSchuelerController(svmContext, monatsstatistikSchuelerModel, DEFAULT_BUTTON_ENABLED);
        monatsstatistikSchuelerController.setTxtMonatJahr(txtMonatJahr);
        monatsstatistikSchuelerController.setRadioBtnGroupAnAbmeldungenDispensationen(radioBtnAnmeldungenKindertheater, radioBtnAbmeldungenKindertheater, radioBtnAnmeldungenKurse, radioBtnAbmeldungenKurse, radioBtnDispensationen);
        monatsstatistikSchuelerController.setBtnSuchen(btnSuchen);
        monatsstatistikSchuelerController.setBtnAbbrechen(btnAbbrechen);
        monatsstatistikSchuelerController.setErrLblMonatJahr(errLblMonatJahr);
        monatsstatistikSchuelerController.addZurueckListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        });
    }

    private void onZurueck() {
        setDefaultButton();
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{$$$getRootComponent$$$(), "Schüler suchen"}, ActionEvent.ACTION_PERFORMED, "Zurück zu Schüler suchen"));
    }

    public void addCloseListener(ActionListener actionListener) {
        monatsstatistikSchuelerController.addCloseListener(actionListener);
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
        monatsstatistikSchuelerController.addNextPanelListener(nextPanelListener);
    }

    private void setDefaultButton() {
        if (DEFAULT_BUTTON_ENABLED) {
            svmContext.getRootPaneJFrame().setDefaultButton(btnSuchen);
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        datenPanel = new JPanel();
        datenPanel.setLayout(new GridBagLayout());
        panel.add(datenPanel, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        datenPanel.add(panel1, gbc);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Monatsstatistik", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont()), new Color(-16777216)));
        lblMonatJahr = new JLabel();
        lblMonatJahr.setText("Monat/Jahr (MM.JJJJ)");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(lblMonatJahr, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer1, gbc);
        txtMonatJahr = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtMonatJahr, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 200;
        panel1.add(spacer4, gbc);
        radioBtnAnmeldungenKindertheater = new JRadioButton();
        radioBtnAnmeldungenKindertheater.setText("Anmeldungen Kindertheater");
        radioBtnAnmeldungenKindertheater.setMnemonic('N');
        radioBtnAnmeldungenKindertheater.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel1.add(radioBtnAnmeldungenKindertheater, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 300;
        panel1.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer7, gbc);
        errLblMonatJahr = new JLabel();
        errLblMonatJahr.setText("errLblMonatJahr");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblMonatJahr, gbc);
        radioBtnAbmeldungenKindertheater = new JRadioButton();
        radioBtnAbmeldungenKindertheater.setText("Abmeldungen Kindertheater");
        radioBtnAbmeldungenKindertheater.setMnemonic('B');
        radioBtnAbmeldungenKindertheater.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(radioBtnAbmeldungenKindertheater, gbc);
        radioBtnDispensationen = new JRadioButton();
        radioBtnDispensationen.setText("Dispensationen");
        radioBtnDispensationen.setMnemonic('D');
        radioBtnDispensationen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(radioBtnDispensationen, gbc);
        radioBtnAbmeldungenKurse = new JRadioButton();
        radioBtnAbmeldungenKurse.setText("Abmeldungen Kurse");
        radioBtnAbmeldungenKurse.setMnemonic('E');
        radioBtnAbmeldungenKurse.setDisplayedMnemonicIndex(3);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(radioBtnAbmeldungenKurse, gbc);
        radioBtnAnmeldungenKurse = new JRadioButton();
        radioBtnAnmeldungenKurse.setText("Anmeldungen Kurse");
        radioBtnAnmeldungenKurse.setMnemonic('M');
        radioBtnAnmeldungenKurse.setDisplayedMnemonicIndex(2);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(radioBtnAnmeldungenKurse, gbc);
        titelPanel = new JPanel();
        titelPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 20, 10);
        datenPanel.add(titelPanel, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 36, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Monatsstatistik Schüler");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        titelPanel.add(label1, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titelPanel.add(spacer8, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        panel.add(buttonPanel, BorderLayout.SOUTH);
        btnSuchen = new JButton();
        btnSuchen.setMaximumSize(new Dimension(114, 29));
        btnSuchen.setMinimumSize(new Dimension(114, 29));
        btnSuchen.setPreferredSize(new Dimension(114, 29));
        btnSuchen.setSelected(true);
        btnSuchen.setText("Suchen");
        btnSuchen.setMnemonic('S');
        btnSuchen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(btnSuchen, gbc);
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
        gbc.anchor = GridBagConstraints.WEST;
        buttonPanel.add(btnAbbrechen, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer11, gbc);
        lblMonatJahr.setLabelFor(txtMonatJahr);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioBtnAnmeldungenKindertheater);
        buttonGroup.add(radioBtnAbmeldungenKindertheater);
        buttonGroup.add(radioBtnDispensationen);
        buttonGroup.add(radioBtnAnmeldungenKurse);
        buttonGroup.add(radioBtnAbmeldungenKurse);
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
        return panel;
    }

}
