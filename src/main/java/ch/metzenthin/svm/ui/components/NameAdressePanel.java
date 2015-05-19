package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.model.entities.Adresse;
import ch.metzenthin.svm.model.entities.Angehoeriger;
import ch.metzenthin.svm.model.entities.Person;

import javax.swing.*;
import java.awt.*;

/**
 * @author hans
 * todo löschen
 */
public class NameAdressePanel {
    private JPanel panel;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtStrasse;
    private JTextField txtPlz;
    private JTextField txtOrt;

    public void setData(Person data) {
        txtNachname.setText(data.getNachname());
    }

    public void setData(Adresse data) {
        txtStrasse.setText(data.getStrasse());
    }

    public void fillAngehoeriger(Angehoeriger angehoeriger) {
        angehoeriger.setNachname(txtNachname.getText());
    }

    public void fillAdresse(Adresse adresse) {
        adresse.setStrasse(txtStrasse.getText());
    }

    public boolean isModified(Person data) {
        return false;
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
        panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel.add(panel1, BorderLayout.WEST);
        final JLabel label1 = new JLabel();
        label1.setText("Nachname");
        label1.setDisplayedMnemonic('N');
        label1.setDisplayedMnemonicIndex(0);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        txtNachname = new JTextField();
        txtNachname.setColumns(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtNachname, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Vorname");
        label2.setDisplayedMnemonic('V');
        label2.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        txtVorname = new JTextField();
        txtVorname.setColumns(20);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtVorname, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Strasse");
        label3.setDisplayedMnemonic('S');
        label3.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label3, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        txtStrasse = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtStrasse, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("PLZ");
        label4.setDisplayedMnemonic('P');
        label4.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label4, gbc);
        txtPlz = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtPlz, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Ort");
        label5.setDisplayedMnemonic('O');
        label5.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label5, gbc);
        txtOrt = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtOrt, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer5, gbc);
        label1.setLabelFor(txtNachname);
        label2.setLabelFor(txtVorname);
        label3.setLabelFor(txtStrasse);
        label4.setLabelFor(txtPlz);
        label5.setLabelFor(txtOrt);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
