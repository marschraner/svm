package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.control.SchuelerDatenblattController;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author Hans Stamm
 */
public class SchuelerDatenblattPanel {
    private JPanel panel1;
    private JLabel lblSchuelerValue;
    private JLabel lblMutterValue;
    private JLabel lblVaterValue;
    private JPanel stammdatenPanel;
    private JPanel buttonPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel lblRechnungsempfaengerValue;
    private JLabel lblGeschwisterValue;
    private JButton btnStammdatenBearbeiten;
    private JPanel titelPanel;
    private JLabel lblAnmeldungValue;
    private JPanel dispensationenPanel;
    private JTable tableDispensationen;
    private JButton btnDispensationBearbeiten;
    private JPanel codesPanel;
    private JTable tableCodes;
    private JButton btnCodesBearbeiten;
    private JPanel datenPanel;
    private JPanel rightButtonPanel;
    private JPanel leftButtonPanel;
    private JButton btnErster;
    private JButton btnVorheriger;
    private JButton btnNachfolgender;
    private JButton btnLetzter;
    private JButton btnAbbrechen;
    private JLabel lblSchuelerGleicherRechnungsempfaenger;
    private JLabel lblSchuelerGleicherRechnungsempfaengerValue;
    private JLabel lblVornameNachname;

    public SchuelerDatenblattPanel(SchuelerDatenblattModel schuelerDatenblattModel) {
        SchuelerDatenblattController schuelerDatenblattController = new SchuelerDatenblattController(schuelerDatenblattModel);
        schuelerDatenblattController.setLabelVornameNachname(lblVornameNachname);
        schuelerDatenblattController.setLabelSchuelerValue(lblSchuelerValue);
        schuelerDatenblattController.setLabelMutterValue(lblMutterValue);
        schuelerDatenblattController.setLabelVaterValue(lblVaterValue);
        schuelerDatenblattController.setLabelRechnungsempfaengerValue(lblRechnungsempfaengerValue);
        schuelerDatenblattController.setLabelGeschwisterValue(lblGeschwisterValue);
        schuelerDatenblattController.setLabelSchuelerGleicherRechnungsempfaengerValue(lblSchuelerGleicherRechnungsempfaengerValue);
        schuelerDatenblattController.setLabelAnmeldungValue(lblAnmeldungValue);
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
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(buttonPanel, BorderLayout.SOUTH);
        rightButtonPanel = new JPanel();
        rightButtonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(rightButtonPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNachfolgender = new JButton();
        btnNachfolgender.setPreferredSize(new Dimension(143, 29));
        btnNachfolgender.setText("Nachfolgender");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightButtonPanel.add(btnNachfolgender, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightButtonPanel.add(spacer1, gbc);
        btnLetzter = new JButton();
        btnLetzter.setPreferredSize(new Dimension(143, 29));
        btnLetzter.setText("Letzter");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightButtonPanel.add(btnLetzter, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightButtonPanel.add(spacer2, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setPreferredSize(new Dimension(143, 29));
        btnAbbrechen.setText("Abbrechen");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        rightButtonPanel.add(btnAbbrechen, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        rightButtonPanel.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        rightButtonPanel.add(spacer4, gbc);
        leftButtonPanel = new JPanel();
        leftButtonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(leftButtonPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnErster = new JButton();
        btnErster.setPreferredSize(new Dimension(143, 29));
        btnErster.setText("Erster");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        leftButtonPanel.add(btnErster, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        leftButtonPanel.add(spacer5, gbc);
        btnVorheriger = new JButton();
        btnVorheriger.setMaximumSize(new Dimension(143, 29));
        btnVorheriger.setMinimumSize(new Dimension(143, 29));
        btnVorheriger.setPreferredSize(new Dimension(143, 29));
        btnVorheriger.setText("Vorheriger");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        leftButtonPanel.add(btnVorheriger, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        leftButtonPanel.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        leftButtonPanel.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        leftButtonPanel.add(spacer8, gbc);
        datenPanel = new JPanel();
        datenPanel.setLayout(new GridBagLayout());
        panel1.add(datenPanel, BorderLayout.CENTER);
        stammdatenPanel = new JPanel();
        stammdatenPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 10, 10);
        datenPanel.add(stammdatenPanel, gbc);
        stammdatenPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Stammdaten", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(stammdatenPanel.getFont().getName(), Font.BOLD, stammdatenPanel.getFont().getSize()), new Color(-16777216)));
        final JLabel label1 = new JLabel();
        label1.setText("Schüler:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(label1, gbc);
        lblSchuelerValue = new JLabel();
        lblSchuelerValue.setText("SchuelerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        stammdatenPanel.add(lblSchuelerValue, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Mutter:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(label2, gbc);
        lblMutterValue = new JLabel();
        lblMutterValue.setText("MutterValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(lblMutterValue, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Vater:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(label3, gbc);
        lblVaterValue = new JLabel();
        lblVaterValue.setText("VaterValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(lblVaterValue, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        stammdatenPanel.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        stammdatenPanel.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        stammdatenPanel.add(spacer11, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        stammdatenPanel.add(spacer12, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        stammdatenPanel.add(spacer13, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Rechnungsempfänger:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(label4, gbc);
        lblRechnungsempfaengerValue = new JLabel();
        lblRechnungsempfaengerValue.setText("RechnungsempfaengerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(lblRechnungsempfaengerValue, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        stammdatenPanel.add(spacer14, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Geschwister:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(label5, gbc);
        lblGeschwisterValue = new JLabel();
        lblGeschwisterValue.setText("GeschwisterValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(lblGeschwisterValue, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.VERTICAL;
        stammdatenPanel.add(spacer15, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.BOTH;
        stammdatenPanel.add(panel2, gbc);
        btnStammdatenBearbeiten = new JButton();
        btnStammdatenBearbeiten.setText("Bearbeiten");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel2.add(btnStammdatenBearbeiten, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer16, gbc);
        final JPanel spacer17 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.fill = GridBagConstraints.VERTICAL;
        stammdatenPanel.add(spacer17, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Anmeldedatum:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(label6, gbc);
        lblAnmeldungValue = new JLabel();
        lblAnmeldungValue.setText("AnmeldungValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(lblAnmeldungValue, gbc);
        lblSchuelerGleicherRechnungsempfaenger = new JLabel();
        lblSchuelerGleicherRechnungsempfaenger.setText("Andere Schüler mit gleichem Rechnungsempfänger:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 10);
        stammdatenPanel.add(lblSchuelerGleicherRechnungsempfaenger, gbc);
        final JPanel spacer18 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        stammdatenPanel.add(spacer18, gbc);
        lblSchuelerGleicherRechnungsempfaengerValue = new JLabel();
        lblSchuelerGleicherRechnungsempfaengerValue.setText("SchuelerGleicherRechnungsempfaengerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        stammdatenPanel.add(lblSchuelerGleicherRechnungsempfaengerValue, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        datenPanel.add(panel3, gbc);
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        panel3.add(leftPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        dispensationenPanel = new JPanel();
        dispensationenPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 10, 0);
        leftPanel.add(dispensationenPanel, gbc);
        dispensationenPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Dispensationen", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(dispensationenPanel.getFont().getName(), Font.BOLD, dispensationenPanel.getFont().getSize()), new Color(-16777216)));
        final JPanel spacer19 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dispensationenPanel.add(spacer19, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dispensationenPanel.add(panel4, gbc);
        btnDispensationBearbeiten = new JButton();
        btnDispensationBearbeiten.setText("Bearbeiten");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel4.add(btnDispensationBearbeiten, gbc);
        final JPanel spacer20 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer20, gbc);
        final JPanel spacer21 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        dispensationenPanel.add(spacer21, gbc);
        final JPanel spacer22 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        dispensationenPanel.add(spacer22, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setPreferredSize(new Dimension(23, 54));
        scrollPane1.setVerifyInputWhenFocusTarget(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        dispensationenPanel.add(scrollPane1, gbc);
        tableDispensationen = new JTable();
        scrollPane1.setViewportView(tableDispensationen);
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        panel3.add(rightPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        codesPanel = new JPanel();
        codesPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 10);
        rightPanel.add(codesPanel, gbc);
        codesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Codes", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(codesPanel.getFont().getName(), Font.BOLD, codesPanel.getFont().getSize()), new Color(-16777216)));
        final JPanel spacer23 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        codesPanel.add(spacer23, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        codesPanel.add(panel5, gbc);
        btnCodesBearbeiten = new JButton();
        btnCodesBearbeiten.setText("Bearbeiten");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel5.add(btnCodesBearbeiten, gbc);
        final JPanel spacer24 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(spacer24, gbc);
        final JPanel spacer25 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        codesPanel.add(spacer25, gbc);
        final JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setPreferredSize(new Dimension(23, 54));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        codesPanel.add(scrollPane2, gbc);
        tableCodes = new JTable();
        scrollPane2.setViewportView(tableCodes);
        final JPanel spacer26 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        codesPanel.add(spacer26, gbc);
        titelPanel = new JPanel();
        titelPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 40, 10);
        datenPanel.add(titelPanel, gbc);
        lblVornameNachname = new JLabel();
        lblVornameNachname.setFont(new Font(lblVornameNachname.getFont().getName(), lblVornameNachname.getFont().getStyle(), 36));
        lblVornameNachname.setText("Vorname Nachname");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        titelPanel.add(lblVornameNachname, gbc);
        final JPanel spacer27 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titelPanel.add(spacer27, gbc);
        final JPanel spacer28 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        datenPanel.add(spacer28, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
