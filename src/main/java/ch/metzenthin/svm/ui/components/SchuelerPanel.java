package ch.metzenthin.svm.ui.components;


import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.domain.model.SchuelerModel;
import ch.metzenthin.svm.ui.control.CompletedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hans
 */
public class SchuelerPanel {
    private JPanel panel;
    private JComboBox<Anrede> comboBoxAnrede;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtStrasse;
    private JTextField txtHausnummer;
    private JTextField txtPlz;
    private JTextField txtOrt;
    private JTextField txtFestnetz;
    private JTextField txtNatel;
    private JTextField txtEmail;
    private JTextField txtGeburtsdatum;
    private JTextField txtAnmeldedatum;
    private JTextField txtAbmeldedatum;
    private JTextField txtDispensationsbeginn;
    private JTextField txtDispensationsende;
    private JTextArea textAreaBemerkungen;

    private SchuelerModel schuelerModel;

    public SchuelerPanel() {
        $$$setupUI$$$();
        comboBoxAnrede.setModel(new DefaultComboBoxModel<>(Anrede.values()));
        comboBoxAnrede.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnredeSelected();
            }
        });
        txtNachname.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNachnameEvent();
            }
        });
        txtNachname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onNachnameEvent();
            }
        });
        txtVorname.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVornameEvent();
            }
        });
        txtVorname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onVornameEvent();
            }
        });
        txtStrasse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStrasseEvent();
            }
        });
        txtStrasse.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onStrasseEvent();
            }
        });
        txtHausnummer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHausnummerEvent();
            }
        });
        txtHausnummer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onHausnummerEvent();
            }
        });
        txtPlz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPlzEvent();
            }
        });
        txtPlz.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onPlzEvent();
            }
        });
        txtOrt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOrtEvent();
            }
        });
        txtOrt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onOrtEvent();
            }
        });
        txtFestnetz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFestnetzEvent();
            }
        });
        txtFestnetz.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onFestnetzEvent();
            }
        });
        txtNatel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNatelEvent();
            }
        });
        txtNatel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onNatelEvent();
            }
        });
        txtEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEmailEvent();
            }
        });
        txtEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onEmailEvent();
            }
        });
        txtGeburtsdatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGeburtsdatumEvent();
            }
        });
        txtGeburtsdatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onGeburtsdatumEvent();
            }
        });
        txtAnmeldedatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnmeldedatumEvent();
            }
        });
        txtAnmeldedatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAnmeldedatumEvent();
            }
        });
        txtAbmeldedatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbmeldedatumEvent();
            }
        });
        txtAbmeldedatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAbmeldedatumEvent();
            }
        });
        txtDispensationsbeginn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDispensationsbeginnEvent();
            }
        });
        txtDispensationsbeginn.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onDispensationsbeginnEvent();
            }
        });
        txtDispensationsende.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDispensationsendeEvent();
            }
        });
        txtDispensationsende.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onDispensationsendeEvent();
            }
        });
        // todo weitere listener?
        textAreaBemerkungen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBemerkungenEvent();
            }
        });
    }

    public void setModel(SchuelerModel schuelerModel) {
        this.schuelerModel = schuelerModel;
    }

    private final List<CompletedListener> completedListeners = new ArrayList<>();

    public void addCompletedListener(CompletedListener completedListener) {
        completedListeners.add(completedListener);
    }

    private void onAnredeSelected() {
        System.out.println("SchuelerPanel Event Anrede selected=" + comboBoxAnrede.getSelectedItem());
        schuelerModel.setAnrede((Anrede) comboBoxAnrede.getSelectedItem());
        checkCompleted();
    }

    private void onNachnameEvent() {
        System.out.println("SchuelerPanel Event Nachname");
        schuelerModel.setNachname(txtNachname.getText());
        checkCompleted();
    }

    private void onVornameEvent() {
        System.out.println("SchuelerPanel Event Vorname");
        schuelerModel.setVorname(txtVorname.getText());
        checkCompleted();
    }

    private void onStrasseEvent() {
        System.out.println("SchuelerPanel Event Strasse");
        schuelerModel.setStrasse(txtStrasse.getText());
        checkCompleted();
    }

    private void onHausnummerEvent() {
        System.out.println("SchuelerPanel Event Hausnummer");
        schuelerModel.setHausnummer(txtHausnummer.getText());
        checkCompleted();
    }

    private void onPlzEvent() {
        System.out.println("SchuelerPanel Event PLZ");
        schuelerModel.setPlz(txtPlz.getText());
        checkCompleted();
    }

    private void onOrtEvent() {
        System.out.println("SchuelerPanel Event Ort");
        schuelerModel.setOrt(txtOrt.getText());
        checkCompleted();
    }

    private void onFestnetzEvent() {
        System.out.println("SchuelerPanel Event Festnetz");
        schuelerModel.setFestnetz(txtFestnetz.getText());
        checkCompleted();
    }

    private void onNatelEvent() {
        System.out.println("SchuelerPanel Event Natel");
        schuelerModel.setNatel(txtNatel.getText());
        checkCompleted();
    }

    private void onEmailEvent() {
        System.out.println("SchuelerPanel Event Email");
        schuelerModel.setEmail(txtEmail.getText());
        checkCompleted();
    }

    private void onGeburtsdatumEvent() {
        System.out.println("SchuelerPanel Event Geburtsdatum");
        schuelerModel.setGeburtsdatum(txtGeburtsdatum.getText());
        checkCompleted();
    }

    private void onAnmeldedatumEvent() {
        System.out.println("SchuelerPanel Event Anmeldedatum");
        schuelerModel.setAnmeldedatum(txtAnmeldedatum.getText());
        checkCompleted();
    }

    private void onAbmeldedatumEvent() {
        System.out.println("SchuelerPanel Event Abmeldedatum");
        schuelerModel.setAbmeldedatum(txtAbmeldedatum.getText());
        checkCompleted();
    }

    private void onDispensationsbeginnEvent() {
        System.out.println("SchuelerPanel Event Dispensationsbeginn");
        schuelerModel.setDispensationsbeginn(txtDispensationsbeginn.getText());
        checkCompleted();
    }

    private void onDispensationsendeEvent() {
        System.out.println("SchuelerPanel Event Dispensationsende");
        schuelerModel.setDispensationsende(txtDispensationsende.getText());
        checkCompleted();
    }

    private void onBemerkungenEvent() {
        System.out.println("SchuelerPanel Event Bemerkungen");
        schuelerModel.setBemerkungen(textAreaBemerkungen.getText());
        checkCompleted();
    }

    private void checkCompleted() {
        fireCompleted(schuelerModel.isValid());
    }

    private void fireCompleted(boolean completed) {
        for (CompletedListener completedListener : completedListeners) {
            completedListener.completed(completed);
        }
    }

    private void createUIComponents() {
        comboBoxAnrede = new JComboBox<>();
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
        panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel.add(panel1, BorderLayout.CENTER);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final JLabel label1 = new JLabel();
        label1.setText("Nachname");
        label1.setDisplayedMnemonic('C');
        label1.setDisplayedMnemonicIndex(2);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        txtNachname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtNachname, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Vorname");
        label2.setDisplayedMnemonic('V');
        label2.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label2, gbc);
        txtVorname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtVorname, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Strasse/Hausnummer");
        label3.setDisplayedMnemonic('S');
        label3.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label3, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("PLZ");
        label4.setDisplayedMnemonic('P');
        label4.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label4, gbc);
        txtPlz = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtPlz, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Ort");
        label5.setDisplayedMnemonic('O');
        label5.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label5, gbc);
        txtOrt = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtOrt, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer5, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        txtStrasse = new JTextField();
        txtStrasse.setColumns(27);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtStrasse, gbc);
        txtHausnummer = new JTextField();
        txtHausnummer.setColumns(3);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtHausnummer, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer6, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Festnetz");
        label6.setDisplayedMnemonic('F');
        label6.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer7, gbc);
        txtFestnetz = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtFestnetz, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Anrede");
        label7.setDisplayedMnemonic('A');
        label7.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer8, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxAnrede, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Geburtsdatum");
        label8.setDisplayedMnemonic('G');
        label8.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 19;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label8, gbc);
        txtGeburtsdatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 19;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtGeburtsdatum, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 18;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer9, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Natel");
        label9.setDisplayedMnemonic('N');
        label9.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label9, gbc);
        txtNatel = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtNatel, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer10, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("E-Mail");
        label10.setDisplayedMnemonic('E');
        label10.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label10, gbc);
        txtEmail = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtEmail, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer11, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer12, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 20;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer13, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Abmeldedatum");
        label11.setDisplayedMnemonic('B');
        label11.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 23;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label11, gbc);
        txtAbmeldedatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 23;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtAbmeldedatum, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 24;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer14, gbc);
        final JLabel label12 = new JLabel();
        label12.setText("Anmeldedatum");
        label12.setDisplayedMnemonic('M');
        label12.setDisplayedMnemonicIndex(2);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 21;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label12, gbc);
        txtAnmeldedatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtAnmeldedatum, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 22;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer15, gbc);
        final JLabel label13 = new JLabel();
        label13.setText("Dispensationsbeginn");
        label13.setDisplayedMnemonic('D');
        label13.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 25;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label13, gbc);
        txtDispensationsbeginn = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 25;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtDispensationsbeginn, gbc);
        final JLabel label14 = new JLabel();
        label14.setText("Dispensationsende");
        label14.setDisplayedMnemonic('I');
        label14.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 27;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label14, gbc);
        txtDispensationsende = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 27;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtDispensationsende, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 26;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer16, gbc);
        final JPanel spacer17 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 28;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer17, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 29;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(scrollPane1, gbc);
        textAreaBemerkungen = new JTextArea();
        textAreaBemerkungen.setRows(5);
        scrollPane1.setViewportView(textAreaBemerkungen);
        final JLabel label15 = new JLabel();
        label15.setText("Bemerkungen");
        label15.setDisplayedMnemonic('R');
        label15.setDisplayedMnemonicIndex(4);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 29;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(label15, gbc);
        final JPanel spacer18 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 30;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer18, gbc);
        label1.setLabelFor(txtNachname);
        label2.setLabelFor(txtVorname);
        label3.setLabelFor(txtHausnummer);
        label4.setLabelFor(txtPlz);
        label5.setLabelFor(txtOrt);
        label6.setLabelFor(txtFestnetz);
        label7.setLabelFor(comboBoxAnrede);
        label8.setLabelFor(txtGeburtsdatum);
        label9.setLabelFor(txtNatel);
        label10.setLabelFor(txtEmail);
        label11.setLabelFor(txtAbmeldedatum);
        label12.setLabelFor(txtAnmeldedatum);
        label13.setLabelFor(txtDispensationsbeginn);
        label14.setLabelFor(txtDispensationsende);
        label15.setLabelFor(textAreaBemerkungen);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
