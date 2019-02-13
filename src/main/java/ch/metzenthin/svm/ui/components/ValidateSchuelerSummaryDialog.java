package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.model.SchuelerErfassenModel;
import ch.metzenthin.svm.domain.model.ValidateSchuelerSummaryResult;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

import static ch.metzenthin.svm.common.utils.Converter.asString;

public class ValidateSchuelerSummaryDialog extends SchuelerErfassenDialog {
    private final ValidateSchuelerSummaryResult validateSchuelerSummaryResult;
    private final SchuelerErfassenModel schuelerErfassenModel;
    private JPanel contentPane;
    private JButton buttonSpeichern;
    private JButton buttonAbbrechen;
    private JLabel schuelerValue;
    private JLabel mutterValue;
    private JLabel vaterValue;
    private JLabel rechnungsempfaengerValue;
    private JLabel lblSchueler;
    private JLabel lblRechnungsempfaenger;
    private JLabel lblMutter;
    private JLabel lblVater;
    private JLabel infoIdentischeAdressen;
    private JLabel geschwisterValue;
    private JLabel schuelerGleicherRechnungsempfaengerValue;
    private JLabel anmeldedatumValue;
    private JLabel geburtsdatumValue;
    private JLabel lblAndereSchuelerGleicherRechnungsempfaenger1;
    private JLabel lblAndereSchuelerGleicherRechnungsempfaenger2;
    private JLabel abmeldedatum;
    private JLabel abmeldedatumValue;

    public ValidateSchuelerSummaryDialog(
            ValidateSchuelerSummaryResult validateSchuelerSummaryResult,
            SchuelerErfassenModel schuelerErfassenModel
    ) {
        this.validateSchuelerSummaryResult = validateSchuelerSummaryResult;
        this.schuelerErfassenModel = schuelerErfassenModel;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSpeichern);

        setTitle("Zusammenfassung");

        if (validateSchuelerSummaryResult != null) {
            setSchueler();
            setMutter();
            setVater();
            setRechnungsempfaenger();
            setGeschwister();
            setSchuelerGleicherRechnungsempfaenger();
            setGeburtsdatum();
            setAnmeldedatum();
            setAbmeldedatum();
            setInfoIdentischeAdressen();
        }

