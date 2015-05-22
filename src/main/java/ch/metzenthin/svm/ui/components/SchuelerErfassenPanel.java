package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.AngehoerigerModel;
import ch.metzenthin.svm.domain.model.SchuelerModel;
import ch.metzenthin.svm.ui.control.CompletedListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Hans Stamm
 */
public class SchuelerErfassenPanel {
    private JPanel panel;
    private SchuelerPanel schuelerPanel;
    private AngehoerigerPanel mutterPanel;
    private AngehoerigerPanel vaterPanel;
    private AngehoerigerPanel drittempfaengerPanel;
    private final SchuelerModel schuelerModel;
    private final AngehoerigerModel mutterModel;
    private final AngehoerigerModel vaterModel;
    private final AngehoerigerModel drittempfaengerModel;
    private JPanel btnPanel;
    private JButton btnSpeichern;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    private boolean isSchuelerModelCompleted;
    private boolean isMutterModelCompleted;
    private boolean isVaterModelCompleted;
    private boolean isDrittempfaengerModelCompleted;
    private boolean isMutterRechnungsempfaenger;
    private boolean isVaterRechnungsempfaenger;
    private boolean isDrittempfaengerRechnungsempfaenger;

    public SchuelerErfassenPanel(SvmContext svmContext) {
        $$$setupUI$$$();
        // todo in controller und model auslagern?
        btnSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSpeichern();
            }
        });
        btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
        schuelerModel = svmContext.getModelFactory().createSchuelerModel();
        schuelerPanel.setModel(schuelerModel);
        schuelerModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onSchuelerModelCompleted(completed);
            }
        });
        mutterModel = svmContext.getModelFactory().createAngehoerigerModel();
        mutterPanel.setModel(mutterModel);
        mutterModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onMutterModelCompleted(completed);
            }
        });
        mutterModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                onMutterModelPropertyChange(evt);
            }
        });
        vaterModel = svmContext.getModelFactory().createAngehoerigerModel();
        vaterPanel.setModel(vaterModel);
        vaterModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onVaterModelCompleted(completed);
            }
        });
        vaterModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                onVaterModelPropertyChange(evt);
            }
        });
        drittempfaengerModel = svmContext.getModelFactory().createAngehoerigerModel();
        drittempfaengerPanel.setModel(drittempfaengerModel);
        drittempfaengerModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onDrittempfaengerModelCompleted(completed);
            }
        });
        drittempfaengerModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                onDrittempfaengerModelPropertyChange(evt);
            }
        });
        schuelerModel.checkCompleted();
        mutterModel.checkCompleted();
        vaterModel.checkCompleted();
        drittempfaengerModel.checkCompleted();
    }

    // todo schöner programmieren (nächste drei methoden)
    private void onMutterModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(vaterModel, drittempfaengerModel);
            }
            isMutterRechnungsempfaenger = newValue;
        }
    }

    private void onVaterModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, drittempfaengerModel);
            }
            isVaterRechnungsempfaenger = newValue;
        }
    }

    private void onDrittempfaengerModelPropertyChange(PropertyChangeEvent evt) {
        if (isRechnungsempfaengerPropertyChange(evt)) {
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                uncheckRechnungsempfaenger(mutterModel, vaterModel);
            }
            isDrittempfaengerRechnungsempfaenger = newValue;
        }
    }

    private boolean isRechnungsempfaengerPropertyChange(PropertyChangeEvent evt) {
        return "Rechnungsempfaenger".equals(evt.getPropertyName());
    }

    private boolean isBooleanNewValuePropertyChecked(Boolean newValue) {
        return (newValue != null) && newValue;
    }

    private void uncheckRechnungsempfaenger(AngehoerigerModel... angehoerigerModels) {
        for (AngehoerigerModel angehoerigerModel : angehoerigerModels) {
            angehoerigerModel.setIsRechnungsempfaenger(false);
        }
    }

    private void onSchuelerModelCompleted(boolean completed) {
        System.out.println("SchuelerModel completed=" + completed);
        isSchuelerModelCompleted = completed;
        setEnabledBtnSpeichern();
    }

    private void onMutterModelCompleted(boolean completed) {
        System.out.println("MutterModel completed=" + completed);
        isMutterModelCompleted = completed;
        setEnabledBtnSpeichern();
    }

    private void onVaterModelCompleted(boolean completed) {
        System.out.println("VaterModel completed=" + completed);
        isVaterModelCompleted = completed;
        setEnabledBtnSpeichern();
    }

    private void onDrittempfaengerModelCompleted(boolean completed) {
        System.out.println("DrittempfaengerModel completed=" + completed);
        isDrittempfaengerModelCompleted = completed;
        setEnabledBtnSpeichern();
    }

    private void setEnabledBtnSpeichern() {
        System.out.println("isSetRechnungsempfaenger=" + isSetRechnungsempfaenger() + ",isSchuelerModelCompleted=" + isSchuelerModelCompleted + ",isMutterModelCompleted=" + isMutterModelCompleted + ",isVaterModelCompleted=" + isVaterModelCompleted + ",isDrittempfaengerModelCompleted=" + isDrittempfaengerModelCompleted);
        // todo invoke later?
        btnSpeichern.setEnabled(isSetRechnungsempfaenger() && isSchuelerModelCompleted && isMutterModelCompleted && isVaterModelCompleted && isDrittempfaengerModelCompleted);
    }

    private boolean isSetRechnungsempfaenger() {
        return isMutterRechnungsempfaenger || isVaterRechnungsempfaenger || isDrittempfaengerRechnungsempfaenger;
    }

    private void onAbbrechen() {
        System.out.println("SchuelerErfassenPanel Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
        // todo Dialog, ob wirklich abgebrochen werden soll
    }

    private void onSpeichern() {
        System.out.println("SchuelerErfassenPanel Speichern gedrückt");
        schuelerModel.save();
        // todo Dialog "erfolgreich gespeichert"
        closeListener.actionPerformed(new ActionEvent(btnSpeichern, ActionEvent.ACTION_PERFORMED, "Close nach Speichern"));
    }

    public void addCloseListener(ActionListener actionListener) {
        closeListener = actionListener;
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
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Schüler", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel2.getFont().getName(), Font.BOLD, panel2.getFont().getSize()), new Color(-16777216)));
        schuelerPanel = new SchuelerPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(schuelerPanel.$$$getRootComponent$$$(), gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer2, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(panel3, gbc);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Mutter", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel3.getFont().getName(), Font.BOLD, panel3.getFont().getSize())));
        mutterPanel = new AngehoerigerPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(mutterPanel.$$$getRootComponent$$$(), gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer4, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(panel4, gbc);
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Vater"));
        vaterPanel = new AngehoerigerPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(vaterPanel.$$$getRootComponent$$$(), gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel4.add(spacer6, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(panel5, gbc);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Drittperson Rechnungsempfänger"));
        drittempfaengerPanel = new AngehoerigerPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(drittempfaengerPanel.$$$getRootComponent$$$(), gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel5.add(spacer8, gbc);
        btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.add(btnPanel, BorderLayout.SOUTH);
        btnSpeichern = new JButton();
        btnSpeichern.setText("Speichern");
        btnSpeichern.setMnemonic('S');
        btnSpeichern.setDisplayedMnemonicIndex(0);
        btnPanel.add(btnSpeichern);
        btnAbbrechen = new JButton();
        btnAbbrechen.setText("Abbrechen");
        btnAbbrechen.setMnemonic('A');
        btnAbbrechen.setDisplayedMnemonicIndex(0);
        btnPanel.add(btnAbbrechen);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
