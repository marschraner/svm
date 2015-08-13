package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.control.CodesController;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public class CodesPanel {
    private JPanel panel1;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JTable codesTable;
    private JLabel lblTitle;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnZurueck;
    private JButton btnAbbrechen;
    private JPanel titelPanel;
    private CodesController codesController;

    public CodesPanel(SvmContext svmContext, CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable, int selectedRow, boolean isCodesSpecificSchueler, boolean isFromSchuelerSuchenResult, Codetyp codetyp) {
        $$$setupUI$$$();
        createCodesController(svmContext, codesTableModel, schuelerDatenblattModel, schuelerSuchenTableModel, schuelerSuchenResultTable, selectedRow, isCodesSpecificSchueler, isFromSchuelerSuchenResult, codetyp);
    }

    private void createCodesController(SvmContext svmContext, CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable, int selectedRow, boolean isCodesSpecificSchueler, boolean isFromSchuelerSuchenResult, Codetyp codetyp) {
        codesController = new CodesController(svmContext.getModelFactory().createCodesModel(), svmContext, codesTableModel, schuelerDatenblattModel, schuelerSuchenTableModel, schuelerSuchenResultTable, selectedRow, isCodesSpecificSchueler, isFromSchuelerSuchenResult, codetyp);
        codesController.setCodesTable(codesTable);
        codesController.setLblTitel(lblTitle);
        codesController.setBtnNeu(btnNeu);
        codesController.setBtnBearbeiten(btnBearbeiten);
        codesController.setBtnLoeschen(btnLoeschen);
        codesController.setBtnZurueck(btnZurueck);
        codesController.setBtnAbbrechen(btnAbbrechen);
    }

    public void addNextPanelListener(ActionListener actionListener) {
        codesController.addNextPanelListener(actionListener);
    }

    public void addCloseListener(ActionListener closeListener) {
        codesController.addCloseListener(closeListener);
    }

    public void addZurueckZuSchuelerSuchenListener(ActionListener zurueckZuSchuelerSuchenListener) {
        codesController.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
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
        lblTitle = new JLabel();
        lblTitle.setFont(new Font(lblTitle.getFont().getName(), lblTitle.getFont().getStyle(), 36));
        lblTitle.setText("Codes verwalten");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        titelPanel.add(lblTitle, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titelPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.VERTICAL;
        datenPanel.add(spacer2, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        datenPanel.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Codes", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel2.getFont().getName(), Font.BOLD, panel2.getFont().getSize())));
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer4, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(scrollPane1, gbc);
        codesTable = new JTable();
        codesTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
        scrollPane1.setViewportView(codesTable);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.VERTICAL;
        datenPanel.add(spacer7, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(buttonPanel, BorderLayout.SOUTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        buttonPanel.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        buttonPanel.add(panel4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer8, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(114, 29));
        btnAbbrechen.setMinimumSize(new Dimension(114, 29));
        btnAbbrechen.setPreferredSize(new Dimension(114, 29));
        btnAbbrechen.setText("Abbrechen");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel4.add(btnAbbrechen, gbc);
        btnZurueck = new JButton();
        btnZurueck.setMaximumSize(new Dimension(114, 29));
        btnZurueck.setMinimumSize(new Dimension(114, 29));
        btnZurueck.setPreferredSize(new Dimension(114, 29));
        btnZurueck.setText("Zurück");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel4.add(btnZurueck, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer10, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        buttonPanel.add(panel5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        btnLoeschen = new JButton();
        btnLoeschen.setMaximumSize(new Dimension(114, 29));
        btnLoeschen.setMinimumSize(new Dimension(114, 29));
        btnLoeschen.setPreferredSize(new Dimension(114, 29));
        btnLoeschen.setText("Löschen");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 0);
        panel5.add(btnLoeschen, gbc);
        btnBearbeiten = new JButton();
        btnBearbeiten.setInheritsPopupMenu(true);
        btnBearbeiten.setMaximumSize(new Dimension(114, 29));
        btnBearbeiten.setMinimumSize(new Dimension(114, 29));
        btnBearbeiten.setPreferredSize(new Dimension(114, 29));
        btnBearbeiten.setText("Bearbeiten");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel5.add(btnBearbeiten, gbc);
        btnNeu = new JButton();
        btnNeu.setMaximumSize(new Dimension(114, 29));
        btnNeu.setMinimumSize(new Dimension(114, 29));
        btnNeu.setPreferredSize(new Dimension(114, 29));
        btnNeu.setText("Neu");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel5.add(btnNeu, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel5.add(spacer11, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel5.add(spacer12, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