        buttonSpeichern.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSpeichern();
            }
        });

        buttonAbbrechen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });

        // call onAbbrechen() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });

        // call onAbbrechen() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setSchueler() {
        lblSchueler.setText(validateSchuelerSummaryResult.getSchueler().getGeschlecht() == Geschlecht.W ? "Schülerin:" : "Schüler:");
        schuelerValue.setText(validateSchuelerSummaryResult.getSchueler().toString());
    }

    private void setMutter() {
        String neuMutter = ((validateSchuelerSummaryResult.getMutter() != null && validateSchuelerSummaryResult.isMutterNeu()) ? " (neu)" : "");
        lblMutter.setText("Mutter" + neuMutter + ":");
        mutterValue.setText(validateSchuelerSummaryResult.getMutter() == null ? "-" : validateSchuelerSummaryResult.getMutter().toString());
    }

    private void setVater() {
        String neuVater = ((validateSchuelerSummaryResult.getVater() != null && validateSchuelerSummaryResult.isVaterNeu()) ? " (neu)" : "");
        lblVater.setText("Vater" + neuVater + ":");
        vaterValue.setText(validateSchuelerSummaryResult.getVater() == null ? "-" : validateSchuelerSummaryResult.getVater().toString());
    }

    private void setRechnungsempfaenger() {
        String lblRechnungsempfaengerText;
        String rechnungsempfaengerText;
        if (validateSchuelerSummaryResult.isRechnungsempfaengerMutter()) {
            lblRechnungsempfaengerText = "Rechnungsempfängerin:";
            rechnungsempfaengerText = "Mutter";
        } else if (validateSchuelerSummaryResult.isRechnungsempfaengerVater()) {
            lblRechnungsempfaengerText = "Rechnungsempfänger:";
            rechnungsempfaengerText = "Vater";
        } else {
            String neuRechnungsempfaenger = (validateSchuelerSummaryResult.isRechnungsempfaengerNeu() ? " (neu)" : "");
            lblRechnungsempfaengerText = (validateSchuelerSummaryResult.getRechnungsempfaenger().getAnrede() == Anrede.FRAU ? "Rechnungsempfängerin" + neuRechnungsempfaenger + ":" : "Rechnungsempfänger" + neuRechnungsempfaenger + ":");
            rechnungsempfaengerText = validateSchuelerSummaryResult.getRechnungsempfaenger().toString();
        }
        lblRechnungsempfaenger.setText(lblRechnungsempfaengerText);
        rechnungsempfaengerValue.setText(rechnungsempfaengerText);
    }

    private void setGeburtsdatum() {
        geburtsdatumValue.setText(asString(validateSchuelerSummaryResult.getSchueler().getGeburtsdatum()));
    }

    private void setAnmeldedatum() {
        anmeldedatumValue.setText(asString(validateSchuelerSummaryResult.getSchueler().getAnmeldungen().get(0).getAnmeldedatum()));
    }

    private void setAbmeldedatum() {
        if (validateSchuelerSummaryResult.getSchueler().getAnmeldungen().get(0).getAbmeldedatum() == null) {
            abmeldedatum.setVisible(false);
            abmeldedatumValue.setVisible(false);
        } else {
            abmeldedatumValue.setText(asString(validateSchuelerSummaryResult.getSchueler().getAnmeldungen().get(0).getAbmeldedatum()));
        }
    }

    private void setInfoIdentischeAdressen() {
        String infoIdentischeAdressenText;
        String identischeAdressen = validateSchuelerSummaryResult.getIdentischeAdressen();
        String abweichendeAdressen = validateSchuelerSummaryResult.getAbweichendeAdressen();
        if (identischeAdressen != null && !identischeAdressen.isEmpty() && abweichendeAdressen != null && !abweichendeAdressen.isEmpty()) {
            infoIdentischeAdressenText = identischeAdressen + ", " + abweichendeAdressen + ".";
        } else if (identischeAdressen != null && !identischeAdressen.isEmpty()) {
            infoIdentischeAdressenText = identischeAdressen + ".";
        } else {
            infoIdentischeAdressenText = abweichendeAdressen + ".";
        }
        infoIdentischeAdressen.setText(infoIdentischeAdressenText);
    }

    private void setGeschwister() {
        if (validateSchuelerSummaryResult.getGeschwister() != null && !validateSchuelerSummaryResult.getGeschwister().isEmpty()) {
            StringBuilder geschwisterStb = new StringBuilder("<html>");
            for (Schueler geschwister : validateSchuelerSummaryResult.getGeschwister()) {
                if (geschwisterStb.length() > 6) {
                    geschwisterStb.append("<br>");
                }
                geschwisterStb.append(geschwister.toStringForGuiWithAbgemeldetInfo());
            }
            geschwisterStb.append("</html>");
            geschwisterValue.setText(geschwisterStb.toString());
        } else {
            geschwisterValue.setText("-");
        }
    }

    private void setSchuelerGleicherRechnungsempfaenger() {
        if (validateSchuelerSummaryResult.getRechnungsempfaenger().getAnrede() == Anrede.FRAU) {
            lblAndereSchuelerGleicherRechnungsempfaenger1.setText("Andere Schüler mit gleicher");
            lblAndereSchuelerGleicherRechnungsempfaenger2.setText("Rechnungsempfängerin:");
        }
        if (validateSchuelerSummaryResult.getAndereSchueler() != null && !validateSchuelerSummaryResult.getAndereSchueler().isEmpty()) {
            StringBuilder schuelerGleicherRechnungsempfaengerStb = new StringBuilder("<html>");
            for (Schueler schueler : validateSchuelerSummaryResult.getAndereSchueler()) {
                if (schuelerGleicherRechnungsempfaengerStb.length() > 6) {
                    schuelerGleicherRechnungsempfaengerStb.append("<br>");
                }
                schuelerGleicherRechnungsempfaengerStb.append(schueler.toString());
            }
            schuelerGleicherRechnungsempfaengerStb.append("</html>");
            schuelerGleicherRechnungsempfaengerValue.setText(schuelerGleicherRechnungsempfaengerStb.toString());
        } else {
            schuelerGleicherRechnungsempfaengerValue.setText("-");
        }
    }

    private void onSpeichern() {
        setResult(schuelerErfassenModel.speichern(validateSchuelerSummaryResult));
        dispose();
    }

    private void onAbbrechen() {
        schuelerErfassenModel.abbrechen();
        abbrechen();
        dispose();
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
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        contentPane.add(panel1, BorderLayout.SOUTH);
        buttonAbbrechen = new JButton();
        buttonAbbrechen.setMaximumSize(new Dimension(114, 29));
        buttonAbbrechen.setMinimumSize(new Dimension(114, 29));
        buttonAbbrechen.setPreferredSize(new Dimension(114, 29));
        buttonAbbrechen.setText("Abbrechen");
        buttonAbbrechen.setMnemonic('A');
        buttonAbbrechen.setDisplayedMnemonicIndex(0);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 10, 5);
        panel1.add(buttonAbbrechen, gbc);
        buttonSpeichern = new JButton();
        buttonSpeichern.setHorizontalAlignment(0);
        buttonSpeichern.setHorizontalTextPosition(11);
        buttonSpeichern.setMaximumSize(new Dimension(114, 29));
        buttonSpeichern.setMinimumSize(new Dimension(114, 29));
        buttonSpeichern.setOpaque(false);
        buttonSpeichern.setPreferredSize(new Dimension(114, 29));
        buttonSpeichern.setText("Speichern");
        buttonSpeichern.setMnemonic('S');
        buttonSpeichern.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 10, 5);
        panel1.add(buttonSpeichern, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        contentPane.add(panel2, BorderLayout.CENTER);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel2.add(panel3, gbc);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Zusammefassung", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel3.getFont()), new Color(-16777216)));
        lblSchueler = new JLabel();
        lblSchueler.setText("Schüler:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lblSchueler, gbc);
        lblMutter = new JLabel();
        lblMutter.setText("Mutter:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lblMutter, gbc);
        schuelerValue = new JLabel();
        schuelerValue.setText("SchuelerValue");
        schuelerValue.setVerticalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(schuelerValue, gbc);
        mutterValue = new JLabel();
        mutterValue.setText("MutterValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(mutterValue, gbc);
        lblVater = new JLabel();
        lblVater.setText("Vater:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lblVater, gbc);
        vaterValue = new JLabel();
        vaterValue.setText("VaterValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(vaterValue, gbc);
        lblRechnungsempfaenger = new JLabel();
        lblRechnungsempfaenger.setText("Rechnungsempfänger:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lblRechnungsempfaenger, gbc);
        rechnungsempfaengerValue = new JLabel();
        rechnungsempfaengerValue.setHorizontalAlignment(10);
        rechnungsempfaengerValue.setHorizontalTextPosition(11);
        rechnungsempfaengerValue.setText("RechnungsempfaengerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(rechnungsempfaengerValue, gbc);
        infoIdentischeAdressen = new JLabel();
        infoIdentischeAdressen.setText("Info identische Adressen");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 20;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(infoIdentischeAdressen, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer4, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Geschwister:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label1, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer5, gbc);
        geschwisterValue = new JLabel();
        geschwisterValue.setText("GeschwisterValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(geschwisterValue, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer6, gbc);
        lblAndereSchuelerGleicherRechnungsempfaenger1 = new JLabel();
        lblAndereSchuelerGleicherRechnungsempfaenger1.setText("Andere Schüler mit gleichem");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel3.add(lblAndereSchuelerGleicherRechnungsempfaenger1, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 21;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer10, gbc);
        lblAndereSchuelerGleicherRechnungsempfaenger2 = new JLabel();
        lblAndereSchuelerGleicherRechnungsempfaenger2.setText("Rechnungsempfänger:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lblAndereSchuelerGleicherRechnungsempfaenger2, gbc);
        schuelerGleicherRechnungsempfaengerValue = new JLabel();
        schuelerGleicherRechnungsempfaengerValue.setText("SchuelerGleicherRechnungsempfaengerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(schuelerGleicherRechnungsempfaengerValue, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Anmeldedatum:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label2, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 19;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer11, gbc);
        anmeldedatumValue = new JLabel();
        anmeldedatumValue.setText("AnmeldedatumValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(anmeldedatumValue, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 18;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer12, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Geburtsdatum:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label3, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer13, gbc);
        geburtsdatumValue = new JLabel();
        geburtsdatumValue.setText("GeburtsdatumValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(geburtsdatumValue, gbc);
        abmeldedatum = new JLabel();
        abmeldedatum.setText("Abmeldedatum:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel3.add(abmeldedatum, gbc);
        abmeldedatumValue = new JLabel();
        abmeldedatumValue.setText("AbmeldedatumValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel3.add(abmeldedatumValue, gbc);
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
        return contentPane;
    }

}
