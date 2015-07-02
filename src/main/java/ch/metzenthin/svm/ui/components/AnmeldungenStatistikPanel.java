package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.control.AnmeldungenStatistikController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public class AnmeldungenStatistikPanel {
    private JPanel panel;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private JLabel lblAnAbmeldemonat;
    private JTextField txtAnAbmeldemonat;
    private JRadioButton radioBtnAnmeldungen;
    private JRadioButton radioBtnAbmeldungen;
    private JLabel errLblAnAbmeldemonat;
    private AnmeldungenStatistikController anmeldungenStatistikController;

    public AnmeldungenStatistikPanel(SvmContext svmContext) {
        $$$setupUI$$$();
        initializeErrLbls();
        createAnmeldungenStatistikController(svmContext);
    }

    private void initializeErrLbls() {
        errLblAnAbmeldemonat.setVisible(false);
        errLblAnAbmeldemonat.setForeground(Color.RED);
    }

    private void createAnmeldungenStatistikController(SvmContext svmContext) {
        anmeldungenStatistikController = new AnmeldungenStatistikController(svmContext.getModelFactory().createAnmeldungenStatistikModel());
        anmeldungenStatistikController.setTxtAnAbmeldemonat(txtAnAbmeldemonat);
        anmeldungenStatistikController.setRadioBtnGroupAnAbmeldungen(radioBtnAnmeldungen, radioBtnAbmeldungen);
        anmeldungenStatistikController.setBtnSuchen(btnSuchen);
        anmeldungenStatistikController.setBtnAbbrechen(btnAbbrechen);
        anmeldungenStatistikController.setErrLblAnAbmeldemonat(errLblAnAbmeldemonat);
    }

    public void addCloseListener(ActionListener actionListener) {
        anmeldungenStatistikController.addCloseListener(actionListener);
    }

    public void addNextPanelListener(ActionListener actionListener) {
        anmeldungenStatistikController.addNextPanelListener(actionListener);
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel.add(panel1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel1.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "An-/Abmeldestatistik"));
        lblAnAbmeldemonat = new JLabel();
        lblAnAbmeldemonat.setText("An-/Abmeldemonat");
        lblAnAbmeldemonat.setDisplayedMnemonic('M');
        lblAnAbmeldemonat.setDisplayedMnemonicIndex(11);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblAnAbmeldemonat, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer1, gbc);
        txtAnAbmeldemonat = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtAnAbmeldemonat, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 200;
        panel2.add(spacer4, gbc);
        radioBtnAnmeldungen = new JRadioButton();
        radioBtnAnmeldungen.setSelected(true);
        radioBtnAnmeldungen.setText("Anmeldungen");
        radioBtnAnmeldungen.setMnemonic('N');
        radioBtnAnmeldungen.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel2.add(radioBtnAnmeldungen, gbc);
        radioBtnAbmeldungen = new JRadioButton();
        radioBtnAbmeldungen.setText("Abmeldungen");
        radioBtnAbmeldungen.setMnemonic('B');
        radioBtnAbmeldungen.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(radioBtnAbmeldungen, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 300;
        panel2.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer7, gbc);
        errLblAnAbmeldemonat = new JLabel();
        errLblAnAbmeldemonat.setText("errLblAnAbmeldemonat");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(errLblAnAbmeldemonat, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel.add(panel3, BorderLayout.SOUTH);
        btnSuchen = new JButton();
        btnSuchen.setMaximumSize(new Dimension(114, 29));
        btnSuchen.setMinimumSize(new Dimension(114, 29));
        btnSuchen.setPreferredSize(new Dimension(114, 29));
        btnSuchen.setSelected(true);
        btnSuchen.setText("Suchen");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 10, 5);
        panel3.add(btnSuchen, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(114, 29));
        btnAbbrechen.setMinimumSize(new Dimension(114, 29));
        btnAbbrechen.setPreferredSize(new Dimension(114, 29));
        btnAbbrechen.setText("Abbrechen");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 10, 5);
        panel3.add(btnAbbrechen, gbc);
        lblAnAbmeldemonat.setLabelFor(txtAnAbmeldemonat);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioBtnAnmeldungen);
        buttonGroup.add(radioBtnAbmeldungen);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
