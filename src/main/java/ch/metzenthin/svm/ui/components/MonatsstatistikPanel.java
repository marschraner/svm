package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.utils.Converter;
import ch.metzenthin.svm.domain.model.MonatsstatistikModel;
import ch.metzenthin.svm.ui.control.MonatsstatistikController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikPanel {

    private JPanel panel;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private JLabel lblMonatJahr;
    private JTextField txtMonatJahr;
    private JRadioButton radioBtnAnmeldungen;
    private JRadioButton radioBtnAbmeldungen;
    private JRadioButton radioBtnDispensationen;
    private JLabel errLblMonatJahr;
    private JPanel titelPanel;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private MonatsstatistikController monatsstatistikController;
    private MonatsstatistikModel monatsstatistikModel;
    private ActionListener nextPanelListener;

    public MonatsstatistikPanel(SvmContext svmContext) {
        $$$setupUI$$$();
        monatsstatistikModel = svmContext.getModelFactory().createMonatsstatistikModel();
        initializeTxtFields();
        initializeRadioButtonGroups();
        initializeErrLbls();
        btnSuchen.setEnabled(true);
        createMonatsstatistikController(svmContext);
    }

    private void initializeTxtFields() {
        txtMonatJahr.setText(Converter.asString(monatsstatistikModel.getMonatJahrInit(), MonatsstatistikModel.MONAT_JAHR_DATE_FORMAT_STRING));
    }

    private void initializeRadioButtonGroups() {
        switch (monatsstatistikModel.getAnAbmeldungenDispensationenInit()) {
            case ANMELDUNGEN:
                radioBtnAnmeldungen.setSelected(true);
                break;
            case ABMELDUNGEN:
                radioBtnAbmeldungen.setSelected(true);
                break;
            case DISPENSATIONEN:
                radioBtnDispensationen.setSelected(true);
                break;
        }
    }

    private void initializeErrLbls() {
        errLblMonatJahr.setVisible(false);
        errLblMonatJahr.setForeground(Color.RED);
    }

    private void createMonatsstatistikController(SvmContext svmContext) {
        monatsstatistikController = new MonatsstatistikController(svmContext, monatsstatistikModel);
        monatsstatistikController.setTxtMonatJahr(txtMonatJahr);
        monatsstatistikController.setRadioBtnGroupAnAbmeldungenDispensationen(radioBtnAnmeldungen, radioBtnAbmeldungen, radioBtnDispensationen);
        monatsstatistikController.setBtnSuchen(btnSuchen);
        monatsstatistikController.setBtnAbbrechen(btnAbbrechen);
        monatsstatistikController.setErrLblMonatJahr(errLblMonatJahr);
        monatsstatistikController.addZurueckListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        });
    }

    private void onZurueck() {
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{$$$getRootComponent$$$(), "Schüler suchen"}, ActionEvent.ACTION_PERFORMED, "Zurück zu Schüler suchen"));
    }

    public void addCloseListener(ActionListener actionListener) {
        monatsstatistikController.addCloseListener(actionListener);
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
        monatsstatistikController.addNextPanelListener(nextPanelListener);
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Monatsstatistik", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel1.getFont().getName(), Font.BOLD, panel1.getFont().getSize()), new Color(-16777216)));
        lblMonatJahr = new JLabel();
        lblMonatJahr.setText("Monat/Jahr (MM.JJJJ)");
        lblMonatJahr.setDisplayedMnemonic('M');
        lblMonatJahr.setDisplayedMnemonicIndex(0);
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
        radioBtnAnmeldungen = new JRadioButton();
        radioBtnAnmeldungen.setText("Anmeldungen");
        radioBtnAnmeldungen.setMnemonic('N');
        radioBtnAnmeldungen.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel1.add(radioBtnAnmeldungen, gbc);
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
        gbc.gridy = 7;
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
        radioBtnAbmeldungen = new JRadioButton();
        radioBtnAbmeldungen.setText("Abmeldungen");
        radioBtnAbmeldungen.setMnemonic('B');
        radioBtnAbmeldungen.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(radioBtnAbmeldungen, gbc);
        radioBtnDispensationen = new JRadioButton();
        radioBtnDispensationen.setText("Dispensationen");
        radioBtnDispensationen.setMnemonic('D');
        radioBtnDispensationen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(radioBtnDispensationen, gbc);
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
        label1.setFont(new Font(label1.getFont().getName(), label1.getFont().getStyle(), 36));
        label1.setText("Monatsstatistik");
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
        buttonGroup.add(radioBtnAnmeldungen);
        buttonGroup.add(radioBtnAbmeldungen);
        buttonGroup.add(radioBtnDispensationen);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
