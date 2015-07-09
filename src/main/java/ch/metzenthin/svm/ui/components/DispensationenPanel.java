package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.DispensationenTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.control.DispensationenController;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public class DispensationenPanel {
    private JPanel panel1;
    private JTable dispensationenTable;
    private JLabel lblTitel;
    private JButton btnBearbeiten;
    private JButton btnNeu;
    private JButton btnZurueck;
    private JButton btnAbbrechen;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JPanel titelPanel;
    private JButton btnLoeschen;
    private DispensationenController dispensationenController;

    public DispensationenPanel(SvmContext svmContext, DispensationenTableModel dispensationenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, int selectedRow) {
        $$$setupUI$$$();
        initDispensationenTable(dispensationenTableModel);
        createDispensationenController(svmContext, schuelerDatenblattModel, schuelerSuchenTableModel, selectedRow);
    }

    private void initDispensationenTable(DispensationenTableModel dispensationenTableModel) {
        dispensationenTable.setModel(dispensationenTableModel);
        UiComponentsUtils.setJTableWidthAsPercentages(dispensationenTable, 0.15, 0.15, 0.3, 0.4);
    }

    private void createDispensationenController(SvmContext svmContext, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, int selectedRow) {
        dispensationenController = new DispensationenController(svmContext, schuelerDatenblattModel, schuelerSuchenTableModel, selectedRow);
        dispensationenController.setDispensationenTable(dispensationenTable);
        dispensationenController.setLblTitel(lblTitel);
        dispensationenController.setBtnNeu(btnNeu);
        dispensationenController.setBtnBearbeiten(btnBearbeiten);
        dispensationenController.setBtnLoeschen(btnLoeschen);
        dispensationenController.setBtnZurueck(btnZurueck);
        dispensationenController.setBtnAbbrechen(btnAbbrechen);
    }

    public void addNextPanelListener(ActionListener actionListener) {
        dispensationenController.addNextPanelListener(actionListener);
    }

    public void addCloseListener(ActionListener closeListener) {
        dispensationenController.addCloseListener(closeListener);
    }

    public void addZurueckZuSchuelerSuchenListener(ActionListener zurueckZuSchuelerSuchenListener) {
        dispensationenController.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
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
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        datenPanel.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Dispensationen", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel2.getFont().getName(), Font.BOLD, panel2.getFont().getSize()), new Color(-16777216)));
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer4, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(scrollPane1, gbc);
        dispensationenTable = new JTable();
        dispensationenTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        scrollPane1.setViewportView(dispensationenTable);
        titelPanel = new JPanel();
        titelPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 20, 10);
        datenPanel.add(titelPanel, gbc);
        lblTitel = new JLabel();
        lblTitel.setFont(new Font(lblTitel.getFont().getName(), lblTitel.getFont().getStyle(), 36));
        lblTitel.setText("Dispensationen Vorname Nachname");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        titelPanel.add(lblTitel, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titelPanel.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.VERTICAL;
        datenPanel.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.VERTICAL;
        datenPanel.add(spacer7, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(buttonPanel, BorderLayout.SOUTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        buttonPanel.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnNeu = new JButton();
        btnNeu.setActionCommand("Neuer Eintrag");
        btnNeu.setMaximumSize(new Dimension(114, 29));
        btnNeu.setMinimumSize(new Dimension(114, 29));
        btnNeu.setPreferredSize(new Dimension(114, 29));
        btnNeu.setSelected(true);
        btnNeu.setText("Neu");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 20, 5);
        panel3.add(btnNeu, gbc);
        btnBearbeiten = new JButton();
        btnBearbeiten.setMaximumSize(new Dimension(114, 29));
        btnBearbeiten.setMinimumSize(new Dimension(114, 29));
        btnBearbeiten.setPreferredSize(new Dimension(114, 29));
        btnBearbeiten.setText("Bearbeiten");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 20, 5);
        panel3.add(btnBearbeiten, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer9, gbc);
        btnLoeschen = new JButton();
        btnLoeschen.setMaximumSize(new Dimension(114, 29));
        btnLoeschen.setMinimumSize(new Dimension(114, 29));
        btnLoeschen.setPreferredSize(new Dimension(114, 29));
        btnLoeschen.setText("Löschen");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 20, 5);
        panel3.add(btnLoeschen, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        buttonPanel.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer10, gbc);
        btnZurueck = new JButton();
        btnZurueck.setMaximumSize(new Dimension(114, 29));
        btnZurueck.setMinimumSize(new Dimension(114, 29));
        btnZurueck.setPreferredSize(new Dimension(114, 29));
        btnZurueck.setText("Zurück");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 20, 5);
        panel4.add(btnZurueck, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(114, 29));
        btnAbbrechen.setMinimumSize(new Dimension(114, 29));
        btnAbbrechen.setPreferredSize(new Dimension(114, 29));
        btnAbbrechen.setText("Abbrechen");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 20, 5);
        panel4.add(btnAbbrechen, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer11, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
