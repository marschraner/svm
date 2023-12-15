package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.control.KurseController;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

/**
 * @author Martin Schraner
 */
public class KursePanel {

    private JPanel panel1;
    private JPanel datenPanel;
    private JPanel titelPanel;
    private JPanel kurseTablePanel;
    private JPanel buttonPanel;
    private JLabel lblTitel;
    private JLabel lblTotal;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private JButton btnImportieren;
    private JButton btnExportieren;
    private JTable kurseTable;
    private KurseController kurseController;

    public KursePanel(SvmContext svmContext, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel, String titel) {
        lblTitel.setText(titel);
        createUIComponents();
        createKurseController(svmContext, kurseSemesterwahlModel, kurseTableModel);
    }

    private void createKurseController(SvmContext svmContext, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel) {
        kurseController = new KurseController(svmContext.getModelFactory().createKurseModel(), svmContext, kurseSemesterwahlModel, kurseTableModel);
        kurseController.setKurseTable(kurseTable);
        kurseController.setLblTotal(lblTotal);
        kurseController.setBtnNeu(btnNeu);
        kurseController.setBtnBearbeiten(btnBearbeiten);
        kurseController.setBtnLoeschen(btnLoeschen);
        kurseController.setBtnAbbrechen(btnAbbrechen);
        kurseController.setBtnImportieren(btnImportieren);
        kurseController.setBtnExportieren(btnExportieren);
    }

    public void addCloseListener(ActionListener closeListener) {
        kurseController.addCloseListener(closeListener);
    }

    @SuppressWarnings("DuplicatedCode")
    private void createUIComponents() {
        // JTable mit optimiertem Scroll-Verhalten
        // http://stackoverflow.com/questions/6104916/how-to-make-jtable-both-autoresize-and-horizontall-scrollable
        kurseTable = new JTable() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        kurseTable.setAutoCreateRowSorter(true);
        kurseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        kurseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JScrollPane scrollPane = new JScrollPane(kurseTable);
        kurseTablePanel.add(scrollPane);
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
        datenPanel = new JPanel();
        datenPanel.setLayout(new GridBagLayout());
        panel1.add(datenPanel, BorderLayout.CENTER);
        titelPanel = new JPanel();
        titelPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 20, 10);
        datenPanel.add(titelPanel, gbc);
        lblTitel = new JLabel();
        Font lblTitelFont = this.$$$getFont$$$(null, -1, 36, lblTitel.getFont());
        if (lblTitelFont != null) lblTitel.setFont(lblTitelFont);
        lblTitel.setText("Kurse verwalten");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        titelPanel.add(lblTitel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titelPanel.add(spacer1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        datenPanel.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kurse", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel2.getFont()), null));
        kurseTablePanel = new JPanel();
        kurseTablePanel.setLayout(new BorderLayout(0, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(kurseTablePanel, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer8, gbc);
        lblTotal = new JLabel();
        lblTotal.setText("lblTotal");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel2.add(lblTotal, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(buttonPanel, BorderLayout.SOUTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        buttonPanel.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnImportieren = new JButton();
        btnImportieren.setMaximumSize(new Dimension(120, 29));
        btnImportieren.setMinimumSize(new Dimension(120, 29));
        btnImportieren.setPreferredSize(new Dimension(120, 29));
        btnImportieren.setText("Importieren");
        btnImportieren.setMnemonic('I');
        btnImportieren.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 15, 0, 0);
        panel3.add(btnImportieren, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer11, gbc);
        btnExportieren = new JButton();
        btnExportieren.setMaximumSize(new Dimension(120, 29));
        btnExportieren.setMinimumSize(new Dimension(120, 29));
        btnExportieren.setPreferredSize(new Dimension(120, 29));
        btnExportieren.setText("Exportieren");
        btnExportieren.setMnemonic('E');
        btnExportieren.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        panel3.add(btnExportieren, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        buttonPanel.add(panel4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(114, 29));
        btnAbbrechen.setMinimumSize(new Dimension(114, 29));
        btnAbbrechen.setPreferredSize(new Dimension(114, 29));
        btnAbbrechen.setText("Abbrechen");
        btnAbbrechen.setMnemonic('A');
        btnAbbrechen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 15);
        panel4.add(btnAbbrechen, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer12, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer13, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer14, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        buttonPanel.add(panel5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNeu = new JButton();
        btnNeu.setMaximumSize(new Dimension(114, 29));
        btnNeu.setMinimumSize(new Dimension(114, 29));
        btnNeu.setPreferredSize(new Dimension(114, 29));
        btnNeu.setText("Neu");
        btnNeu.setMnemonic('N');
        btnNeu.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel5.add(btnNeu, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel5.add(spacer15, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel5.add(spacer16, gbc);
        btnBearbeiten = new JButton();
        btnBearbeiten.setMaximumSize(new Dimension(114, 29));
        btnBearbeiten.setMinimumSize(new Dimension(114, 29));
        btnBearbeiten.setPreferredSize(new Dimension(114, 29));
        btnBearbeiten.setText("Bearbeiten");
        btnBearbeiten.setMnemonic('B');
        btnBearbeiten.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel5.add(btnBearbeiten, gbc);
        btnLoeschen = new JButton();
        btnLoeschen.setMaximumSize(new Dimension(114, 29));
        btnLoeschen.setMinimumSize(new Dimension(114, 29));
        btnLoeschen.setPreferredSize(new Dimension(114, 29));
        btnLoeschen.setText("Löschen");
        btnLoeschen.setMnemonic('L');
        btnLoeschen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 0);
        panel5.add(btnLoeschen, gbc);
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
        return panel1;
    }

}
