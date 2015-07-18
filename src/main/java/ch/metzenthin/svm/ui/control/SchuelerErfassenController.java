package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.DeleteSchuelerCommand;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.domain.model.*;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller, der die Models von Schüler erfassen überwacht.
 *
 * Nicht abgeleitet von AbstractController, da dieser Controller keine eigenen Eingabe-Felder hat.
 *
 * @author Hans Stamm
 */
public class SchuelerErfassenController {

    private static final Logger LOGGER = Logger.getLogger(SchuelerErfassenController.class);

    private JButton btnSpeichern;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    private final SvmContext svmContext;
    private final SchuelerErfassenModel schuelerErfassenModel;
    private final boolean isBearbeiten;
    private ActionListener zurueckZuDatenblattListener;
    private ActionListener nextPanelListener;

    public SchuelerErfassenController(SvmContext svmContext, SchuelerErfassenModel schuelerErfassenModel, boolean isBearbeiten) {
        this.svmContext = svmContext;
        this.schuelerErfassenModel = schuelerErfassenModel;
        this.isBearbeiten = isBearbeiten;
        this.schuelerErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onSchuelerErfassenModelCompleted(completed);
            }
        });
    }

    public void constructionDone() {
        schuelerErfassenModel.initializeCompleted();
    }

    private void onSchuelerErfassenModelCompleted(boolean completed) {
        LOGGER.trace("SchuelerErfassenController completed=" + completed);
        if (completed) {
            try {
                schuelerErfassenModel.validate();
                btnSpeichern.setToolTipText(null);
                btnSpeichern.setEnabled(true);
            } catch (SvmValidationException e) {
                LOGGER.trace("SchuelerErfassenController Exception=" + e.getMessage());
                btnSpeichern.setToolTipText(e.getMessage());
                btnSpeichern.setEnabled(false);
            }
        } else {
            btnSpeichern.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnSpeichern.setEnabled(false);
        }
    }

    public void setSchuelerPanel(SchuelerPanel schuelerPanel, SchuelerModel schuelerModel) {
        schuelerPanel.setModel(schuelerModel);
        // Kein Abmeldedatum sichtbar
        schuelerPanel.getLblAbmeldedatum().setVisible(isBearbeiten);
        schuelerPanel.getTxtAbmeldedatum().setVisible(isBearbeiten);
        // Geschlecht-Voreinstellung
        schuelerModel.setGeschlecht(Geschlecht.W);
        schuelerErfassenModel.setSchuelerModel(schuelerModel);
    }

    public void setMutterPanel(AngehoerigerPanel mutterPanel, AngehoerigerModel mutterModel) {
        mutterPanel.setModel(mutterModel);
        // Rechnungsempfänger-Label überschreiben
        mutterPanel.getLblRechnungsempfaenger().setText("Rechnungsempfängerin");
        // Keine Anrede anzeigen
        mutterPanel.getLblAnrede().setVisible(false);
        mutterPanel.getComboBoxAnrede().setVisible(false);
        // Anrede immer auf Frau setzen
        try {
            mutterModel.setAnrede(Anrede.FRAU);
            // Default Rechungsempfängerin
            mutterModel.setIsRechnungsempfaenger(true);
        } catch (SvmValidationException ignore) {
        }
        schuelerErfassenModel.setMutterModel(mutterModel);
    }

    public void setVaterPanel(AngehoerigerPanel vaterPanel, AngehoerigerModel vaterModel) {
        vaterPanel.setModel(vaterModel);
        // Keine Anrede anzeigen
        vaterPanel.getLblAnrede().setVisible(false);
        vaterPanel.getComboBoxAnrede().setVisible(false);
        // Anrede immer auf Herr setzen
        try {
            vaterModel.setAnrede(Anrede.HERR);
        } catch (SvmValidationException ignore) {
        }
        schuelerErfassenModel.setVaterModel(vaterModel);
    }

    public void setDrittempfaengerPanel(AngehoerigerPanel drittempfaengerPanel, AngehoerigerModel drittempfaengerModel) {
        drittempfaengerPanel.setModel(drittempfaengerModel);
        // Anrede: KEINE nicht anzeigen:
        drittempfaengerPanel.getComboBoxAnrede().removeItem(Anrede.KEINE);
        // Keine Adresse Schüler übernehmen-Checkbox anzeigen
        drittempfaengerPanel.getLblGleicheAdresseWieSchueler().setVisible(false);
        drittempfaengerPanel.getCheckBoxGleicheAdresseWieSchueler().setVisible(false);
        // Keine Rechnungsempfänger-Checkbox anzeigen
        drittempfaengerPanel.getLblRechnungsempfaenger().setVisible(false);
        drittempfaengerPanel.getCheckBoxRechnungsempfaenger().setVisible(false);
        try {
            drittempfaengerModel.setAnrede(Anrede.FRAU);
        } catch (SvmValidationException ignore) {
        }
        // Default deaktiviert
        drittempfaengerModel.disableFields();
        schuelerErfassenModel.setDrittempfaengerModel(drittempfaengerModel);
    }

    public void setBtnSpeichern(JButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
        btnSpeichern.setEnabled(false);
        btnSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSpeichern();
            }
        });
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setBtnLoeschen(JButton btnLoeschen) {
        if (!isBearbeiten) {
            btnLoeschen.setVisible(false);
        }
        btnLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoeschen();
            }
        });
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void addZurueckZuDatenblattListener(ActionListener saveSuccessfulListener) {
        this.zurueckZuDatenblattListener = saveSuccessfulListener;
    }

    private void onAbbrechen() {
        LOGGER.trace("SchuelerErfassenPanelAbbrechen gedrückt");
        Object[] options = {"Ja", "Nein"};
        String dialogText;
        if (!isBearbeiten) {
            dialogText = "Durch Drücken des Ja-Buttons wird die Eingabemaske geschlossen. Allfällige Eingaben werden nicht gespeichert. Fortfahren?";
        } else {
            dialogText = "Durch Drücken des Ja-Buttons wird die Eingabemaske geschlossen. Allfällige getätigte Änderungen werden nicht gespeichert. Fortfahren?";
        }
        int n = JOptionPane.showOptionDialog(
                btnAbbrechen.getParent().getParent(),
                dialogText,
                "Eingabemaske schliessen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title
        if (n == 0) {
            if (!isBearbeiten) {
                closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
            } else {
                zurueckZuDatenblattListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Zurück nach Abbrechen"));
            }
        }
    }

    private void onSpeichern() {
        LOGGER.trace("SchuelerErfassenPanel Speichern gedrückt");
        SchuelerErfassenDialog dialog;
        SchuelerErfassenSaveResult schuelerErfassenSaveResult = schuelerErfassenModel.validieren();
        while (schuelerErfassenSaveResult != null) { // Wenn null: kein weiterer Dialog
            dialog = createDialog(schuelerErfassenSaveResult);
            if (dialog == null) {
                break;
            }
            dialog.pack();
            dialog.setVisible(true);
            schuelerErfassenSaveResult = dialog.getResult();
        }
        // Wenn isAbbrechen() zurück zur Eingabemaske, sonst Listener aufrufen (wenn vorhanden), der ein neues Panel aufruft
        if (schuelerErfassenSaveResult != null && schuelerErfassenSaveResult.getResult() == ValidateSchuelerCommand.Result.SPEICHERUNG_ERFOLGREICH) {
            if (zurueckZuDatenblattListener != null) {
                zurueckZuDatenblattListener.actionPerformed(new ActionEvent(btnSpeichern, ActionEvent.ACTION_PERFORMED, "Speichern erfolgreich"));
            } else {
                SchuelerSuchenTableData schuelerSuchenTableData = schuelerErfassenModel.getSchuelerSuchenTableData();
                SchuelerSuchenTableModel schuelerSuchenTableModel = new SchuelerSuchenTableModel(schuelerSuchenTableData);
                SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, 0, false);
                schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
                schuelerDatenblattPanel.addCloseListener(closeListener);
                nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler ausgewählt"));
            }
        }
    }

    private void onLoeschen() {
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Durch Drücken des Ja-Buttons wird der Schüler unwiderruflich aus der Datenbank gelöscht. Fortfahren?",
                "Schüler löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteSchuelerCommand.Result result = schuelerErfassenModel.loeschen();
            switch (result) {
                case SCHUELER_IN_KURSE_EINGESCHRIEBEN:
                    JOptionPane.showMessageDialog(null, "Der Schüler ist in mindestestens einem Kurs eingeschrieben und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    JOptionPane.showMessageDialog(
                            null,
                            "Der Schüler wurde erfolgreich aus der Datenbank gelöscht.",
                            "Löschen erfolgreich",
                            JOptionPane.INFORMATION_MESSAGE);
                    closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Löschen"));
                    break;
            }
        }
    }

    private SchuelerErfassenDialog createDialog(SchuelerErfassenSaveResult schuelerErfassenSaveResult) {
        final SchuelerErfassenDialog[] dialog = new SchuelerErfassenDialog[1]; // Array, weil Variable final sein muss
        schuelerErfassenSaveResult.accept(new SchuelerErfassenSaveResultVisitor() {
            @Override
            public void visit(ValidateSchuelerSummaryResult validateSchuelerSummaryResult) {
                dialog[0] = new ValidateSchuelerSummaryDialog(validateSchuelerSummaryResult, schuelerErfassenModel);
            }

            @Override
            public void visit(AngehoerigerMehrereEintraegePassenResult angehoerigerMehrereEintraegePassenResult) {
                dialog[0] = new AngehoerigerMehrereEintraegePassenDialog(angehoerigerMehrereEintraegePassenResult, schuelerErfassenModel);
            }

            @Override
            public void visit(AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult) {
                dialog[0] = new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeDialog(angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult, schuelerErfassenModel);
            }

            @Override
            public void visit(AngehoerigerEinEintragGleicherNameAndereAttributeResult angehoerigerEinEintragGleicherNameAndereAttributeResult) {
                dialog[0] = new AngehoerigerEinEintragGleicherNameAndereAttributeDialog(angehoerigerEinEintragGleicherNameAndereAttributeResult, schuelerErfassenModel);
            }

            @Override
            public void visit(AngehoerigerEinEintragPasstResult angehoerigerEinEintragPasstResult) {
                dialog[0] = new AngehoerigerEinEintragPasstDialog(angehoerigerEinEintragPasstResult, schuelerErfassenModel);
            }

            @Override
            public void visit(SchuelerBereitsInDatenbankResult schuelerBereitsInDatenbankResult) {
                JOptionPane.showMessageDialog(
                        null,
                        schuelerBereitsInDatenbankResult.getErrorMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                schuelerErfassenModel.abbrechen();
                dialog[0] = null;
            }

            @Override
            public void visit(SchuelerErfassenSaveOkResult schuelerErfassenSaveOkResult) {
                JOptionPane.showMessageDialog(
                        null,
                        (isBearbeiten ? schuelerErfassenSaveOkResult.getTextSchuelerBearbeiten() : schuelerErfassenSaveOkResult.getTextSchuelerErfassen()),
                        (isBearbeiten ? schuelerErfassenSaveOkResult.getTitleSchuelerBearbeiten() : schuelerErfassenSaveOkResult.getTitleSchuelerErfassen()),
                        JOptionPane.INFORMATION_MESSAGE);
                dialog[0] = null;
            }

            @Override
            public void visit(SchuelerErfassenUnerwarteterFehlerResult schuelerErfassenUnerwarteterFehlerResult) {
                throw new RuntimeException(schuelerErfassenUnerwarteterFehlerResult.getFehler());
            }

            @Override
            public void visit(DrittpersonIdentischMitElternteilResult drittpersonIdentischMitElternteilResult) {
                JOptionPane.showMessageDialog(
                        null,
                        drittpersonIdentischMitElternteilResult.getErrorMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                schuelerErfassenModel.abbrechen();
                dialog[0] = null;
            }
        });
        return dialog[0];
    }

}
