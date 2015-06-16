package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.model.SchuelerErfassenModel;
import ch.metzenthin.svm.domain.model.ValidateSchuelerSummaryResult;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    private JLabel infoGeschwister;

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
            setSchuelerAngehoerige();
            setInfoIdentischeAdressen();
            setGeschwisterSchuelerRechungsempfaenger();
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

    private void setSchuelerAngehoerige() {
        lblSchueler.setText(validateSchuelerSummaryResult.getSchueler().getGeschlecht() == Geschlecht.W ? "Schülerin:" : "Schüler:");
        schuelerValue.setText(validateSchuelerSummaryResult.getSchueler().toString());
        String neuMutter = ((validateSchuelerSummaryResult.getSchueler().getMutter() != null && validateSchuelerSummaryResult.isMutterNeu()) ? " (neu)" : "");
        lblMutter.setText("Mutter" + neuMutter + ":");
        mutterValue.setText(validateSchuelerSummaryResult.getSchueler().getMutter() == null ? "-" : validateSchuelerSummaryResult.getSchueler().getMutter().toString());
        String neuVater = ((validateSchuelerSummaryResult.getSchueler().getVater() != null && validateSchuelerSummaryResult.isVaterNeu()) ? " (neu)" : "");
        lblVater.setText("Vater" + neuVater + ":");
        vaterValue.setText(validateSchuelerSummaryResult.getSchueler().getVater() == null ? "-" : validateSchuelerSummaryResult.getSchueler().getVater().toString());
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
            lblRechnungsempfaengerText = (validateSchuelerSummaryResult.getSchueler().getRechnungsempfaenger().getAnrede() == Anrede.FRAU ? "Rechnungsempfängerin" + neuRechnungsempfaenger + ":" : "Rechnungsempfänger" + neuRechnungsempfaenger + ":");
            rechnungsempfaengerText = validateSchuelerSummaryResult.getSchueler().getRechnungsempfaenger().toString();
        }
        lblRechnungsempfaenger.setText(lblRechnungsempfaengerText);
        rechnungsempfaengerValue.setText(rechnungsempfaengerText);
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

    private void setGeschwisterSchuelerRechungsempfaenger() {
        StringBuilder infoGeschwisterStb = new StringBuilder("<html>");
        String schuelerVornameNachname = validateSchuelerSummaryResult.getSchueler().getVorname() + " " + validateSchuelerSummaryResult.getSchueler().getNachname();
        if (validateSchuelerSummaryResult.getGeschwister() == null || validateSchuelerSummaryResult.getGeschwister().isEmpty()) {
            infoGeschwisterStb.append(schuelerVornameNachname).append(" hat keine angemeldeten Geschwister.");

        } else {
            infoGeschwisterStb.append("Angemeldete Geschwister von ").append(schuelerVornameNachname).append(":");
            for (Schueler schueler1 : validateSchuelerSummaryResult.getGeschwister()) {
                infoGeschwisterStb.append("<br>").append(schueler1.toString());
            }
        }

        if (validateSchuelerSummaryResult.getAndereSchueler() != null && !validateSchuelerSummaryResult.getAndereSchueler().isEmpty()) {
            infoGeschwisterStb.append("<br>Andere Schüler, welche den Vater, die Mutter oder den Rechnungsempfänger von ").append(schuelerVornameNachname).append(" als Rechnungsempfänger haben:");
            for (Schueler schueler1 : validateSchuelerSummaryResult.getAndereSchueler()) {
                infoGeschwisterStb.append("<br>").append(schueler1.toString());
            }
        }
        infoGeschwisterStb.append("</html>");
        infoGeschwister.setText(infoGeschwisterStb.toString());
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

    public static void main(String[] args) {
        ValidateSchuelerSummaryDialog dialog = new ValidateSchuelerSummaryDialog(null, null);
        dialog.setMinimumSize(new Dimension(50, 175));
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
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
        contentPane.add(panel1, BorderLayout.CENTER);
        lblSchueler = new JLabel();
        lblSchueler.setText("Schüler:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 5);
        panel1.add(lblSchueler, gbc);
        lblMutter = new JLabel();
        lblMutter.setText("Mutter:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel1.add(lblMutter, gbc);
        schuelerValue = new JLabel();
        schuelerValue.setText("SchuelerValue");
        schuelerValue.setVerticalAlignment(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 0, 5);
        panel1.add(schuelerValue, gbc);
        mutterValue = new JLabel();
        mutterValue.setText("MutterValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel1.add(mutterValue, gbc);
        lblVater = new JLabel();
        lblVater.setText("Vater:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel1.add(lblVater, gbc);
        vaterValue = new JLabel();
        vaterValue.setText("VaterValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel1.add(vaterValue, gbc);
        lblRechnungsempfaenger = new JLabel();
        lblRechnungsempfaenger.setText("Rechnungsempfänger:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel1.add(lblRechnungsempfaenger, gbc);
        rechnungsempfaengerValue = new JLabel();
        rechnungsempfaengerValue.setHorizontalAlignment(10);
        rechnungsempfaengerValue.setHorizontalTextPosition(11);
        rechnungsempfaengerValue.setText("RechnungsempfaengerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel1.add(rechnungsempfaengerValue, gbc);
        infoIdentischeAdressen = new JLabel();
        infoIdentischeAdressen.setText("Info identische Adressen");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 5);
        panel1.add(infoIdentischeAdressen, gbc);
        infoGeschwister = new JLabel();
        infoGeschwister.setText("Info Geschwister");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 5, 5);
        panel1.add(infoGeschwister, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        contentPane.add(panel2, BorderLayout.SOUTH);
        buttonAbbrechen = new JButton();
        buttonAbbrechen.setText("Abbrechen");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel2.add(buttonAbbrechen, gbc);
        buttonSpeichern = new JButton();
        buttonSpeichern.setHorizontalAlignment(0);
        buttonSpeichern.setHorizontalTextPosition(11);
        buttonSpeichern.setText("Speichern");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel2.add(buttonSpeichern, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
