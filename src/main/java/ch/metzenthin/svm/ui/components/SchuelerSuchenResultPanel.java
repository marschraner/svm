package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.control.SchuelerSuchenResultController;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenResultPanel {
    private JTable schuelerSuchenResultTable;
    private JPanel panel;
    private JButton btnAlleDeselektieren;
    private JButton btnAlleSelektieren;
    private JButton btnDatenblatt;
    private JButton btnExportieren;
    private JButton btnAbbrechen;
    private JButton btnZurueck;
    private JLabel lblTotal;
    private JPanel buttonPanel;
    private JPanel datenPanel;
    private JPanel titelPanel;
    private JPanel schuelerSuchenResultTablePanel;
    private JButton btnEmail;
    private final SchuelerSuchenResultController schuelerSuchenResultController;

    public SchuelerSuchenResultPanel(SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel) {
        $$$setupUI$$$();
        createUIComponents();

        // Kursiv unterstrichen für alle deselektieren / alle selektieren
        Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        Font newButtonFont = new Font(btnAlleDeselektieren.getFont().getName(), Font.ITALIC, btnAlleDeselektieren.getFont().getSize()).deriveFont(fontAttributes);
        btnAlleDeselektieren.setFont(newButtonFont);
        btnAlleSelektieren.setFont(newButtonFont);

        schuelerSuchenResultController = new SchuelerSuchenResultController(svmContext, schuelerSuchenTableModel, schuelerSuchenResultTable);
        schuelerSuchenResultController.setLblTotal(lblTotal);
        schuelerSuchenResultController.setBtnAlleDeselektieren(btnAlleDeselektieren);
        schuelerSuchenResultController.setBtnAlleSelektieren(btnAlleSelektieren);
        schuelerSuchenResultController.setBtnDatenblatt(btnDatenblatt);
        schuelerSuchenResultController.setBtnExportieren(btnExportieren);
        schuelerSuchenResultController.setBtnEmail(btnEmail);
        schuelerSuchenResultController.setBtnZurueck(btnZurueck);
        schuelerSuchenResultController.setBtnAbbrechen(btnAbbrechen);
    }

    public void addNextPanelListener(ActionListener actionListener) {
        schuelerSuchenResultController.addNextPanelListener(actionListener);
    }

    public void addCloseListener(ActionListener closeListener) {
        schuelerSuchenResultController.addCloseListener(closeListener);
    }

    public void addZurueckListener(ActionListener zurueckListener) {
        schuelerSuchenResultController.addZurueckListener(zurueckListener);
    }

    private void createUIComponents() {
        // JTable mit optimiertem Scroll-Verhalten
        // http://stackoverflow.com/questions/6104916/how-to-make-jtable-both-autoresize-and-horizontall-scrollable
        schuelerSuchenResultTable = new JTable() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        schuelerSuchenResultTable.setAutoCreateRowSorter(true);
        schuelerSuchenResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        schuelerSuchenResultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPane = new JScrollPane(schuelerSuchenResultTable);
        schuelerSuchenResultTablePanel.add(scrollPane);
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
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel.add(buttonPanel, BorderLayout.SOUTH);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        buttonPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnExportieren = new JButton();
        btnExportieren.setMaximumSize(new Dimension(141, 29));
        btnExportieren.setMinimumSize(new Dimension(141, 29));
        btnExportieren.setPreferredSize(new Dimension(141, 29));
        btnExportieren.setText("Exportieren");
        btnExportieren.setMnemonic('E');
        btnExportieren.setDisplayedMnemonicIndex(0);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        panel1.add(btnExportieren, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer4, gbc);
        btnEmail = new JButton();
        btnEmail.setMaximumSize(new Dimension(141, 29));
        btnEmail.setMinimumSize(new Dimension(141, 29));
        btnEmail.setPreferredSize(new Dimension(141, 29));
        btnEmail.setText("Gruppen-E-Mail");
        btnEmail.setMnemonic('G');
        btnEmail.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(btnEmail, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        buttonPanel.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnDatenblatt = new JButton();
        btnDatenblatt.setMaximumSize(new Dimension(114, 29));
        btnDatenblatt.setMinimumSize(new Dimension(114, 29));
        btnDatenblatt.setPreferredSize(new Dimension(114, 29));
        btnDatenblatt.setText("Datenblatt");
        btnDatenblatt.setMnemonic('B');
        btnDatenblatt.setDisplayedMnemonicIndex(5);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel2.add(btnDatenblatt, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer6, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        buttonPanel.add(panel3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnZurueck = new JButton();
        btnZurueck.setMaximumSize(new Dimension(114, 29));
        btnZurueck.setMinimumSize(new Dimension(114, 29));
        btnZurueck.setPreferredSize(new Dimension(114, 29));
        btnZurueck.setText("Zurück");
        btnZurueck.setMnemonic('Z');
        btnZurueck.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel3.add(btnZurueck, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer8, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(114, 29));
        btnAbbrechen.setMinimumSize(new Dimension(114, 29));
        btnAbbrechen.setPreferredSize(new Dimension(114, 29));
        btnAbbrechen.setText("Abbrechen");
        btnAbbrechen.setMnemonic('A');
        btnAbbrechen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel3.add(btnAbbrechen, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer10, gbc);
        datenPanel = new JPanel();
        datenPanel.setLayout(new GridBagLayout());
        panel.add(datenPanel, BorderLayout.CENTER);
        titelPanel = new JPanel();
        titelPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 20, 10);
        datenPanel.add(titelPanel, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 36, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Suchresultat");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        titelPanel.add(label1, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titelPanel.add(spacer11, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        datenPanel.add(panel4, gbc);
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Schüler", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel4.getFont())));
        schuelerSuchenResultTablePanel = new JPanel();
        schuelerSuchenResultTablePanel.setLayout(new BorderLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel4.add(schuelerSuchenResultTablePanel, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer12, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer13, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer14, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer15, gbc);
        lblTotal = new JLabel();
        lblTotal.setText("lblTotal");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(lblTotal, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer16, gbc);
        final JPanel spacer17 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer17, gbc);
        btnAlleDeselektieren = new JButton();
        btnAlleDeselektieren.setBackground(new Color(-1));
        btnAlleDeselektieren.setBorderPainted(false);
        btnAlleDeselektieren.setContentAreaFilled(false);
        btnAlleDeselektieren.setOpaque(false);
        btnAlleDeselektieren.setText("Auswahl löschen");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(btnAlleDeselektieren, gbc);
        final JPanel spacer18 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer18, gbc);
        btnAlleSelektieren = new JButton();
        btnAlleSelektieren.setBackground(new Color(-1));
        btnAlleSelektieren.setBorderPainted(false);
        btnAlleSelektieren.setContentAreaFilled(false);
        btnAlleSelektieren.setOpaque(false);
        btnAlleSelektieren.setText("Alle auswählen");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(btnAlleSelektieren, gbc);
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
        return panel;
    }
}
