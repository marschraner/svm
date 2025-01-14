package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.domain.model.AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult;
import ch.metzenthin.svm.domain.model.SchuelerErfassenModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

@SuppressWarnings({"java:S100", "java:S1171"})
public class AngehoerigerMehrereEintraegeGleicherNameAndereAttributeDialog extends SchuelerErfassenDialog {

    private final transient AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult;
    private final transient SchuelerErfassenModel schuelerErfassenModel;
    private JPanel contentPane;
    private JButton buttonNeuErfasstenEintragVerwenden;
    private JButton buttonKorrigieren;
    private JLabel lblBeschreibung;
    private JLabel lblAngehoerigeFound;

    public AngehoerigerMehrereEintraegeGleicherNameAndereAttributeDialog(
            AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult,
            SchuelerErfassenModel schuelerErfassenModel
    ) {
        this.angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult = angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult;
        this.schuelerErfassenModel = schuelerErfassenModel;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonNeuErfasstenEintragVerwenden);

        setTitle("Teilweise übereinstimmende Datenbankeinträge für " + angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult.getAngehoerigenArt());

        lblBeschreibung.setText("In der Datenbank wurden mehrere Einträge gefunden, der mit den erfassten Angaben für " + angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult.getAngehoerigenArt() + " teilweise übereinstimmen:");
        setAngehoerigeFound();

        buttonNeuErfasstenEintragVerwenden.addActionListener(e -> onNeuErfasstenEintragVerwenden());

        buttonKorrigieren.addActionListener(e -> onKorrigieren());

        // call onKorrigieren() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onKorrigieren();
            }
        });

        // call onKorrigieren() on ESCAPE
        contentPane.registerKeyboardAction(e -> onKorrigieren(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setAngehoerigeFound() {
        StringBuilder angehoerigeFoundStb = new StringBuilder("<html>");
        angehoerigeFoundStb.append(angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult.getAngehoerigeFoundInDatabase().get(0));
        for (int i = 1; i < angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult.getAngehoerigeFoundInDatabase().size(); i++) {
            angehoerigeFoundStb.append("<br>").append(angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult.getAngehoerigeFoundInDatabase().get(i));
        }
        angehoerigeFoundStb.append("</html>");
        lblAngehoerigeFound.setText(angehoerigeFoundStb.toString());
    }

    private void onNeuErfasstenEintragVerwenden() {
        setResult(schuelerErfassenModel.proceedWeiterfahren(angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult));
        dispose();
    }

    private void onKorrigieren() {
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
        contentPane.add(panel1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel1.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Teilweise übereinstimmende Datenbankeinträge", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel2.getFont()), null));
        lblBeschreibung = new JLabel();
        lblBeschreibung.setText("Beschreibung");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblBeschreibung, gbc);
        lblAngehoerigeFound = new JLabel();
        lblAngehoerigeFound.setText("AngehoerigeFoundInDatabase");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblAngehoerigeFound, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
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
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer5, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        contentPane.add(panel3, BorderLayout.SOUTH);
        buttonKorrigieren = new JButton();
        buttonKorrigieren.setMaximumSize(new Dimension(174, 29));
        buttonKorrigieren.setMinimumSize(new Dimension(174, 29));
        buttonKorrigieren.setPreferredSize(new Dimension(174, 29));
        buttonKorrigieren.setText("Eingabe korrigieren");
        buttonKorrigieren.setMnemonic('E');
        buttonKorrigieren.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel3.add(buttonKorrigieren, gbc);
        buttonNeuErfasstenEintragVerwenden = new JButton();
        buttonNeuErfasstenEintragVerwenden.setMaximumSize(new Dimension(271, 29));
        buttonNeuErfasstenEintragVerwenden.setMinimumSize(new Dimension(271, 29));
        buttonNeuErfasstenEintragVerwenden.setPreferredSize(new Dimension(271, 29));
        buttonNeuErfasstenEintragVerwenden.setText("Neu erfassten Eintrag verwenden");
        buttonNeuErfasstenEintragVerwenden.setMnemonic('N');
        buttonNeuErfasstenEintragVerwenden.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel3.add(buttonNeuErfasstenEintragVerwenden, gbc);
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
        return contentPane;
    }

}
