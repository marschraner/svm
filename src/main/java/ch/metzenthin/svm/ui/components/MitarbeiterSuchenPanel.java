package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.ui.control.MitarbeiterSuchenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public class MitarbeiterSuchenPanel {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = true;

    private final SvmContext svmContext;
    private JPanel panel1;
    private JPanel datenPanel;
    private JPanel titelPanel;
    private JPanel buttonPanel;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JRadioButton radioBtnLehrkraftJa;
    private JRadioButton radioBtnLehrkraftNein;
    private JRadioButton radioBtnLehrkraftAlle;
    private JRadioButton radioBtnStatusAktiv;
    private JRadioButton radioBtnStatusNichtAktiv;
    private JRadioButton radioBtnStatusAlle;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private JComboBox<MitarbeiterCode> comboBoxMitarbeiterCode;
    private MitarbeiterSuchenController mitarbeiterSuchenController;
    private ActionListener nextPanelListener;

    public MitarbeiterSuchenPanel(SvmContext svmContext) {
        this.svmContext = svmContext;
        $$$setupUI$$$();
        initializeErrLbls();
        setDefaultButton();
        createMitarbeitersSuchenController(svmContext);
    }

    private void initializeErrLbls() {
        errLblNachname.setVisible(false);
        errLblNachname.setForeground(Color.RED);
        errLblVorname.setVisible(false);
        errLblVorname.setForeground(Color.RED);
    }

    private void createMitarbeitersSuchenController(SvmContext svmContext) {
        mitarbeiterSuchenController = new MitarbeiterSuchenController(svmContext, svmContext.getModelFactory().createMitarbeitersSuchenModel(), DEFAULT_BUTTON_ENABLED);
        mitarbeiterSuchenController.setTxtNachname(txtNachname);
        mitarbeiterSuchenController.setTxtVorname(txtVorname);
        mitarbeiterSuchenController.setComboBoxMitarbeiterCode(comboBoxMitarbeiterCode);
        mitarbeiterSuchenController.setRadioBtnGroupLehrkraftJaNein(radioBtnLehrkraftJa, radioBtnLehrkraftNein, radioBtnLehrkraftAlle);
        mitarbeiterSuchenController.setRadioBtnGroupStatus(radioBtnStatusAktiv, radioBtnStatusNichtAktiv, radioBtnStatusAlle);
        mitarbeiterSuchenController.setErrLblNachname(errLblNachname);
        mitarbeiterSuchenController.setErrLblVorname(errLblVorname);
        mitarbeiterSuchenController.setBtnSuchen(btnSuchen);
        mitarbeiterSuchenController.setBtnAbbrechen(btnAbbrechen);
        mitarbeiterSuchenController.addZurueckListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        });
    }

    private void onZurueck() {
        setDefaultButton();
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{$$$getRootComponent$$$(), "Mitarbeiter suchen", Boolean.TRUE}, ActionEvent.ACTION_PERFORMED, "Zurück zu Schüler suchen"));
    }

    public void addCloseListener(ActionListener actionListener) {
        mitarbeiterSuchenController.addCloseListener(actionListener);
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
        mitarbeiterSuchenController.addNextPanelListener(nextPanelListener);
    }

    private void createUIComponents() {
        comboBoxMitarbeiterCode = new JComboBox<>();
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
        createUIComponents();
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
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 20, 10);
        datenPanel.add(titelPanel, gbc);
        final JLabel label1 = new JLabel();
        label1.setFont(new Font(label1.getFont().getName(), label1.getFont().getStyle(), 36));
        label1.setText("Mitarbeiter suchen");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        titelPanel.add(label1, gbc);
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
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        datenPanel.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Suchkriterien", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel2.getFont().getName(), Font.BOLD, panel2.getFont().getSize())));
        final JLabel label2 = new JLabel();
        label2.setText("Nachname");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label2, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 300;
        panel2.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        panel2.add(spacer7, gbc);
        txtNachname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtNachname, gbc);
        errLblNachname = new JLabel();
        errLblNachname.setText("errLblNachname");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(errLblNachname, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer8, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Vorname");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label3, gbc);
        txtVorname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtVorname, gbc);
        errLblVorname = new JLabel();
        errLblVorname.setText("errLblVorname");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(errLblVorname, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer9, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Code");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label4, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer10, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Lehrkraft");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label5, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel3, gbc);
        radioBtnLehrkraftJa = new JRadioButton();
        radioBtnLehrkraftJa.setText("ja");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel3.add(radioBtnLehrkraftJa, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer11, gbc);
        radioBtnLehrkraftNein = new JRadioButton();
        radioBtnLehrkraftNein.setText("nein");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel3.add(radioBtnLehrkraftNein, gbc);
        radioBtnLehrkraftAlle = new JRadioButton();
        radioBtnLehrkraftAlle.setText("alle");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(radioBtnLehrkraftAlle, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer12, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Status");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label6, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel4, gbc);
        radioBtnStatusAktiv = new JRadioButton();
        radioBtnStatusAktiv.setText("aktiv");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel4.add(radioBtnStatusAktiv, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer13, gbc);
        radioBtnStatusNichtAktiv = new JRadioButton();
        radioBtnStatusNichtAktiv.setText("nicht aktiv");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel4.add(radioBtnStatusNichtAktiv, gbc);
        radioBtnStatusAlle = new JRadioButton();
        radioBtnStatusAlle.setText("alle");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(radioBtnStatusAlle, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(comboBoxMitarbeiterCode, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        panel1.add(buttonPanel, BorderLayout.SOUTH);
        btnSuchen = new JButton();
        btnSuchen.setMaximumSize(new Dimension(114, 29));
        btnSuchen.setMinimumSize(new Dimension(114, 29));
        btnSuchen.setPreferredSize(new Dimension(114, 29));
        btnSuchen.setText("Suchen");
        btnSuchen.setMnemonic('S');
        btnSuchen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnSuchen, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer14, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer15, gbc);
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
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer16, gbc);
        label2.setLabelFor(txtNachname);
        label3.setLabelFor(txtVorname);
        label4.setLabelFor(comboBoxMitarbeiterCode);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioBtnLehrkraftJa);
        buttonGroup.add(radioBtnLehrkraftNein);
        buttonGroup.add(radioBtnLehrkraftAlle);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioBtnStatusAktiv);
        buttonGroup.add(radioBtnStatusNichtAktiv);
        buttonGroup.add(radioBtnStatusAlle);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
